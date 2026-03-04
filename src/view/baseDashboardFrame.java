package view;

import javax.swing.*;
import java.awt.*;

public class baseDashboardFrame extends defaultFrame {		//Mockup M2 Frame
	
	protected JPanel adminPanel;

    public baseDashboardFrame(String nomeUtente) {
        super("Bentornato, " + nomeUtente + ".");       

        setSize(400, 450); 
                
        JButton reportBtn = buildButton("Segnala un nuovo issue");
        centerPanel.add(reportBtn);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton summaryBtn = buildButton("Consulta il riepilogo issues");
        centerPanel.add(summaryBtn);
        
        
        adminPanel = new JPanel();												//In order to add admin buttons in the middle
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        centerPanel.add(adminPanel);
        
        centerPanel.add(Box.createVerticalGlue());
        
        JButton exitBtn = buildButton("Esci", 80, 40);
        exitBtn.addActionListener(e -> {
            this.dispose(); 
            new loginFrame().setVisible(true); 
        });
        centerPanel.add(exitBtn);

    }
    
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "false");  //Notifiche NVIDIA

        SwingUtilities.invokeLater(() -> {
            baseDashboardFrame frame = new baseDashboardFrame("Francesco");
            frame.setVisible(true); 
        });
    }
}