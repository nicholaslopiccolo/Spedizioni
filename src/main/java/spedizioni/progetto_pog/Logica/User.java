/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import java.io.Serializable;

/**
 *
 * @author giokk
 * @version 1.0.0
 * 
 */
public class User implements Serializable{
    private String username;
    private String password;
    private String indirizzo;

    private int nroSpedizioni;
    
    public User(String username,String password, String indirizzo){
        nroSpedizioni = 0;
        setUsername(username);
        setPassword(password);
        setIndirizzo(indirizzo);
    }
    
    public void nuovaSpedizione(){nroSpedizioni++;}
    // SETTERS
    private void setPassword(String password) {this.password = password;}
    private void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo;}
    private void setUsername(String username) {this.username = username;}

    // GETTERS
    public String getUsername(){return this.username;}
    public String getPassword() {return password;}
    public String getIndirizzo() {return indirizzo;}
    public int getNroSpedizioni() {return nroSpedizioni;}
    
    //utils
    @Override
    public String toString(){
        return "Nome:"+username+" Password:"+password+" Indirizzo:"+indirizzo;
    }
}
