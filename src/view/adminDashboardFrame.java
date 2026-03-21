package view;

import javax.swing.*;
import java.awt.*;


public class adminDashboardFrame extends baseDashboardFrame {	//Mockup M2.1 Frame

    public adminDashboardFrame() {
        super(); 

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
        deleteAccountBtn.addActionListener(e -> {
        	new deleteUserFrame(this).setVisible(true);
        	this.setVisible(false); 
        });

        adminPanel.add(makeAccountBtn);
        adminPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        adminPanel.add(deleteAccountBtn);
    }
   
}