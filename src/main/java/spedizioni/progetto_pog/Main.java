/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog;

import javax.swing.SwingUtilities;
import spedizioni.progetto_pog.Grafica.AppFrame;
import spedizioni.progetto_pog.Logica.Core;
/**
 *
 * @author giokk
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // il core Avvia il thread con il 50% di possibilità di fallimento 
        // e lo spin ogni 20 secondi
        Core core = new Core(50,20);
        
        SwingUtilities.invokeLater(() -> {
            new AppFrame(core,"Spedisci Online s.r.l.").setVisible(true);
        });
    }
}
