/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

/**
 *
 * @author giokk
 */
public class SpedizioneAssicurata extends Spedizione{
    public double valore_assicurato;
    
    public SpedizioneAssicurata(){}
    
    public SpedizioneAssicurata(String cod, String dest, double peso, double valore_assicurato){
        super(cod,dest,peso);
        setValoreAssicurato(valore_assicurato);
    }

    public double getValoreAssicurato() {
        return valore_assicurato;
    }

    private void setValoreAssicurato(double ammontare) {
        valore_assicurato = ammontare;
    }
    
    @Override
    public boolean isStatoFinale(){
        return (stato_consegna==Stato.RIMBORSO_EROGATO || stato_consegna==Stato.RICEVUTO);
    }
    
    @Override
    public boolean setStatoConsegna(Stato stato) {
        stato_consegna = stato;
        return true;
    }
    
    @Override
    public String toString(){
        return "Codice: "+super.getCodice()+" Destinazione: "+super.getDestinazione()+
                " Peso: "+super.getPeso()+" Data: "+super.getData()+" Val.: "+valore_assicurato;
    }
    
    @Override
    public String toLabelString() {
        return getCodice()+" - "+getDestinazione()+" - "+getPeso()+" - "+valore_assicurato+" - "+getData();
    }
}
