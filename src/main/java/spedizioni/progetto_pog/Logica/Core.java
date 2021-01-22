package spedizioni.progetto_pog.Logica;

import java.util.ArrayList;


/**
 * <strong>Core</strong> permette di creare l'oggetto principe dell'applicazione
 * che verrà utilizzato per gestire la logica dietro l'applicazione.
 * @author nicholaslopiccolo
 */
public class Core {
    private ThreadStato worker;
    private Database db;
    
    private User current_user;
    private boolean admin = false;
    private int user_index = -1;
    
    /**
     * Il costruttore crea il database ed esegue il load dei dati da file
     * in'oltre istanzia il thread di cambio stato e lo avvia.
     * @param perc Percentuale di fallimento
     * @param s Secondi per spin del thread di cambio stato
     */
    public Core(int perc,int s){
        db = new Database();
        db.load();
        
        worker = new ThreadStato(this,perc,s);
        worker.start();
    }
    /**
     *  Ritorna l'utente corrente
     *  @return User
     *  @see User
     */
    public User getCurrentUser() {return current_user;}
    /**
    *   Ritorna il valore del permesso admin
    *   @return boolean admin oppure cliente standard  
    *   @see boolean
    */
    public boolean isAdmin() {return admin;}
    /**
     *  Scrive il database nei relativi file
     */
    public void write(){
        if(user_index>-1)
            db.utenti.replace(user_index, current_user);
        db.write();
    }
    /**
    *   Imposta lo user loggato al momento
    *   @param user indica il nuovo user corrente
    */
    private void setUser(User user){current_user = user;}
    /**
     *
     * Registrazione di un nuovo utente, prima fa un controlo per accertarsi 
     * che lo user non stia provando ad eseguire una registrazione come admin, in seguito
     * controlla che non esista uno user con lo stesso username.
     * In fine crea lo user qualora lo username sia libero.
     * @param username indica lo username del nuovo cliente
     * @param password indica la password del nuovo cliente
     * @param indirizzo indica l'indirizzo associato al nuovo cliente
     * @return     registrazione eseguita con successo o meno
     * @see boolean
     */
    public boolean register(String username,String password, String indirizzo){
        username = username.toLowerCase();
        boolean free = true;
        
        //Il tentativo di creazione username admin fallisce
        if(username.equals("admin")) return false;
        int i;
        for(i=0;i<db.utenti.size();i++)
            if(db.utenti.get(i).getUsername().equals(username))
                free = false;
        
        if(free){
            User u = new User(username,password,indirizzo);
            db.utenti.add(u);
            setUser(u);
            user_index = ++i;
            return true;
        }
        return false;
    }
    /**
     *
     *   Funzione di login per l'applicazione in caso di admin esegue un login
     *   statico in caso di user normale cerca di reperire i dati nel database. 
     *   @param username indica lo username 
     *   @param password indica la password 
     *   @return boolean Login eseguito con successo o meno
     *   @see boolean
     */
    public boolean login(String username,String password){
        username = username.toLowerCase();
        
        if("admin".equals(username) && "toor".equals(password)){
            setUser(new User("admin","",""));
            admin=true;
            return true;
        }
        
        for(int i=0;i<db.utenti.size();i++){
            User u = db.utenti.get(i);
            
            if(u.getUsername().equals(username) && password.equals(u.getPassword())){
                setUser(u);
                return true;
            }
        }
        return false;
    }
    /**
     * Disconnette l'utente dall'applicazione e permette di rieseguire il login.
     * Imposta lo user corrente a null, lo user_index a -1 e l'admin a false;
     */
    public void logout(){
        setUser(null);
        user_index = -1;
        admin=false;
        
        System.out.println("Logout eseguito");
    }
     
