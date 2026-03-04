package view;

import javax.swing.*;
import java.awt.*;


public class adminDashboardFrame extends baseDashboardFrame {	//Mockup M2.1 Frame

    public adminDashboardFrame(String nomeUtente) {
        super(nomeUtente); 

        JLabel adminSectionLabel = new JLabel("--- Operazioni Admin ---");
        adminSectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminSectionLabel.setForeground(Color.RED);

        adminPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        adminPanel.add(adminSectionLabel);
        adminPanel.add(Box.createRigidArea(new Dimension(0, 10)));


        JButton makeAccountBtn = buildButton("Crea nuovo account");
        makeAccountBtn.addActionListener(e -> {
            this.setVisible(false);
            new makeAccountFrame(this).setVisible(true); 
        });
        
        JButton deleteAccountBtn = buildButton("Elimina account");

        adminPanel.add(makeAccountBtn);
        adminPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        adminPanel.add(deleteAccountBtn);
    }
    

    
    public static void main(String[] args) {
    	
    	System.setProperty("sun.java2d.d3d", "false");	//Notifiche NVIDIA

        SwingUtilities.invokeLater(() -> {
            adminDashboardFrame frame = new adminDashboardFrame("Francesco");
            frame.setVisible(true); 
        });
    }
}