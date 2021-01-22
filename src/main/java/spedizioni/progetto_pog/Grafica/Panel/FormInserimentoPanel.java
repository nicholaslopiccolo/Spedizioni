/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author giokk
 */
public class FormInserimentoPanel extends JPanel implements ActionListener{
    private AppFrame frame;
    private Core core;
    private ListaSpedizioniPanel lista;
    
    //String Indirizzo di destinazione
    private JComboBox denominazione;
    private JTextField destinazione_text;
    private JTextField civico_text;
    
    private JLabel val_label;
    private JSpinner peso_number;
    private JSpinner val_number;
    private JCheckBox assi_checkbox;
    
    private JButton invia;
    private String invia_str = "Aggiungi spedizione";
    
    public FormInserimentoPanel(AppFrame frame, Core core,ListaSpedizioniPanel lista){
        super();
        this.frame = frame;
        this.core = core;
        this.lista = lista;
        
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
         public void keyPressed(KeyEvent ke) {
            String value = civico_text.getText();
            int l = value.length();
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

        invia = new JButton(invia_str);
        
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
    
    String getIndirizzo(){
    return denominazione.getSelectedItem() +" "+ destinazione_text.getText() 
            +" "+ civico_text.getText();
    }
    private boolean checkIndirizzo() {
        String dest = destinazione_text.getText();
        String civ = civico_text.getText();
        
        if(!(dest.isEmpty() || dest.isBlank()) && !(civ.isEmpty() || civ.isBlank()))
            return true;
        return false;
    }
    boolean checkSpedizione(double peso, double val){
        if((val > 0 || !assi_checkbox.isSelected()) && peso >0)
            return true;
        return false;
    }
    // Da terminare
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