    // Logiche applicative
    /**
     * Restituisce la lista delle spedizioni prese dal database:
     * Se lo user corrente è l'amministratore di sistema restituisce l'intera 
     * lista in caso contrario rimuove le spedizioni di cui lo user non è proprietario.
     * @return ArrayList
     * @see ArrayList
     */
    public ArrayList getSpedizioni() {
        ArrayList lista = db.getSpedizioni();
        
        if(isAdmin()) return lista;
        
        String username = current_user.getUsername();
                
        for(int i=0;i<lista.size();i++){
            Object sped_o = lista.get(i);
            int j= -1;
            String codice;
            
            if(sped_o instanceof SpedizioneAssicurata){
                codice = ((SpedizioneAssicurata)sped_o).getCodice();
                j = codice.indexOf(username);
            }
            else{
                codice = ((Spedizione)sped_o).getCodice();
                j = codice.indexOf(username);
            }
                
            if(j<0 && codice.indexOf("-") != username.length()){
                lista.remove(i);
                i--;
            }
        }
        return lista;
    }
    /**
     * Aggiunge una spedizione al database, se valore assicurato è minore o uguale di zero
     * allora la nuova spedizione e di tipo Spedizione in caso contrario è di tipo
     * SpedizioneAssicurata.
     * Incremento il contatore di spedizioni dello user e creo il codice concatenando
     * lo username e il numero di spedizioni, in seguito la nuova spedizione verrà aggiunta 
     * all'apposita lista del database.
     * @param dest Destinazione della spedizione
     * @param peso Peso della spedizione
     * @param valAssi valore assicurato
     */
    public void aggiungiSpedizione(String dest,double peso,double valAssi){
        current_user.nuovaSpedizione();
        String codice = current_user.getUsername()+"-"+current_user.getNroSpedizioni();

        if(valAssi<=0)db.sped_std.add(new Spedizione(codice,dest,peso));
        else db.sped_assi.add(new SpedizioneAssicurata(codice,dest,peso,valAssi));
        
        System.out.println("Spedizione Aggiunta: "+codice);
    }
    /**
     * Definiscono la possibilità o meno di passare da un vecchio stato ad uno nuovo.
     * @param vecchio Stato attuale della spedizione
     * @param nuovo Nuovo stato della spedizione
     * @return boolean
     * @see boolean
     */
    private boolean logicheAggiornamentoStato(Stato vecchio, Stato nuovo){
        switch(vecchio){
            case FALLITO -> {
                if(admin == false && nuovo==Stato.RIMBORSO_RICHIESTO)
                    return true;
                break;
            }
            case PREPARAZIONE -> {
                if(admin)
                    if(nuovo==Stato.TRANSITO || nuovo==Stato.FALLITO)
                        return true;
                break;
            }
            case TRANSITO -> {
                if(admin)
                    if(nuovo==Stato.RICEVUTO || nuovo==Stato.FALLITO)
                        return true;
                break;
            }
            case RIMBORSO_RICHIESTO -> {
                if(admin)
                    if(nuovo==Stato.RIMBORSO_EROGATO) return true;
                break;
            }
        }
        return false;
    }
    /**
     * Aggiorna lo stato della spedizione.
     * Prima cerca la spedizione nel database sfruttandone il codice, in seguito
     * esegue un controllo tramite la funzione logicheAggiornamento in caso di controllo
     * positivo modifica lo stato ed esegue un replace della spedizione nel database.
     * @param codice Codice univoco della spedizione
     * @param nuovo_stato Nuovo stato della spedizione
     * @return boolean
     * @see boolean
     */
    public boolean aggiornaStatoSpedizione(String codice,Stato nuovo_stato){
        boolean ok = false;
        
        Object spedizione = db.trovaSpedizione(codice);
        
        if(spedizione != null)
            if(spedizione instanceof SpedizioneAssicurata){
                SpedizioneAssicurata assi = (SpedizioneAssicurata)spedizione;
                if (logicheAggiornamentoStato(assi.getStatoConsegna(),nuovo_stato)){
                    int i = db.sped_assi.find(assi);
                    ok = assi.setStatoConsegna(nuovo_stato);
                    db.sped_assi.replace(i, assi);
                }
            }
            else{
                Spedizione std = (Spedizione)spedizione;
                if (logicheAggiornamentoStato(std.getStatoConsegna(),nuovo_stato)){
                    int i = db.sped_std.find(std);
                    ok = std.setStatoConsegna(nuovo_stato);
                    db.sped_std.replace(i, std);
                }
            }
        return ok;
    }
    /**
     * Controlla se la spedizione contraddistinta dal codice univoco sia in stato
     * finale oppure no. Esegue una ricerca della spedizione nel database e 
     * chiama la funzione inStato finale della spedizione.
     * @param codice Codice univoco della spedizione
     * @return boolean
     * @see boolean
     */
    public boolean isSpedizioneStatoFinale(String codice){
        
        Object spedizione = db.trovaSpedizione(codice);
        
        if(spedizione != null)
            if(spedizione instanceof SpedizioneAssicurata)
                return ((SpedizioneAssicurata)spedizione).isStatoFinale();
            else
                return ((Spedizione)spedizione).isStatoFinale();
        
        return false;
    }
    /**
     * Elimina la spedizione dal database. Esegue una ricerca della spedizione sul database
     * tramite il codice ed infine la elimina.
     * @param codice Codice univoco della spedizione
     */
    public void eliminaSpedizione(String codice){
        
        Object spedizione = db.trovaSpedizione(codice);
        
        if(spedizione != null)
            if(spedizione instanceof SpedizioneAssicurata)
                db.sped_assi.remove((SpedizioneAssicurata)spedizione);
            else
                db.sped_std.remove((Spedizione)spedizione);
    }
    
