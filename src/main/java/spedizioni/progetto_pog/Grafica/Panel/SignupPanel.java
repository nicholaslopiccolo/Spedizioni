package spedizioni.progetto_pog.Grafica.Panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
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
        
        JTextField addrText = new JTextField();

        
        JButton sendButton = new JButton("Invia");
        
        sendButton.addActionListener((var arg) -> {
            //Login avvenuto con successo
            String usr_text = usrText.getText();
            String addr_text = addrText.getText();
            String pwd_text = new String(pwdText.getPassword());
            
            System.out.println("SignUp: "+usr_text+"-"+pwd_text);
            
            if(this.core.register(usr_text,pwd_text,addr_text))
                dialog.dispose();
            else
                errorLabel.setVisible(true);
                
        });
        
        Font f = errorLabel.getFont();
        errorLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
        errorLabel.setForeground(Color.RED);
        errorLabel.setText("Username o password errati");
        errorLabel.setVisible(false);
        
        add(usrLabel);
        add(usrText);
        add(pwdLabel);
        add(pwdText);
        add(addrLabel);
        add(addrText);
        add(sendButton);
        add(errorLabel);
    }
}
