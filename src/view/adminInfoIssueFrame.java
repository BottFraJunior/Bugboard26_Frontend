package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.*;

public class adminInfoIssueFrame extends baseInfoIssueFrame {		//Mockup M6.1 Frame

    public adminInfoIssueFrame(JFrame parentFrame, model.issueModel issue) {
        super(parentFrame, issue);

        adminExtensionPanel.setLayout(new BoxLayout(adminExtensionPanel, BoxLayout.X_AXIS));
        
        Border lineBorder = BorderFactory.createLineBorder(Color.RED);
        TitledBorder redTitledBorder = BorderFactory.createTitledBorder(
        		lineBorder,                             
        		"Azioni Amministratore",                
        		TitledBorder.LEFT,                     
        		TitledBorder.TOP,                      
        		new Font("Arial", Font.BOLD, 12),      
        		Color.RED                              
        );
        
        adminExtensionPanel.setBorder(BorderFactory.createCompoundBorder(
        		redTitledBorder,                        
        		new EmptyBorder(10, 10, 10, 10)         
        ));

        
        JButton assignBtn = new JButton("👤 Assegna ad un membro");
        assignBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton resolveBtn = new JButton("✅ Segna come risolto");
        resolveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        adminExtensionPanel.add(assignBtn);
        adminExtensionPanel.add(Box.createHorizontalGlue()); 
        adminExtensionPanel.add(resolveBtn);


        
        assignBtn.addActionListener(e -> {
        	new assignUserFrame(this, issue).setVisible(true);        	
        	this.setVisible(false);       
        });

        resolveBtn.addActionListener(e -> {           
        	issue.setStato("DONE");
        	issue.setAssegnatario(null); 
        	
        	controller.adminInfoIssueController updateCtrl = new controller.adminInfoIssueController();
        	boolean success = updateCtrl.updateIssue(issue);
        	
        	if (success) {
        		JOptionPane.showMessageDialog(this, 
        				"Issue aggiornata correttamente!", 
        				"Issue completata", JOptionPane.INFORMATION_MESSAGE);
        		
        		if (this.parentFrame != null) {
        			if (this.parentFrame instanceof showIssueFrame) {		//Update the parentFrame to show the changes
                        ((showIssueFrame) this.parentFrame).refreshData();
                    }
        			this.parentFrame.setVisible(true);
        		}
        		this.dispose();
        	} else {
        		JOptionPane.showMessageDialog(this, 
        				"Errore di comunicazione col server. Modifica non salvata.", 
        				"Issue non aggiornata", JOptionPane.ERROR_MESSAGE);
        		issue.setStato("IN PROGRESS"); 
        	}
                  
        	
        });
        	
                
    }
}