    // Funzioni per il thread
    /**
     * Restituisce l'intera lista di spedizioni
     * @return ArrayList
     * @see ArrayList
     */
    public ArrayList getSpedizioniThread() {return db.getSpedizioni();}
    /**
     * Definiscono la possibilità o meno di passare da un vecchio stato ad uno nuovo,
     * nel caso spedifico del thread.
     * @param vecchio Stato attuale della spedizione
     * @param nuovo Nuovo stato della spedizione
     * @return boolean
     * @see boolean
     */
    private boolean logicheAggiornamentoStatoThread(Stato vecchio, Stato nuovo){
        switch(vecchio){
            case PREPARAZIONE -> {
                if(nuovo==Stato.TRANSITO || nuovo==Stato.FALLITO)
                    return true;
                break;
            }
            case TRANSITO -> {
                if(nuovo==Stato.RICEVUTO || nuovo==Stato.FALLITO)
                    return true;
                break;
            }
            case RIMBORSO_RICHIESTO ->{
                if(nuovo==Stato.RIMBORSO_EROGATO)
                    return true;
                break;
            }
        }
        return false;
    }
    /**
     * Controlla se la spedizione contraddistinta dal codice univoco sia in stato
     * finale oppure no. Esegue una ricerca della spedizione nel database e 
     * chiama la funzione inStato finale della spedizione. Aggiorna lo stato con
     * delle logiche specifiche per il thread.
     * @param codice Codice univoco della spedizione
     * @param nuovo_stato Nuovo stato della spedizione
     * @return boolean
     * @see boolean
     */
    public boolean aggiornaStatoSpedizioneThread(String codice,Stato nuovo_stato){
         boolean ok = false;
        
        Object spedizione = db.trovaSpedizione(codice);
        
        if(spedizione != null)
            if(spedizione instanceof SpedizioneAssicurata){
                SpedizioneAssicurata assi = (SpedizioneAssicurata)spedizione;
                if (logicheAggiornamentoStatoThread(assi.getStatoConsegna(),nuovo_stato)){
                    int i = db.sped_assi.find(assi);
                    ok = assi.setStatoConsegna(nuovo_stato);
                    db.sped_assi.replace(i, assi);
                }
            }
            else{
                Spedizione std = (Spedizione)spedizione;
                if (logicheAggiornamentoStatoThread(std.getStatoConsegna(),nuovo_stato)){
                    int i = db.sped_std.find(std);
                    ok = std.setStatoConsegna(nuovo_stato);
                    db.sped_std.replace(i, std);
                }
            }
        
        return ok;
    }
}
