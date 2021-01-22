/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import java.io.Serializable;

/**
 * <strong>User</strong> è la classe per la creazione di utenti.
 * Implementa i metodi di Serializable cosi che possa essere serializzata 
 * all'interno di un file.
 * @author nicholaslopiccolo
 */
public class User implements Serializable{
    /**
     * Username dello user
     */
    private String username;
    /**
     * Password dello user
     */
    private String password;
    /**
     * Indirizzo dello user
     */
    private String indirizzo;
    /**
     * Numero di spedizioni create dallo user
     */
    private int nroSpedizioni;
    /**
     * Il costruttore setta il numero delle spedizioni a 0 e i parametri iniziali.
     * @param username Username dello user
     * @param password Password dello User
     * @param indirizzo Indirizzo dello User
     */
    public User(String username,String password, String indirizzo){
        nroSpedizioni = 0;
        setUsername(username);
        setPassword(password);
        setIndirizzo(indirizzo);
    }
    /**
     * Incrementa il numero delle spedizioni
     */
    public void nuovaSpedizione(){nroSpedizioni++;}
    // SETTERS
    /**
     * Setta la password dello user
     * @param password Stringa contenente la password
     */
    private void setPassword(String password) {this.password = password;}
    /**
     * Setta l'indirizzo dell'utente
     * @param indirizzo Stringa contenente l'indirizzo
     */
    private void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo;}
    /**
     * Setta lo username dell'utente
     * @param username Stringa contenente lo username dell'utente
     */
    private void setUsername(String username) {this.username = username;}

    // GETTERS
    /**
     * Ritorna lo username dell'utente
     * @return String
     * @see String
     */
    public String getUsername(){return this.username;}
    /**
     * Ritorna la password dello user
     * @return String
     * @see String
     */
    public String getPassword() {return password;}
    /**
     * Ritorna l'indirizzo dello user
     * @return String
     * @see String
     */
    public String getIndirizzo() {return indirizzo;}
    /**
     * Ritorna il numero delle spedizioni eseguite dall'utente
     * @return String
     * @see String
     */
    public int getNroSpedizioni() {return nroSpedizioni;}
    
    /**
     * La funzione così modificata ritorna la stringa dell'utente formata dai 
     * dati all'interno del suo stato.
     * @return String
     * @see String
     */
    @Override
    public String toString(){
        return "Nome:"+username+" Password:"+password+" Indirizzo:"+indirizzo;
    }
}
