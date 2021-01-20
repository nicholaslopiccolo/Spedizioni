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
 *
 * @author giokk
 */
public class AppFrame extends JFrame {
    private Core core;
    
    private StartPanel startPanel;
    private MainPanel mainPanel;
    

    public AppFrame(Core core, String titolo){
        super(titolo);
        this.core = core;
        /*
        *
        *   Sull'evento di chiusura della finestra salva i dati su file
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
        //pack();
    }
    
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
    
    public void runApp(){
        remove(startPanel);
        mainPanel = new MainPanel(this,core);
        
        add(mainPanel);
        
        invalidate();
        validate();
        repaint();
        
        //frame.remove(this);
    }
}
