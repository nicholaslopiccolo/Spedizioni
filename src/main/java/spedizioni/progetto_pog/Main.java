package spedizioni.progetto_pog;

import javax.swing.SwingUtilities;
import spedizioni.progetto_pog.Grafica.AppFrame;
import spedizioni.progetto_pog.Logica.Core;
/**
* Main è la prima funzione che viene chiamata dal programma.
* Il main istanzia il core, che gestirà tutte le logiche dell'applicazione,
* e avvia un thread SwingUtilities.invokeLater
* che farà partire l'interfaccia grafica creando
* un oggetto AppFrame.
* @author nicholaslopiccolo
*/
public class Main {
    /**
     * 
     * @param args Argomenti del main (inutilizzati)
     */
    public static void main(String[] args){
        // Il Core Avvia il thread con il 50% di possibilità di fallimento 
        // e lo spin ogni 20 secondi
        Core core = new Core(50,20);
        
        SwingUtilities.invokeLater(() -> {
            new AppFrame(core,"Spedisci Online s.r.l.").setVisible(true);
        });
    }
}
