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
 * <strong>StartPanel</strong> è il pannello porta al login e alla 
 * registrazione di un nuovo utente.
 * @author nicholaslopiccolo
 */
public class StartPanel extends JPanel implements ActionListener{
    /**
     * Variabile memorizza il core dell'applicativo
     */
    private Core core;
    /**
     * Variabile memorizza il frame a cui è agganciato il pannello
     */
    private AppFrame frame;
    /**
     * Variabile che memorizza in Sign Dialog
     */
    private SignDialog signDialog;
    /**
     * Variabile che memorizza il checkbox per il login di admin
     */
    private JCheckBox admin = new JCheckBox("Admin");
    /**
     * Bottone dell'evento di login
     */
    private JButton login = new JButton("Login");
    /**
     * Bottone dell'evento di registrazione
     */
    private JButton registrati = new JButton("Registrati");
    /**
     * Il costruttore aggancia il frame ed il core allo stato interno dell'oggetto.
     * Viene lanciata la funzione init che inizializza i componenti grafici.
     * @param core Gestisce la logica dell'applicazione
     * @param frame Frame antenato del pannello
     */
    public StartPanel(AppFrame frame,Core core){
        super();
        
        this.core = core;
        this.frame = frame;
        
        init();
    }
    /**
     * Setta il layout e aggiunge gli ActionListeners asi bottoni in fine li 
     * aggiunge al pannello
     */
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
    /**
     * Crea il SignDialog che permetterà di svolgere le operazioni di autenticazione,
     * per fare ciò sfrutta @see SwingUtilities.invokeLater e vi crea un thread
     * con lo scopo di mantenere la finestra attiva e agganciare l'evento di 
     * chiusura del JDialog.
     * @param titolo Titolo del nuovo dialog
     * @param login True in caso di login false in caso di registrazione
     * @param admin true se si tratta del login admin
     */
    private void newSignDialog(String titolo,boolean login, boolean admin){
        SwingUtilities.invokeLater(() -> {
            signDialog = new SignDialog(core,frame,titolo,login,admin);
            signDialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    //CHANGE PANEL
                    System.out.println("User corrente: "+core.getCurrentUser().toString());
                    frame.runApp();
                }
            });
        });
    }
    /**
     * La funzione così modificata ascolta e eventi distinti, admin che abilita 
     * o disabilita il tasto register, login che crea un signdialog in modalità
     * login e registrati che apre un signdialog in modalità registrazione.
     * @param e ActionEvent che fa eseguire l'evento
     * @see ActionEvent
     */
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

