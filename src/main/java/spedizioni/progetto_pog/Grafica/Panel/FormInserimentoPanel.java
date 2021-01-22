package spedizioni.progetto_pog.Grafica.Panel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import spedizioni.progetto_pog.Grafica.AppFrame;
import spedizioni.progetto_pog.Logica.Core;


/**
 * <strong>FormInserimentoPanel</strong> pannello in cui l'utente creerà 
 * nuove spedizioni.
 * @author nicholaslopiccolo
 */
public class FormInserimentoPanel extends JPanel implements ActionListener{
    /**
     * Variabile che memorizza il frame a cui è agganciato il pannello.
     */
    private AppFrame frame;
    /**
     * Variabile che memorizza il core dell'applicazione.
     */
    private Core core;
    
    /**
     * Variabile che memorizza il combobox contenente i suffissi urbanistici.
     */
    private JComboBox denominazione;
    /**
     * Variabile che memorizza la textfield della stringa di destinazione della spedizione.
     */
    private JTextField destinazione_text;
    /**
     * Variabile che memorizza la textfield ocntenente il civico di spedizione.
     */
    private JTextField civico_text;
    /**
     * Variabile che memorizza la label del valore assicurato.
     */
    private JLabel val_label;
    /**
     * Variabile che memorizza il peso della nuova spedizione.
     */
    private JSpinner peso_number;
    /**
     * Variabile che memorizza il valore assicurato della nuova spedizione.
     */
    private JSpinner val_number;
    /**
     * Variabile che memorizza il la checkbox che stabilisce se la prossima 
     * spedizione sarà a assicurata o meno.
     */
    private JCheckBox assi_checkbox;
    /**
     * Variabile che memorizza il JButton per la creazoine della prossima spedizione
     */
    private JButton invia;
    
    /**
     * Il costruttore eseguirà un setup del pannello in modo tale da 
     * mostrare gli elementi grafici all'utente.
     * Le aggiunte grafiche sono: toolbar dell'indirizzo (contenente: suffisso 
     * urbano nome via e civico), il peso della spedizione, un eventuale valore
     * assicurato, le label e un checkbox per stabilire se la spedizione sia 
     * assicurata o meno.
     * @param frame Frame antenato del pannello
     * @param core Gestisce la logica dell'applicazione
     */
    public FormInserimentoPanel(AppFrame frame, Core core){
        super();
        this.frame = frame;
        this.core = core;
        
        setLayout(new GridLayout(6,1,10,10));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // Labels
        JLabel destinazione_label = new JLabel("Destinzione");
        JLabel peso_label = new JLabel("Peso (Kg)");
        val_label = new JLabel("Valore Assicurato");
        val_label.setEnabled(false);
        
        //Toolbar di destinazione (suffisso, nome via, civico)
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable( false);
        
        String[] suffissi = {"via","viale","strada","stradello","piazza","piazzetta","largo"};
        denominazione = new JComboBox(suffissi);
        
        destinazione_text = new JTextField();
        
        civico_text = new JTextField();
        civico_text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9')
                   civico_text.setEditable(true);
                else 
                    civico_text.setEditable(false);
                }
            });        
        
        toolbar.add(denominazione);
        toolbar.add(destinazione_text);
        toolbar.add(civico_text);
        
        // Spinner peso e valore assicurato
        SpinnerModel model_peso = new SpinnerNumberModel(0, 0, 100, 0.5);     
        peso_number = new JSpinner(model_peso);
        
        SpinnerModel model_val = new SpinnerNumberModel(0, 0, 10000, 5);     
        val_number = new JSpinner(model_val);
        val_number.setEnabled(false);

        
        assi_checkbox = new JCheckBox("Spedizione assicurata");

        invia = new JButton("Aggiungi Spedizione");
        
        assi_checkbox.addActionListener(this);
        invia.addActionListener(this);
    
        add(destinazione_label);
        add(toolbar);
        add(peso_label);
        add(peso_number);
        add(val_label);
        add(val_number);
        add(assi_checkbox);
        add(invia);
    }
    /**
     * Questa funzione gestirà gli eventi che avvengono sul pannello nello 
     * specifico la creazione di una nuova spedizione ed il checkbox per la 
     * creazione di una spedizione assicurata.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String component = e.getActionCommand();
        
        switch(component){
            case "Spedizione assicurata":{
                val_label.setEnabled(!this.val_label.isEnabled());
                val_number.setEnabled(!this.val_number.isEnabled());
                break;
            }
            case "Aggiungi spedizione":{
                aggiungiSpedizione();
                break;
            }

        }
    }
    /**
     * Aggiunge una spedizione eseguendo i controlli necessari in caso di 
     * successo dei controlli: esegue la funzione aggiungiSpedizione dell'oggetto
     * core e aggiunge un effetto al bottone in caso di successo o fallimento.
     */
    void aggiungiSpedizione(){
        double peso = Double.parseDouble(peso_number.getValue().toString());
        double val = (assi_checkbox.isSelected())?
                Double.parseDouble(val_number.getValue().toString()):
                -1;
        
        if(checkIndirizzo() && checkSpedizione(peso,val)){
            core.aggiungiSpedizione(getIndirizzo(), peso,val);
            effettoAggiungi(true);
            System.out.println("SUCCESSO");
        } else {
            effettoAggiungi(false);
            System.out.println("FALLITO");
        }
    }
    
    /**
     * La funzione ritorna l'indirizzo della spedizione che l'utente sta creando.
     * @return String Ritorna la stringa contenente l'intero indirizzo
     * @see String
     */
    String getIndirizzo(){
    return denominazione.getSelectedItem() +" "+ destinazione_text.getText() 
            +" "+ civico_text.getText();
    }
    /**
     * Esegue un controllo sull'indirizzo per evitare che gli elementi siano vuoti.
     * @return boolean ritorna true se il controllo non riporta errori
     * @see boolean
     */
    private boolean checkIndirizzo() {
        String dest = destinazione_text.getText();
        String civ = civico_text.getText();
        
        if(!(dest.isEmpty() || dest.isBlank()) && !(civ.isEmpty() || civ.isBlank()))
            return true;
        return false;
    }
    /**
     * Esegue un controllo sul peso e sul valore assicurato, ritorna unn booleano
     * in caso di successo o meno.
     * @param peso Peso della nuova spedizione
     * @param val Valore assicurato della nuova spedizione
     * @return boolean ritorna true se il controllo non riporta errori
     * @see boolean
     */
    private boolean checkSpedizione(double peso, double val){
        if((val > 0 || !assi_checkbox.isSelected()) && peso >0)
            return true;
        return false;
    }
    /**
     * Aggiungeu n effetto visivo che mostra all'utente il successo, o meno, della
     * aggiunta della spedizione.
     * @param success Stato di successo della aggiunta di spedizione
     */
    private void effettoAggiungi(boolean success){
        Color defB = invia.getBackground();
        Color defF = invia.getForeground();

        Color toSetB = (success) ? Color.GREEN:Color.RED;
        Color tosetF = (success) ? Color.BLACK:Color.WHITE;
        
        invia.setBackground(toSetB);
        invia.setForeground(tosetF);
        repaint();
        
        SwingUtilities.invokeLater(() -> {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){}

            invia.setBackground(defB);
            invia.setForeground(defF);

            repaint();
        });
    }
}
