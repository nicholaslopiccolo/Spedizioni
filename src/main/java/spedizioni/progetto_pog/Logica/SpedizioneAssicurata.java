package spedizioni.progetto_pog.Logica;

/**
 * <strong>SpedizioneAssicurata</strong> è la classe figlia di Spedizione, 
 * permette la creazione di spedizioni assicurate quindi di impostare un valore assicurato.
 * @author nicholaslopiccolo
 */
public class SpedizioneAssicurata extends Spedizione{
    /**
     * Valore assicurato della spedizione
     */
    private double valore_assicurato;
    
    /**
     * Costruttore vuoto per test
     */
    public SpedizioneAssicurata(){}
    
    /**
     * Costruttore che permette il settaggio dei parametri iniziali chiamando 
     * il cotruttore della classe padre.
     * @param cod codice della spedizione
     * @param dest indirizzo della spedizione
     * @param peso peso della spedizione
     * @param valore_assicurato valore assicurato della spedizione
     */
    public SpedizioneAssicurata(String cod, String dest, double peso, double valore_assicurato){
        super(cod,dest,peso);
        setValoreAssicurato(valore_assicurato);
    }
    /**
     * Restituisce il valore assicurato.
     * @return double
     * @see double
     */
    public double getValoreAssicurato() {
        return valore_assicurato;
    }
    /**
     * Setta il valore assicurato
     * @param ammontare Ammontare assicurato
     */
    private void setValoreAssicurato(double ammontare) {
        valore_assicurato = ammontare;
    }
    /**
     * La funzione così modificata ritorna true nel caso in cui la spedizione
     * assicurata abbia stato rimborso erogato o ricevuta.
     * @return boolean
     * @see boolean
     */
    @Override
    public boolean isStatoFinale(){
        return (stato_consegna==Stato.RIMBORSO_EROGATO || stato_consegna==Stato.RICEVUTO);
    }
    /**
     * La funzione così modificata setta il nuovo stato di consegna senza 
     * controlli.
     * @param stato Nuovo stato della spedizione
     * @return boolean
     * @see boolean
     */
    @Override
    public boolean setStatoConsegna(Stato stato) {
        stato_consegna = stato;
        return true;
    }
    /**
     * La funzione così modificata ritorna la stringa della SpedizioneAssicurata
     * formata dai dati all'interno del suo stato.
     * @return String
     * @see String
     */
    @Override
    public String toString(){
        return "Codice: "+super.getCodice()+" Destinazione: "+super.getDestinazione()+
                " Peso: "+super.getPeso()+" Data: "+super.getData()+" Val.: "+valore_assicurato;
    }
}
