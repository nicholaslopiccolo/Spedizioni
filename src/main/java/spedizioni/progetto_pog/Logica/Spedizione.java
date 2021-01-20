/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author giokk
 */

public class Spedizione implements Serializable{
    private String codice;
    protected Stato stato_consegna;
    private String destinazione;
    private double peso;
    private String data;
    
    public Spedizione(){}

    public Spedizione(String cod, String dest, double peso){
        data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        stato_consegna = Stato.PREPARAZIONE;
        setCodice(cod);
        setDestinazione(dest);
        setPeso(peso);
    }
    //GETTERS

    public String getCodice() {
        return codice;
    }

    public Stato getStatoConsegna() {
        return stato_consegna;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public double getPeso() {
        return peso;
    }

    public String getData() {
        return data;
    }
    
    //SETTERS
    public void setCodice(String cod) {
        codice = cod;
    }

    public boolean setStatoConsegna(Stato statoConsegna) {
        if(statoConsegna == Stato.RIMBORSO_EROGATO 
                || statoConsegna == Stato.RIMBORSO_RICHIESTO){
            System.out.println("Impossibile cambiare stato");
            return false;
        }
        stato_consegna = statoConsegna;
        return true;
    }

    public void setDestinazione(String dest) {
        destinazione = dest;
    }

    public void setPeso(double p) {
        peso = p;
    }
    
    public boolean isStatoFinale(){
        return (stato_consegna==Stato.RICEVUTO);
    }
    @Override
    public String toString(){
        return "Codice: "+codice+" Destinazione: "+destinazione+" Peso: "+peso+" Data: "+data;
    }

    public String toLabelString() {
        return codice+" - "+destinazione+" - "+peso+" - "+data;
    }
    
}
