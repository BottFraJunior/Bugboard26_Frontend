package view;

import javax.swing.*;
import java.awt.*;


public class adminDashboardFrame extends baseDashboardFrame {	//Mockup M2.1 Frame

    public adminDashboardFrame(String nomeUtente) {
        super(nomeUtente); 

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel adminSectionLabel = new JLabel("--- Operazioni Admin ---");
        adminSectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminSectionLabel.setForeground(Color.RED);
        centerPanel.add(adminSectionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton makeAccountBtn = buildDashboardBtn("Crea nuovo account", 250, 40);
        JButton deleteAccountBtn = buildDashboardBtn("Elimina account", 250, 40);

        centerPanel.add(makeAccountBtn);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(deleteAccountBtn);
    }
    
    public static void main(String[] args) {
    	
    	System.setProperty("sun.java2d.d3d", "false");	//Notifiche NVIDIA

        SwingUtilities.invokeLater(() -> {
            adminDashboardFrame frame = new adminDashboardFrame("Francesco");
            frame.setVisible(true); 
        });
    }
}