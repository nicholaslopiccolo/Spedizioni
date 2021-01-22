/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Grafica;

import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import spedizioni.progetto_pog.Grafica.Panel.SigninPanel;
import spedizioni.progetto_pog.Grafica.Panel.SignupPanel;
import spedizioni.progetto_pog.Logica.Core;

/**
 *  <strong>SignDialog</strong> permetterà di eseguire il login nell'applicazione
 * @author nicholaslopiccolo
 */
public class SignDialog extends JDialog {
    /**
     * Variabile che memorizza il core dell'applicazione
     */
    private Core core;
    
    /**
     * Il costruttore utilizzerà diversi parametri che verrannò utilizzati 
     * per distinguere il volere dell'utente (es. login/registrazione admin/cliente).
     * Setup grafici della finestra di dialogo.
     * 
     * @param core Gestisce la logica dell'applicazione
     * @param frame Frame antenato del Jdialog
     * @param titolo Titolo della finestra di dialogo
     * @param login In caso di login prende true per la registrazione false
     * @param admin Caso in cui l'utente esegue il login come admin
     */
    public SignDialog(Core core, AppFrame frame, String titolo,boolean login, boolean admin){
        super(frame,titolo,JDialog.ModalityType.APPLICATION_MODAL);
        this.core = core;
        
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        String username = (admin) ? "admin":"";
        
        JPanel signP = login ? 
                new SigninPanel(core,this,username,admin):
                new SignupPanel(core,this,username);
        
        signP.setBorder(BorderFactory.createEmptyBorder(10, 10,10,10));
        add(signP);
        pack();
        
        setResizable(false);
        setVisible(true);

    }
}
