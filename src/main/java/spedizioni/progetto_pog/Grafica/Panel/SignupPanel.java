/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author giokk
 */
public class SignupPanel extends JPanel{
    private Core core;
    public SignupPanel(Core core,JDialog dialog,String username){
        super();
        this.core = core;
        //this.setBounds(200, 200,200,200);
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
