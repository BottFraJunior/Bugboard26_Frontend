package view;

import javax.swing.*;
import java.awt.*;
import controller.loginController;

public class loginFrame extends defaultFrame {    //Mockup M1 Frame

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public loginFrame() {
        super("Benvenuto su BugBoard26");
        
        setSize(400, 400); 
        
        toLeftPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Inserisci le tue credenziali");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); 

        emailField = buildInputField("Email:", new JTextField());
        passwordField = buildInputField("Password:", new JPasswordField());

        loginButton = buildButton("Accedi", 150, 40); 
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setOpaque(false);
        buttonPanel.add(Box.createHorizontalGlue()); 
        buttonPanel.add(loginButton);               
        buttonPanel.add(Box.createHorizontalGlue());
        
        toLeftPanel.add(Box.createVerticalGlue());   
        toLeftPanel.add(buttonPanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 20)));


        loginButton.addActionListener(e -> {
            String insertedEmail = emailField.getText();
            String insertedPswrd = new String(passwordField.getPassword());                     
            
            if (insertedEmail.isEmpty() || insertedPswrd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Campo email o campo password non inseriti. È pregato di riprovare.",
                        "Login non riuscito", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            loginController controller = new loginController();
            String[] userData = controller.tryLogin(insertedEmail, insertedPswrd);
            
            if (userData != null) {     
                this.dispose(); 
                
                String ruolo = userData[0];
                String nome = userData[1];
                String email = userData[2];
                
               
                model.utenteModel user = new model.utenteModel();
                user.setRuolo(ruolo);
                user.setNome(nome);
                user.setEmail(email);
                
                model.sessionManager.getInstance().setCurrentUser(user);
                
                
                if (ruolo.equalsIgnoreCase("AMMINISTRAZIONE")) {
                    new adminDashboardFrame().setVisible(true); 
                } else {
                    new baseDashboardFrame().setVisible(true);  
                }
                
            } else {    
                JOptionPane.showMessageDialog(this, 
                    "Credenziali errate o utente inesistente.", "Login non riuscito", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "false");	//Notifiche NVIDIA
        SwingUtilities.invokeLater(() -> {
            new loginFrame().setVisible(true);
        });
    }

}