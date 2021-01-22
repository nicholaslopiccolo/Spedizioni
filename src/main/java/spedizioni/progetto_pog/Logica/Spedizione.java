package spedizioni.progetto_pog.Logica;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <strong>Spedizione</strong> permette di creare l'oggetto spedizione.
 * @author nicholaslopiccolo
 */

public class Spedizione implements Serializable{
    /**
     * Stringa Univoca per ogni spedizione
     */
    private String codice;
    /**
     * Stato di consegna definito come protected se no non potrebbe essere 
     * modificato dalla classe figlia contenuta nello stesso package.
     */
    protected Stato stato_consegna;
    /**
     * Indirizzo di destinazione della spedizione
     */
    private String destinazione;
    /**
     * Peso della spedizione
     */
    private double peso;
    /**
     * Data di creazione della spedizione
     */
    private String data;
    /**
     * Costruttore vuoto per i file di test.
     */
    public Spedizione(){}
    /**
     * Costruttore che permette il settaggio dei parametri iniziali.
     * @param cod codice della spedizione
     * @param dest indirizzo della spedizione
     * @param peso peso della spedizione
     */
    public Spedizione(String cod, String dest, double peso){
        data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        stato_consegna = Stato.PREPARAZIONE;
        setCodice(cod);
        setDestinazione(dest);
        setPeso(peso);
    }
    /**
     * Restituisce il codice della spedizione.
     * @return String
     * @see String
     */
    public String getCodice() {
        return codice;
    }
    /**
     * Restituisce lo stato di consegna.
     * @return Stato
     * @see Stato
     */
    public Stato getStatoConsegna() {
        return stato_consegna;
    }
    /**
     * Restituisce l'indirizzo di destinazione.
     * @return String
     * @see String
     */
    public String getDestinazione() {
        return destinazione;
    }
    /**
     * Restituisce il peso della spedizione.
     * @return double
     * @see double
     */
    public double getPeso() {
        return peso;
    }
    /**
     * Restituisce la stringa della data
     * @return String
     * @see String
     */
    public String getData() {
        return data;
    }
    
    /**
     * Setta il codice della spedizione.
     * @param cod Nuovo codice della spedizione
     */
    public void setCodice(String cod) {
        codice = cod;
    }
    /**
     * Setta lo stato di consegna, esegue un controllo per evitare che venga spostata
     * in stato di rimborso erogato o rimborso richiesto.
     * @param statoConsegna Nuovo stato di consegna della spedizione
     * @return boolean
     * @see boolean
     */
    public boolean setStatoConsegna(Stato statoConsegna) {
        if(statoConsegna == Stato.RIMBORSO_EROGATO 
                || statoConsegna == Stato.RIMBORSO_RICHIESTO){
            System.out.println("Impossibile cambiare stato");
            return false;
        }
        stato_consegna = statoConsegna;
        return true;
    }
    /**
     * Setta la destinazione della spedizione.
     * @param dest Stringa ocntenente il nuovo indirizzo della spedizione
     */
    public void setDestinazione(String dest) {
        destinazione = dest;
    }
    /**
     * Setta il peso della spedizione.
     * @param p Nuovo peso della spedizione
     */
    public void setPeso(double p) {
        peso = p;
    }
    /**
     * Restituisce true se la spedizione si trova in uno stato finale.
     * @return boolean
     * @see boolean
     */
    public boolean isStatoFinale(){
        return (stato_consegna==Stato.RICEVUTO);
    }
    /**
     * La funzione così modificata ritorna la stringa della Spedizione formata 
     * dai dati all'interno del suo stato.
     * @return String
     * @see String
     */
    @Override
    public String toString(){
        return "Codice: "+codice+" Destinazione: "+destinazione+" Peso: "+peso+" Data: "+data;
    }
    
}
