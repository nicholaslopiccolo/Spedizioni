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
 * <strong>SigninPanel</strong> è il pannello che sarà agganciato al SignDialog
 * e permetterà allo user di eseguire il login.
 * @author nicholaslopiccolo
 */
public class SigninPanel extends JPanel{
    /**
     * Variabile memorizza il core dell'applicativo
     */
    private Core core;
    
    /**
     * Il costruttore esegue un setup grafico aggiungendo le labels e le textfields
     * per l'inserimento dei dati di username e password, in oltre vi è un bottone
     * e una label contenente testo in caso di errore.
     * @param core Gestisce la logica dell'applicazione
     * @param dialog Tiene in memoria il Jdialog a cui si aggancia il pannello
     * @param username Username attuale
     * @param admin Caso in cui l'utente esegue il login come admin
     */
    public SigninPanel(Core core,JDialog dialog,String username,boolean admin){
        super();
        this.core = core;
        //this.setBounds(200, 200,200,200);
        setLayout(new GridLayout(6,1,10,10));
        if(admin) dialog.setTitle("Login Admin");
        
        JLabel usrLabel = new JLabel("Username");
        JLabel pwdLabel = new JLabel("Password");
        JLabel errorLabel = new JLabel("Errore");

        
        JTextField usrText = new JTextField(username);
        usrText.setEditable(!admin);
        
        JPasswordField pwdText = new JPasswordField(10);
        pwdText.setName("Password");
        
        JButton sendButton = new JButton("Invia");
        
        sendButton.addActionListener((var arg) -> {
            //Login avvenuto con successo
            String usr_text = usrText.getText();
            String pwd_text = new String(pwdText.getPassword());
            
            System.out.println("SignIn: "+usr_text+"-"+pwd_text);
            
            if(usr_text.isEmpty() || pwd_text.isEmpty())return;
            
            if(this.core.login(usr_text, pwd_text)){
                dialog.dispose();
                return;
            }
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
        add(sendButton);
        add(errorLabel);
    }
}
