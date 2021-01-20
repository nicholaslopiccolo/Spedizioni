/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Logica;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giokk
 */
public class ThreadStato extends Thread{
    private Core core;
    private ArrayList<Stato> stati;
    
    private int seconds;
    private int prob_fallimento;
    private int numero_loop = 0;
    
    public ThreadStato(Core core, int prob,int seconds){
        this.core = core;
        this.seconds = seconds;
        this.prob_fallimento = prob;
        
        stati = new ArrayList<Stato>();
        stati.add(Stato.PREPARAZIONE);
        stati.add(Stato.TRANSITO);
        stati.add(Stato.RICEVUTO);
        stati.add(Stato.RIMBORSO_EROGATO);
    }
    
    @Override
    public void run() {
        
        
        while(true){
            spin();
            
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadStato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void spin() {
        int LIMITE = 100;
        int nro_loop = 0;
        
        ArrayList<Spedizione> lista_sped_std = core.getSpedizioni().getLista();
        ArrayList<SpedizioneAssicurata> lista_sped_assi = core.getSpedizioniAssicurate().getLista();

        if(lista_sped_std.isEmpty() && lista_sped_assi.isEmpty()){
            System.out.println("Nuovo Loop: " + (++numero_loop)+" Non ho niente da aggiornare");
            return;
        }
        
        int indice_successo = 1 + new Random().nextInt(100);
        System.out.println("Nuovo Loop: " + (++numero_loop)+" Indice Succeso: "+indice_successo);
        
        boolean cambio_avvenuto = false;
        while(!cambio_avvenuto){
            // Tutte le spedizioni in stato finale
            if(nro_loop++ >= LIMITE) return;
            
            int std_lenght = lista_sped_std.size();
            int assi_lenght = lista_sped_assi.size();

            int indice_spedizione = 1 + new Random().nextInt(std_lenght+assi_lenght);

            if(indice_spedizione<=std_lenght){
                Spedizione std = lista_sped_std.get(indice_spedizione-1);
                int indice_stato = stati.indexOf(std.getStatoConsegna());
                if(!std.isStatoFinale() && indice_stato>=0 && indice_stato<stati.size()){
                    if(indice_successo > prob_fallimento)
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(std.getCodice(), stati.get(indice_stato+1));
                    else 
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(std.getCodice(), Stato.FALLITO);
                }
            }else{
                SpedizioneAssicurata assi = lista_sped_assi.get(indice_spedizione-1-std_lenght);
                Stato stato = assi.getStatoConsegna();
                int indice_stato = stati.indexOf(stato);
                if(!assi.isStatoFinale() && indice_stato>=0 && indice_stato<stati.size()-1){
                    if(indice_successo > prob_fallimento)
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(assi.getCodice(), stati.get(indice_stato+1));
                    else 
                        cambio_avvenuto = core.aggiornaStatoSpedizioneThread(assi.getCodice(), Stato.FALLITO);
                }else if(stato == Stato.RIMBORSO_RICHIESTO)// Questo caso non fallisce mai
                    cambio_avvenuto = core.aggiornaStatoSpedizioneThread(assi.getCodice(), Stato.RIMBORSO_EROGATO);
                    
            }
        }
    }
}
