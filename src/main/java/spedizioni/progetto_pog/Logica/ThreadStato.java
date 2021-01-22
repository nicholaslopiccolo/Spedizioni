package spedizioni.progetto_pog.Logica;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <strong>ThreadStato</strong> estende thread e classe permette di creare
 * un thread che va a modificare lo stato di una spedizione ogni x secondi.
 * @author nicholaslopiccolo
 */
public class ThreadStato extends Thread{
    private Core core;
    private ArrayList<Stato> stati;
    
    private int secondi;
    private int prob_fallimento;
    private int numero_loop = 0;
    
    /**
     * Il costruttore setta i parametri iniziali e crea un'arraylist contenente
     * i possibili stati successivi.
     * @param core Gestisce la logica dell'applicazione
     * @param prob Probabilità di fallimento
     * @param secondi Secondi per ogni spin
     */
    public ThreadStato(Core core, int prob,int secondi){
        this.core = core;
        this.secondi = secondi;
        this.prob_fallimento = prob;
        
        stati = new ArrayList<Stato>();
        stati.add(Stato.PREPARAZIONE);
        stati.add(Stato.TRANSITO);
        stati.add(Stato.RICEVUTO);
        stati.add(Stato.RIMBORSO_EROGATO);
    }
    /**
     * La funzione così modificata esegue la funzione spin() ogni x secondi 
     * definiti dalla variabile interna "secondi".
     */
    @Override
    public void run() {
        while(true){
            spin();
            
            try {
                Thread.sleep(secondi * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadStato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * spin rappresenta una rotazione del thread.
     * In questo caso ogni rotazione cerca di cambiare stato ad una spedizione,
     * con un massimo di 100 tentativi, per farlo crea un numero randomico 
     * indice_successo che deve essere superiore alla probabilirà di fallimento
     * altrimenti la spedizione fallisce.
     * In seguito prende la lista delle spedizioni dall'oggetto core e genera un indice casuale
     * a quel punto cerca di passare allo stato di spedizione successivo.
     * 
     */
    private void spin(){
        int LIMITE = 100;
        int nro_loop = 0;
        int indice_successo;
        
        ArrayList spedizioni = core.getSpedizioniThread();
        
        if(spedizioni.isEmpty()){
            System.out.println("Nuovo Loop: " + (++numero_loop)+" Non ho niente da aggiornare");
            return;
        }else {
            indice_successo = 1 + new Random().nextInt(100);
            System.out.println("Nuovo Loop: " + (++numero_loop)+" Indice Succeso: "+indice_successo);
        }
        
        boolean cambio_avvenuto = false;
        while(!cambio_avvenuto){
            // Tutte le spedizioni in stato finale
            if(nro_loop++ >= LIMITE) return;

            int i = new Random().nextInt(spedizioni.size());
            Object sped_o = spedizioni.get(i);
            
            if(sped_o instanceof SpedizioneAssicurata){
                SpedizioneAssicurata assi = (SpedizioneAssicurata)sped_o;
                int indice_stato = stati.indexOf(assi.getStatoConsegna());
                if(!assi.isStatoFinale() && indice_stato>=0 && indice_stato<stati.size()){
                    if(indice_successo > prob_fallimento)
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(assi.getCodice(), stati.get(indice_stato+1));
                    else 
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(assi.getCodice(), Stato.FALLITO);
                }
            }else{
                Spedizione std = (Spedizione)sped_o;
                Stato stato = std.getStatoConsegna();
                int indice_stato = stati.indexOf(stato);
                if(!std.isStatoFinale() && indice_stato>=0 && indice_stato<stati.size()-1){
                    if(indice_successo > prob_fallimento)
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(std.getCodice(), stati.get(indice_stato+1));
                    else 
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(std.getCodice(), Stato.FALLITO);
                }else if(stato == Stato.RIMBORSO_RICHIESTO)// Questo caso non fallisce mai
                    cambio_avvenuto = core.aggiornaStatoSpedizioneThread(std.getCodice(), Stato.RIMBORSO_EROGATO);
            }
        }
    }
}
