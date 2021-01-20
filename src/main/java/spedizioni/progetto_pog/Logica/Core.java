/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

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
        
        worker = new ThreadStato(this,perc,s);// 10%, 20 secondi
        worker.start();
    }
    public DBGenerico<Spedizione> getSpedizioni(){
        return db.sped_std;
    }
    public DBGenerico<SpedizioneAssicurata> getSpedizioniAssicurate(){
        return db.sped_assi;
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
     *   Login di un utente
     *   @param username indica lo username del cliente
     *   @param password indica la password del cliente
     *   @return     login eseguito con successo o meno
     *   @see boolean
     */
    public boolean login(String username,String password){
        username = username.toLowerCase();
        
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
     *
     *   Login Admin
     *   @param username indica lo username
     *   @param password indica la password
     *   @return     login eseguito con successo o meno
     *   @see boolean
     */
    public boolean loginAdmin(String username,String password){
        username = username.toLowerCase();
        if("admin".equals(username) && "toor".equals(password)){
            setUser(new User("admin","",""));
            admin=true;
            return true;
        }
        admin = false;
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
    
    public ArrayList<Spedizione> getSpedizioniUtente() {
        ArrayList<Spedizione> lista = new ArrayList<Spedizione>();
        String username = current_user.getUsername();
        
        for(int i=0;i<db.sped_std.size();i++){
            Spedizione std = db.sped_std.get(i);
            String codice = std.getCodice();
            int j = codice.indexOf(username);

            if(j>-1 && codice.indexOf("-") == username.length()){
                lista.add(std);
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
    
    public void aggiungiSpedizione(String dest,double peso){
        current_user.nuovaSpedizione();
        
        String codice = current_user.getUsername()+"-"+current_user.getNroSpedizioni();
        Spedizione std = new Spedizione(codice,dest,peso);
        
        db.sped_std.add(std);
        System.out.println("Spedizione Aggiunta: "+std.toString());
    }
    public void aggiungiSpedizioneAssicurata(String dest,double peso,double val_assicurato){
        current_user.nuovaSpedizione();
        
        String codice = current_user.getUsername()+"-"+current_user.getNroSpedizioni();
        SpedizioneAssicurata assi = new SpedizioneAssicurata(codice,dest,peso,val_assicurato);
        
        db.sped_assi.add(assi);
        System.out.println("Spedizione Assicurata Aggiunta: "+assi.toString());
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
        
        for(int i=0;i<db.sped_std.size();i++){
            Spedizione std = db.sped_std.get(i);
            if(codice.equals(std.getCodice()) && logicheAggiornamentoStato(std.getStatoConsegna(),nuovo_stato)){
                ok = std.setStatoConsegna(nuovo_stato);
                db.sped_std.replace(i, std);
                return ok;
            }
        }

        for(int i=0;i<db.sped_assi.size();i++){
            SpedizioneAssicurata assi = db.sped_assi.get(i);
            if(codice.equals(assi.getCodice()) && logicheAggiornamentoStato(assi.getStatoConsegna(),nuovo_stato)){
                ok = assi.setStatoConsegna(nuovo_stato);
                db.sped_assi.replace(i, assi);
                return ok;
            }
        }
        return ok;
    }
    
    public boolean isSpedizioneStatoFinale(String codice){
        
        for(int i=0;i<db.sped_std.size();i++){
            Spedizione std = db.sped_std.get(i);
            if(std.getCodice().equals(codice))
                return std.isStatoFinale();
        }

        for(int i=0;i<db.sped_assi.size();i++){
            SpedizioneAssicurata assi = db.sped_assi.get(i);
            if(assi.getCodice().equals(codice))
                return assi.isStatoFinale();
        }
        return false;
    }
    
    public void eliminaSpedizione(String codice){
        for(int i=0;i<db.sped_std.size();i++){
            Spedizione std = db.sped_std.get(i);
            if(std.getCodice().equals(codice)) 
                db.sped_std.remove(std);
        }
        for(int i=0;i<db.sped_assi.size();i++){
            SpedizioneAssicurata assi = db.sped_assi.get(i);
            if(assi.getCodice().equals(codice))
                db.sped_assi.remove(assi);
        }
    }
    
    // Funzioni per il thread
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
        
        for(int i=0;i<db.sped_std.size();i++){
            Spedizione std = db.sped_std.get(i);
            if(codice.equals(std.getCodice()) && 
                    logicheAggiornamentoStatoThread(std.getStatoConsegna(),nuovo_stato)){
                ok = std.setStatoConsegna(nuovo_stato);
                db.sped_std.replace(i, std);
                return ok;
            }
        }

        for(int i=0;i<db.sped_assi.size();i++){
            SpedizioneAssicurata assi = db.sped_assi.get(i);
            if(codice.equals(assi.getCodice()) && 
                    logicheAggiornamentoStatoThread(assi.getStatoConsegna(),nuovo_stato)){
                ok = assi.setStatoConsegna(nuovo_stato);
                db.sped_assi.replace(i, assi);
                return ok;
            }
        }
        return ok;
    }
}
