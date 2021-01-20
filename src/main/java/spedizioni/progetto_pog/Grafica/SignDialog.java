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
 *
 * @author giokk
 */
public class SignDialog extends JDialog {
    private Core core;
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
