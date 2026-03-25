package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Enumeration;

public class reportIssueFrame extends defaultFrame {	//Mockup M3 Frame

    private JTextField titleField;
    private JTextArea descriptionArea;
    private ButtonGroup typeGroup;
    private ButtonGroup priorityGroup;
    private JButton attachImageBtn;
    private JButton publishBtn;
    private JButton backBtn;
    
    private JFrame parentFrame;
    private File attachedImage = null; 
    
    private String authorEmail = model.sessionManager.getInstance().getCurrentUser().getEmail();

    public reportIssueFrame(JFrame parentFrame) {
        super("Descrivi il tipo di issue che vuoi segnalare");
        this.parentFrame = parentFrame;
        setSize(450, 625); 
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        toLeftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleField = buildInputField("Titolo:", new JTextField());
        
        //Max. 30 characters for the title
        ((AbstractDocument) titleField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (fb.getDocument().getLength() + text.length() - length <= 30) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        
        JLabel descLabel = new JLabel("Descrizione:");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        descriptionArea = new JTextArea(6, 20); 
        descriptionArea.setLineWrap(true);   		//Automatic new line    
        descriptionArea.setWrapStyleWord(true); 	//Does not split words on new lines
        
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Short.MAX_VALUE, 120));
        
        toLeftPanel.add(descLabel);
        toLeftPanel.add(scrollPane);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        typeGroup = new ButtonGroup();
        String[] types = {"Question", "Documentation", "Bug", "Feature"};
        JPanel typePanel = buildBoxedRadioGroup("Tipologia:", types, typeGroup);
        toLeftPanel.add(typePanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        priorityGroup = new ButtonGroup();
        String[] priorities = {"Nessuna", "Media", "Bassa", "Alta"};
        JPanel priorityPanel = buildBoxedRadioGroup("<html>Priorità:<br><i style='font-size:8px;'>(facoltativo)</i></html>", priorities, priorityGroup);
        toLeftPanel.add(priorityPanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));


        attachImageBtn = new JButton("📷 Allega un'immagine (facoltativo)");
        attachImageBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        attachImageBtn.setContentAreaFilled(false); 
        attachImageBtn.setBorderPainted(false);     
        attachImageBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        attachImageBtn.setHorizontalAlignment(SwingConstants.LEFT);
        attachImageBtn.setMargin(new Insets(0, 0, 0, 0));
        
        attachImageBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Immagini", "jpg", "png", "jpeg"));
            int response = fileChooser.showOpenDialog(this);
            
            if (response == JFileChooser.APPROVE_OPTION) {
            	File selectedFile = fileChooser.getSelectedFile();
            	long maxSizeInBytes = 5 * 1024 * 1024; //5MB limit
                
                if (selectedFile.length() > maxSizeInBytes) {
                    JOptionPane.showMessageDialog(this,
                            "L'immagine selezionata è troppo pesante (" + (selectedFile.length() / 1024 / 1024) + " MB).\nLa dimensione massima consentita è di 5 MB.",
                            "File troppo grande", JOptionPane.WARNING_MESSAGE);
                } else {
                    attachedImage = selectedFile;
                    attachImageBtn.setText("✅ Allegato: " + attachedImage.getName());
                    attachImageBtn.setForeground(new Color(0, 128, 0)); 
                }
            }
        });
        
        toLeftPanel.add(attachImageBtn);
        toLeftPanel.add(Box.createVerticalGlue()); 

        
        publishBtn = buildButton("Pubblica", 200, 40); 
        
        JPanel publishBtnPanel = new JPanel();
        publishBtnPanel.setLayout(new BoxLayout(publishBtnPanel, BoxLayout.X_AXIS));
        publishBtnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        publishBtnPanel.add(Box.createHorizontalGlue()); 
        publishBtnPanel.add(publishBtn);                
        publishBtnPanel.add(Box.createHorizontalGlue()); 
        
        toLeftPanel.add(publishBtnPanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        backBtn = buildButton("Indietro", 90, 35);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT); 
        toLeftPanel.add(backBtn);

        backBtn.addActionListener(e -> {
            if (this.parentFrame != null) {
                this.parentFrame.setVisible(true);
            }
            this.dispose();
        });

        publishBtn.addActionListener(e -> {
            String title = titleField.getText();
            String desc = descriptionArea.getText().trim();
            String type = getSelectedRadio(typeGroup);
            String priority = getSelectedRadio(priorityGroup);

            if (!isIssueInputValid(title, desc, 30)) {
                showValidationError("I campi Titolo e Descrizione sono obbligatori. È pregato di riprovare.");
                return;
            }

            byte[] imageBytes = extractImageBytes(attachedImage);
            if (attachedImage != null && imageBytes == null) {
                return; 
            }

            model.issueModel newIssue = new model.issueModel(title, desc, type, priority, imageBytes, authorEmail, "Nessuno");
            controller.reportIssueController reportController = new controller.reportIssueController();
            boolean success = reportController.submitIssue(newIssue);

            handlePublishResult(success);
        });
    }

    public static boolean isIssueInputValid(String title, String description, int maxTitleLength) {		//Tested
        if (title == null || title.trim().isEmpty() || 
            description == null || description.trim().isEmpty()) {
            return false;
        }
        
        return title.length() <= maxTitleLength;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Errore di validazione", JOptionPane.WARNING_MESSAGE);
    }

    private byte[] extractImageBytes(File imageFile) {
        if (imageFile == null) return null;
        
        try {
            return Files.readAllBytes(imageFile.toPath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Errore durante la lettura dell'immagine. Impossibile allegarla.", 
                "Errore File", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void handlePublishResult(boolean success) {
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "La tua issue è stata pubblicata con successo!", 
                "Issue segnalata", JOptionPane.INFORMATION_MESSAGE);
                
            if (this.parentFrame != null) {
                this.parentFrame.setVisible(true);
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Errore di comunicazione con il server. È pregato di riprovare.", 
                "Errore Server", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private JPanel buildBoxedRadioGroup(String labelText, String[] options, ButtonGroup group) {
        JPanel mainRow = new JPanel();
        mainRow.setLayout(new BoxLayout(mainRow, BoxLayout.X_AXIS));
        mainRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(70, 40)); 
        label.setMaximumSize(new Dimension(70, 40));
        label.setVerticalAlignment(SwingConstants.TOP); 

        JPanel boxPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        boxPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                new EmptyBorder(5, 5, 5, 5) 
        ));
        boxPanel.setMaximumSize(new Dimension(300, 60));

        for (int i = 0; i < options.length; i++) {
            JRadioButton rb = new JRadioButton(options[i]);
            if (i == 0) rb.setSelected(true);
            group.add(rb);
            boxPanel.add(rb);
        }

        mainRow.add(label);
        mainRow.add(Box.createRigidArea(new Dimension(10, 0)));
        mainRow.add(boxPanel);

        return mainRow;
    }

    private String getSelectedRadio(ButtonGroup group) {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return "";
    }
    
}