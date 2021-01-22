/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Grafica;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import spedizioni.progetto_pog.Grafica.Panel.MainPanel;
import spedizioni.progetto_pog.Grafica.Panel.StartPanel;
import spedizioni.progetto_pog.Logica.Core;

/**
 * <strong>AppFrame</strong> è la classe madre della UI di questo progetto, da qui verrà
 * avviata e riavviata l'interfaccia grafica.
 * @author nicholaslopiccolo
 */
public class AppFrame extends JFrame {
    /**
     * Variabile memorizza il core dell'applicativo
     */
    private Core core;
    /**
     * Variabile che memorizza il pannello di start
     */
    private StartPanel startPanel;
    /**
     * Variabile che memoricca il main panel
     */
    private MainPanel mainPanel;
    
    /**
     * Il costruttore di questa classe assegna titolo e core per utilizzo 
     * interno, aggiunge un windowlistener che ascolterà l'evento di chiusura 
     * e avvia l'applicazione.
     * @param core  Gestisce la logica dell'applicazione
     * @param titolo Titolo dell'applicazione
     */
    public AppFrame(Core core, String titolo){
        super(titolo);
        this.core = core;
        /*
        *   Sull'evento di chiusura della finestra salva i dati su file 
        *   sfruttando la funzione write dell'oggetto core
        */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                core.write();
                
                dispose();
                System.exit(0);
            }
	});
        
        avviaApp();
    }
    /**
     * Effettua dei setup grafici quali: grandezza, resize disattivato, 
     * crea lo start panel necessario per il login, dopo lo aggiunge al frame.
     */
    public void avviaApp(){
        setBounds(500,200,500,400);
        setResizable(false);
        
        startPanel = new StartPanel(this,core);
        
        add(startPanel);
        
        if(mainPanel instanceof MainPanel)
            remove(mainPanel);
        invalidate();
        validate();
    }
    /**
     * Funzione chiamata dopo la fase di start o login dell'applicazione.
     * Rimuove lo startPanel e attacca il mainPanel per permettere all'utente
     * loggato di utilizzare tutte le funzionalità dell'applicazione
     */
    public void runApp(){
        remove(startPanel);
        mainPanel = new MainPanel(this,core);
        
        add(mainPanel);
        
        invalidate();
        validate();
        repaint();
    }
}
