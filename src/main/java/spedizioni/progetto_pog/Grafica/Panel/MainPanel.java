/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spedizioni.progetto_pog.Grafica.Panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import spedizioni.progetto_pog.Grafica.AppFrame;
import spedizioni.progetto_pog.Logica.Core;

/**
 *
 * @author giokk
 */
public class MainPanel extends JPanel{
    private Core core;
    private AppFrame frame;
    private ListaSpedizioniPanel ltp;
    
    private JTabbedPane tabbedPanel;
    //private SignDialog signDialog;
    
    public MainPanel(AppFrame frame,Core core){
        super();
        
        this.core = core;
        this.frame = frame;
        
        init();
    }
    
    private void init(){
        boolean admin = core.isAdmin();
        setLayout(new BorderLayout());
        
        tabbedPanel = new JTabbedPane();
        
        ltp = new ListaSpedizioniPanel(frame,core);
        FormInserimentoPanel fip = new FormInserimentoPanel(frame,core,ltp);        
        if(admin){
            tabbedPanel.addTab("Spedizioni", null,ltp,"Gestisci le spedizioni");
        } else {
            tabbedPanel.addTab("+", null,fip,"Crea una nuova spedizione");
            tabbedPanel.addTab("Spedizioni", null,ltp,"Guarda le tue spedizioni");
        }
        
        tabbedPanel.setPreferredSize(new Dimension(getSize().width,300));
        
        JButton logout = new JButton("Logout: "+core.getCurrentUser().getUsername());
        logout.setPreferredSize(new Dimension(getSize().width,50));
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ltp.stop();
                core.logout();
                frame.avviaApp();
            }
        });
        
        add(tabbedPanel,BorderLayout.CENTER);
        add(logout,BorderLayout.PAGE_END);
        
    }

    void refreshListaSpedizioni() {
        remove(tabbedPanel);
        init();
        revalidate();
    }
}
