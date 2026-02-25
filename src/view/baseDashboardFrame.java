package view;

import javax.swing.*;
import java.awt.*;

public class baseDashboardFrame extends JFrame {	//Mockup M2 Frame

    protected JPanel centerPanel; 

    public baseDashboardFrame(String nomeUtente) {
        setTitle("BugBoard26");
        setSize(400, 425); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title1Label = new JLabel("Bentornato, ");
        title1Label.setFont(new Font("Arial", Font.PLAIN, 24));

        JLabel title2Label = new JLabel(nomeUtente + ".");
        title2Label.setFont(new Font("Arial", Font.PLAIN, 24));
        title2Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton reportBtn = buildDashboardBtn("Segnala un nuovo issue", 250, 40);
        JButton summaryBtn = buildDashboardBtn("Consulta il riepilogo issues", 250, 40);
        
        JButton exitBtn = buildDashboardBtn("Esci", 100, 40);
        exitBtn.addActionListener(e -> {
            this.dispose(); 
            new loginFrame().setVisible(true); 
        });
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
       
        
        titlePanel.add(title1Label);
        titlePanel.add(title2Label);
        
        mainPanel.add(titlePanel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30))); 
        centerPanel.add(reportBtn);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(summaryBtn);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(separator);
        
        mainPanel.add(centerPanel); 
        
        mainPanel.add(Box.createVerticalGlue()); 
        mainPanel.add(exitBtn);

        add(mainPanel);
    }

    protected JButton buildDashboardBtn(String text, int width, int height) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(width, height)); 
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    public static void main(String[] args) {
    	
    	System.setProperty("sun.java2d.d3d", "false");	//Notifiche NVIDIA

        SwingUtilities.invokeLater(() -> {
            baseDashboardFrame frame = new baseDashboardFrame("Francesco");
            frame.setVisible(true); 
        });
    }
}