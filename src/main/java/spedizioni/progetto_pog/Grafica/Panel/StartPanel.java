/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Grafica.Panel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import spedizioni.progetto_pog.Grafica.AppFrame;
import spedizioni.progetto_pog.Grafica.SignDialog;
import spedizioni.progetto_pog.Logica.Core;

/**
 *
 * @author giokk
 */
public class StartPanel extends JPanel implements ActionListener{
    private Core core;
    private AppFrame frame;
    private SignDialog signDialog;
    
    private JCheckBox admin = new JCheckBox("Admin");
    private JButton login = new JButton("Login");
    private JButton registrati = new JButton("Registrati");
    
    public StartPanel(AppFrame frame,Core core){
        super();
        
        this.core = core;
        this.frame = frame;
        
        init();
    }
    
    private void init(){
        setLayout(new GridLayout(2,1,10,10));
        
        admin.setAlignmentX(CENTER_ALIGNMENT);
        
        login.addActionListener(this);
        registrati.addActionListener(this);
        admin.addActionListener(this);
        
        add(login);
        add(registrati);
        add(admin);
    }
    
    private void newSignDialog(String titolo,boolean login, boolean admin){
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                signDialog = new SignDialog(core,frame,titolo,login,admin);
                signDialog.addWindowListener(new WindowAdapter() {
                     @Override
                     public void windowClosed(WindowEvent e) {
                         //CHANGE PANEL
                         System.out.println("User corrente: "+core.getCurrentUser().toString());
                         frame.runApp();
                     }
                 });
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String component = e.getActionCommand();
        
        switch(component){
            case "Admin":{
                this.registrati.setEnabled(!this.registrati.isEnabled());
                break;
            }
            case "Login": {
                newSignDialog("Login",true,this.admin.isSelected());
                break;
            }
            case "Registrati":{
                newSignDialog("Nuovo Cliente",false,false);
                break;
            }
        }
    }
}

