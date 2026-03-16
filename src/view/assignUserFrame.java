package view;

import javax.swing.*;

public class assignUserFrame extends selectUserFrame {		//Mockup M8 Frame

    private model.issueModel assignedIssue;

    public assignUserFrame(JFrame parentFrame, model.issueModel issue) {
        super(parentFrame, "Assegna");
        this.assignedIssue = issue;
        
        titleLabel.setText("Scegli un assegnatario");
    }

    @Override
    protected void performAction(model.utenteModel user) {
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Vuoi assegnare l'issue a " + user.getNome() + "?\nLo stato verrà automaticamente aggiornato.'", 
            "Conferma Assegnazione", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            
        	assignedIssue.setAssegnatario(user);
        	assignedIssue.setStato("IN PROGRESS");
            
            controller.adminInfoIssueController adminCtrl = new controller.adminInfoIssueController();
            
            boolean success = adminCtrl.updateIssue(assignedIssue); 
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Issue assegnata con successo!", "Assegnazione completata", JOptionPane.INFORMATION_MESSAGE);
                
                adminInfoIssueFrame parentAdminFrame = (adminInfoIssueFrame) this.parentFrame;
                parentAdminFrame.assigneeLabel.setText("Assegnato a: " + user.getNome() + " (#" + user.getIdentificativo() + ") ");
                
                if (this.parentFrame != null) {
                    if (this.parentFrame instanceof showIssueFrame) {
                        ((showIssueFrame) this.parentFrame).refreshData();
                    }
                    this.parentFrame.setVisible(true);
                    this.dispose(); 
                }
            } else {
                JOptionPane.showMessageDialog(this, "Errore di connessione col server.", "Errore", JOptionPane.ERROR_MESSAGE);                
                assignedIssue.setAssegnatario(null);
                assignedIssue.setStato("Todo");
            }
        }
    }
}