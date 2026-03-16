package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class baseInfoIssueFrame extends defaultFrame {		//Mockup M6 Frame

    protected JFrame parentFrame;
    protected model.issueModel issue;
    protected JLabel assigneeLabel;
    
    protected JPanel adminExtensionPanel; 		//In order to add admin actions at the bottom

    public baseInfoIssueFrame(JFrame parentFrame, model.issueModel issue) {
        super(issue.getTitolo());
        this.parentFrame = parentFrame;
        this.issue = issue;

        setSize(500, 575);
        toLeftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setToolTipText(issue.getTitolo()); 
        
        
        JTextArea descArea = new JTextArea(issue.getDescrizione());
        descArea.setEditable(false);     
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Arial", Font.PLAIN, 13));
        descArea.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        scrollPane.setMaximumSize(new Dimension(Short.MAX_VALUE, 250));
        
        toLeftPanel.add(scrollPane);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        JPanel badgesPanel = new JPanel();
        badgesPanel.setLayout(new BoxLayout(badgesPanel, BoxLayout.X_AXIS));
        badgesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        badgesPanel.setOpaque(false);

        String tipo = issue.getTipologia();
        String priorita = issue.getPriorita();
        String stato = issue.getStato();

        badgesPanel.add(createBadge(tipo, Color.WHITE));
        badgesPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        badgesPanel.add(createBadge(priorita, getPriorityColor(issue.getPriorita())));
        badgesPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        badgesPanel.add(createBadge(stato, getStatusColor(stato)));

        toLeftPanel.add(badgesPanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        byte[] imgBytes = issue.getImmagine();
        if (imgBytes != null && imgBytes.length > 0) {
            JButton viewImageBtn = new JButton("🖼️ Visualizza allegato");
            viewImageBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            viewImageBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            viewImageBtn.addActionListener(e -> showImage(imgBytes));
            
            toLeftPanel.add(viewImageBtn);
            toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        
        JPanel usersPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        usersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usersPanel.setOpaque(false);
        usersPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        
        String assigneeText = "nessuno";
        if (issue.getAssegnatario() != null) {
            String name = issue.getAssegnatario().getNome() != null ? issue.getAssegnatario().getNome() : issue.getAssegnatario().getEmail();
            String id = issue.getAssegnatario().getIdentificativo() != null ? issue.getAssegnatario().getIdentificativo() : "";
            assigneeText = name + (id.isEmpty() ? "" : " (#" + id + ")");
        }

        String authorText = "Utente Eliminato";		//An issue cannot be created by no one, so that's only the case after deleting an user
        if (issue.getAutore() != null) {
            String nome = issue.getAutore().getNome() != null ? issue.getAutore().getNome() : issue.getAutore().getEmail();
            String id = issue.getAutore().getIdentificativo() != null ? issue.getAutore().getIdentificativo() : "";
            authorText = nome + (id.isEmpty() ? "" : " (#" + id + ")");
        }

        assigneeLabel = new JLabel("Assegnato a: " + assigneeText);
        assigneeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        JLabel authorLabel = new JLabel("Segnalato da: " + authorText);
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        authorLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        usersPanel.add(assigneeLabel);
        usersPanel.add(authorLabel);
        
        toLeftPanel.add(usersPanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        adminExtensionPanel = new JPanel();
        adminExtensionPanel.setLayout(new BoxLayout(adminExtensionPanel, BoxLayout.X_AXIS));
        adminExtensionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        adminExtensionPanel.setOpaque(false);
        
        toLeftPanel.add(adminExtensionPanel);
        toLeftPanel.add(Box.createVerticalGlue()); 

        
        JButton backBtn = buildButton("Indietro", 90, 35);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (this.parentFrame != null) {
            	if (this.parentFrame instanceof showIssueFrame) {		  //Update the parentFrame to show the changes
                    ((showIssueFrame) this.parentFrame).refreshData();
                }
                this.parentFrame.setVisible(true);
            }
            this.dispose();
        });
        toLeftPanel.add(backBtn);
    }


    
    //Method to show the image (if attached)
    private void showImage(byte[] imageBytes) {
        try {
            ImageIcon icon = new ImageIcon(imageBytes);
            Image img = icon.getImage();
            
            int maxW = 600;
            if (icon.getIconWidth() > maxW) {
                int newH = (maxW * icon.getIconHeight()) / icon.getIconWidth();
                img = img.getScaledInstance(maxW, newH, Image.SCALE_SMOOTH);
            }
            
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            
            JDialog dialog = new JDialog(this, "Allegato dell'Issue", true);
            dialog.add(new JScrollPane(imgLabel)); 
            dialog.pack();
            dialog.setLocationRelativeTo(this); 
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il caricamento dell'immagine.", "Impossibile visualizzare l'allegato", 
            		JOptionPane.ERROR_MESSAGE);
        }
    }

    
    //Methods to build filter tag badges
    private JLabel createBadge(String text, Color bgColor) {
        JLabel badge = new JLabel("  " + text + "  "); 
        badge.setBackground(bgColor);
        badge.setFont(new Font("Arial", Font.PLAIN, 12));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.equals(Color.WHITE) ? Color.LIGHT_GRAY : Color.GRAY, 1, true),
            new EmptyBorder(3, 5, 3, 5)
        ));
        return badge;
    }

    private Color getPriorityColor(String priority) {
        if (priority == null) return new Color(240, 240, 240);
        switch (priority.toLowerCase()) {
            case "bassa": return new Color(200, 240, 200); 
            case "media": return new Color(255, 220, 180); 
            case "alta": return new Color(255, 180, 180);  
            default: return new Color(240, 240, 240);      
        }
    }

    private Color getStatusColor(String status) {
        if (status == null) return Color.WHITE;
        switch (status.toLowerCase()) {
            case "todo": return new Color(210, 245, 255);        
            case "in progress": return new Color(140, 230, 230); 
            case "done": return new Color(100, 200, 230);        
            default: return Color.WHITE;
        }
    }
}