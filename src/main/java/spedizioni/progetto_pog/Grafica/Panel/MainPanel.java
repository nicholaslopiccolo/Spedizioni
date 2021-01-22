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
 * <strong>MainPanel</strong> permette di creare il pannello principale per l'utilizzo
 * dell'aplicazione.
 * 
 * @author nicholaslopiccolo
 */
public class MainPanel extends JPanel{
    /**
     * Variabile che memorizza il frame a cui è agganciato il pannello.
     */
    private AppFrame frame;
    /**
     * Variabile che memorizza il core dell'applicazione.
     */
    private Core core;
    /**
     * Variabile che memorizza il pannello tabulare
     */
    private JTabbedPane tabbedPanel;
   
    /**
     * 
     * Il costruttore aggancia il frame e il core dell'applicazione allo stato del pannello
     * dopo chiama la funzione init che esegue il setup grafico.
     * @param frame Frame antenato del pannello
     * @param core Gestisce la logica dell'applicazione
     */
    
    public MainPanel(AppFrame frame,Core core){
        super();
        
        this.core = core;
        this.frame = frame;
        
        init();
    }
    /**
     * Crea un JTabbedPanel e vi aggancia i unovi pannelli nel caso dell'admin 
     * il solo pannello @see ListaSpedizioniPanel , per il cliente @see ListaSpedizioniPanel e 
     * @see FormInserimentoPanel.
     * In seguito aggiunge il tabbed panel a se stesso ed un bottone per egeguire
     * il logout.
     */
    private void init(){
        setLayout(new BorderLayout());
        
        tabbedPanel = new JTabbedPane();
        
        ListaSpedizioniPanel ltp = new ListaSpedizioniPanel(frame,core);
        FormInserimentoPanel fip = new FormInserimentoPanel(frame,core);        
        if(core.isAdmin()){
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
}
