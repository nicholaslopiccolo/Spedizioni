/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import static java.lang.Integer.max;
import java.util.ArrayList;


/**
 *
 * @author giokk
 */
public class Core {
    private ThreadStato worker;
    private Database db;
    
    private User current_user;
    private boolean admin = false;
    private int user_index = -1;
    
    /**
     *
     */
    public Core(int perc,int s){
        db = new Database();
        db.load();
        
        worker = new ThreadStato(this,perc,s);
        worker.start();
    }
    //GETTERS

    /**
     *  Ritorna l'utente corrente
     *  @return User
     *  @see User
     */
    public User getCurrentUser() {return current_user;}
    /**
    *   Ritorna il valore del permesso admin
    *   @return     admin oppure cliente standard  
    *   @see boolean
    */
    public boolean isAdmin() {return admin;}
    
    //UTILS

    /**
     *  Scrive il database nei relativi file
     * 
     */
    public void write(){
        if(user_index>-1)
            db.utenti.replace(user_index, current_user);
        db.write();
    }
    
    // Funzioni Utente
    
    /**
    *   Imposta lo user loggato al momento
    *   @param user indica il nuovo user corrente
    */
    private void setUser(User user){current_user = user;}
    /**
     *
     *   Registrazione di un nuovo utente
     *   @param username indica lo username del nuovo cliente
     *   @param password indica la password del nuovo cliente
     *   @param indirizzo indica l'indirizzo associato al nuovo cliente
     *   @return     registrazione eseguita con successo o meno
     *   @see boolean
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
     *   Funzione di login per l'applicazione
     *   @param username indica lo username 
     *   @param password indica la password 
     *   @return     login eseguito con successo o meno
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
     *  Disconnette l'utente dall'applicazione, 
     *  permette di rieseguirei l login
     */
    public void logout(){
        setUser(null);
        user_index = -1;
        admin=false;
        
        System.out.println("Logout eseguito");
    }
     
    // Logiche applicativo
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
            System.out.println("Codice: "+codice+" J: "+j);
                
            if(j<0 && codice.indexOf("-") != username.length()){
                lista.remove(i);
                i--;
            }
        }
        return lista;
    }
    public ArrayList<SpedizioneAssicurata> getSpedizioniAssicurateUtente() {
        ArrayList<SpedizioneAssicurata> lista = new ArrayList<SpedizioneAssicurata>();
        String username = current_user.getUsername();
        
        for(int i=0;i<db.sped_assi.size();i++){
            SpedizioneAssicurata assi = db.sped_assi.get(i);
            String codice = assi.getCodice();
            int j = codice.indexOf(username);

            if(j>-1 && codice.indexOf("-") == username.length())
                lista.add(assi);
        }
        
        return lista;
    }
    
    public void aggiungiSpedizione(String dest,double peso,double valAssi){
        current_user.nuovaSpedizione();
        String codice = current_user.getUsername()+"-"+current_user.getNroSpedizioni();

        if(valAssi<=0)db.sped_std.add(new Spedizione(codice,dest,peso));
        else db.sped_assi.add(new SpedizioneAssicurata(codice,dest,peso,valAssi));
        
        System.out.println("Spedizione Aggiunta: "+codice);
    }
    
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
    
    public boolean isSpedizioneStatoFinale(String codice){
        
        Object spedizione = db.trovaSpedizione(codice);
        
        if(spedizione != null)
            if(spedizione instanceof SpedizioneAssicurata)
                return ((SpedizioneAssicurata)spedizione).isStatoFinale();
            else
                return ((Spedizione)spedizione).isStatoFinale();
        
        return false;
    }
    
    public void eliminaSpedizione(String codice){
        
        Object spedizione = db.trovaSpedizione(codice);
        
        if(spedizione != null)
            if(spedizione instanceof SpedizioneAssicurata)
                db.sped_assi.remove((SpedizioneAssicurata)spedizione);
            else
                db.sped_std.remove((Spedizione)spedizione);
    }
    
    // Funzioni per il thread
    public ArrayList getSpedizioniThread() {return db.getSpedizioni();}
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
