package view;

import controller.makeAccountController;
import controller.passwordHashingUtils;
import model.utenteModel;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Enumeration;

public class makeAccountFrame extends defaultFrame {	//Mockup M5 Frame

    private JTextField nameField;
    private JTextField idField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private ButtonGroup rolesGroup; 
    private JButton backBtn;
    private JButton actionBtn;

    public makeAccountFrame(JFrame parentFrame) {
        super("Registra un nuovo utente");
        setSize(400, 480); 

        toLeftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inlinePanel = new JPanel();
        inlinePanel.setLayout(new BoxLayout(inlinePanel, BoxLayout.X_AXIS));
        inlinePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        
        JLabel nameLabel = new JLabel("Nome account:");
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 22));
        
        applyCharactersLimit(nameField, 30);


        JLabel idLabel = new JLabel("    ID#: ");
        
        idField = new JTextField("0001");
        idField.setForeground(Color.GRAY);
        idField.setMaximumSize(new Dimension(40, 22));
        
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (idField.getText().equals("0001")) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (idField.getText().isEmpty()) {
                    idField.setText("0001");
                    idField.setForeground(Color.GRAY);
                }
            }
        });

        applyCharactersLimit(idField, 4);

        toLeftPanel.add(nameLabel);
        inlinePanel.add(nameField);
        inlinePanel.add(idLabel);
        inlinePanel.add(idField);

        toLeftPanel.add(inlinePanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        emailField = buildInputField("Email:", new JTextField());
        applyCharactersLimit(emailField, 40);
        passwordField = buildInputField("Password:", new JPasswordField());
        applyCharactersLimit(passwordField, 40);

        String[] roles = {"Normale", "Amministrazione"};
        rolesGroup = buildRadioButtons("Tipologia account:", roles);

        toLeftPanel.add(Box.createVerticalGlue()); 

        actionBtn = buildButton("Crea l'account", 200, 40);
        
        JPanel actionBtnPanel = new JPanel();
        actionBtnPanel.setLayout(new BoxLayout(actionBtnPanel, BoxLayout.X_AXIS));
        actionBtnPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        actionBtnPanel.setOpaque(false);
        actionBtnPanel.add(Box.createHorizontalGlue()); //Push from left
        actionBtnPanel.add(actionBtn);                  
        actionBtnPanel.add(Box.createHorizontalGlue()); //Push from right
        
        toLeftPanel.add(actionBtnPanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        backBtn = buildButton("Indietro", 90, 35);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT); 
        toLeftPanel.add(backBtn);

        backBtn.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            this.dispose();
        });
            
        actionBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String email = emailField.getText().trim();
            String pswrdRaw = new String(passwordField.getPassword());
            String role = extractSelectedRole(rolesGroup);

            if (!isRegistrationInputValid(name, id, email, pswrdRaw, role)) {
                showRegistrationError("Non tutti i campi sono stati completati. È pregato di riprovare.", "Creazione utente non riuscita", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String password = passwordHashingUtils.hashPassword(pswrdRaw);
            utenteModel newUser = new utenteModel(name, email, password, role, id);
            
            makeAccountController controller = new makeAccountController();
            String result = controller.registerNewUser(newUser);

            handleRegistrationResult(result, name, id, parentFrame);
        });
        
        
       
    }
    
    private String extractSelectedRole(ButtonGroup group) {
        if (group == null) return "";
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText().toUpperCase();
            }
        }
        return "";
    }

    public static boolean isRegistrationInputValid(String name, String id, String email, String password, String role) {		//Tested
        return !(name == null || name.isEmpty() ||
                 id == null || id.isEmpty() ||
                 email == null || email.isEmpty() ||
                 password == null || password.isEmpty() ||
                 role == null || role.isEmpty());
    }

    private void showRegistrationError(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void handleRegistrationResult(String result, String name, String id, JFrame parentFrame) {
        switch (result) {
            case "SUCCESS":
                JOptionPane.showMessageDialog(this,
                        "Utente " + name + " creato con successo!",
                        "Creazione riuscita", JOptionPane.INFORMATION_MESSAGE);
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
                this.dispose();
                break;
            case "EMAIL_ERROR":
                showRegistrationError("L'email inserita è già associata ad un altro account.", "Email già in uso", JOptionPane.WARNING_MESSAGE);
                break;
            case "ID_ERROR":
                showRegistrationError("L'ID inserito (" + id + ") è già stato assegnato. È pregato di sceglierne un altro.", "ID non disponibile", JOptionPane.WARNING_MESSAGE);
                break;
            default:
                showRegistrationError("Errore nella creazione dell'utente.", "Creazione non riuscita", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
    
    private void applyCharactersLimit(JTextComponent field, int maxLimit) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (fb.getDocument().getLength() + text.length() - length <= maxLimit) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep(); 
                }
            }
        });
    }

}