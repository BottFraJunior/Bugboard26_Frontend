package userInterface;

import javax.swing.*;
import java.awt.*;

import controller.loginController;

public class loginFrame extends JFrame {	//Mockup M1 Frame

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public loginFrame() {
    	
        setTitle("BugBoard26");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setResizable(false); 

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Benvenuto su BugBoard26");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Inserisci le tue credenziali");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        

        loginButton = new JButton("Accedi");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        
        loginButton.addActionListener(e -> {
            String insertedEmail = emailField.getText();
            String insertedPswrd = new String(passwordField.getPassword());
            
            if (insertedEmail.isEmpty() || insertedPswrd.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, 
                    "Campo email o campo password non completi. È pregato di riprovare.",
                    "Login incompleto", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            loginController loginController = new loginController();
            
            boolean resultLogin = loginController.tryLogin(insertedEmail, insertedPswrd);
            
            if (resultLogin) {
                JOptionPane.showMessageDialog(mainPanel, 
                    "Login completato con successo!", "Accesso Consentito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainPanel, 
                    "Credenziali non valide. È pregato di riprovare.", "Login non riuscito", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        

        
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(separator);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        mainPanel.add(formPanel); 
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(loginButton);
        
        add(mainPanel);
    }


    public static void main(String[] args) {
    	
    	System.setProperty("sun.java2d.d3d", "false");	//Notifiche NVIDIA

        SwingUtilities.invokeLater(() -> {
            loginFrame frame = new loginFrame();
            frame.setVisible(true); 
        });
    }
}