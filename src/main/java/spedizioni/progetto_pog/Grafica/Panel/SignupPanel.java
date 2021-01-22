package spedizioni.progetto_pog.Grafica.Panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import spedizioni.progetto_pog.Logica.Core;

/**
 * <strong>SignupPanel</strong> è il pannello che sarà agganciato al SignDialog
 * e permetterà allo user di eseguire una registrazione.
 * @author nicholaslopiccolo
 */
public class SignupPanel extends JPanel{
    /**
     * Variabile memorizza il core dell'applicativo
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
     * Il costruttore esegue un setup grafico aggiungendo le labels e le textfields
     * per l'inserimento dei dati di username, password e via.
     * In oltre vi è un bottone per l'invio dei dati e una label contenente testo in caso di errore.
     * @param core Gestisce la logica dell'applicazione
     * @param dialog Tiene in memoria il Jdialog a cui si aggancia il pannello
     * @param username Username attuale
     */
    public SignupPanel(Core core,JDialog dialog,String username){
        super();
        this.core = core;
        setLayout(new GridLayout(8,1,10,10));
        
        JLabel usrLabel = new JLabel("Username");
        JLabel pwdLabel = new JLabel("Password");
        JLabel addrLabel = new JLabel("Indirizzo");
        JLabel errorLabel = new JLabel("Errore");

        JTextField usrText = new JTextField(username);
        
        JPasswordField pwdText = new JPasswordField(10);
        pwdText.setName("Password");
        
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
        //JTextField addrText = new JTextField();
        
        JButton sendButton = new JButton("Invia");
        
        sendButton.addActionListener((var arg) -> {
            //Login avvenuto con successo
            String usr_text = usrText.getText();
            String addr_text = getIndirizzo();
            String pwd_text = new String(pwdText.getPassword());
            
            System.out.println("SignUp: "+usr_text+"-"+pwd_text);
            
            if(checkIndirizzo() && this.core.register(usr_text,pwd_text,addr_text))
                dialog.dispose();
            else
                errorLabel.setVisible(true); 
        });
        
        Font f = errorLabel.getFont();
        errorLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        errorLabel.setForeground(Color.RED);
        errorLabel.setText("Username in uso o indirizzo errato");
        errorLabel.setVisible(false);
        
        add(usrLabel);
        add(usrText);
        add(pwdLabel);
        add(pwdText);
        add(addrLabel);
        add(toolbar);
        add(sendButton);
        add(errorLabel);
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
     * La funzione ritorna l'indirizzo della spedizione che l'utente sta creando.
     * @return String Ritorna la stringa contenente l'intero indirizzo
     * @see String
     */
    String getIndirizzo(){
    return denominazione.getSelectedItem() +" "+ destinazione_text.getText() 
            +" "+ civico_text.getText();
    }
}
