package view;

import javax.swing.*;

public class deleteUserFrame extends selectUserFrame {		//Mockup M7 Frame

    public deleteUserFrame(JFrame parentFrame) {
        super(parentFrame, "Elimina");

        titleLabel.setText("Scegli l'account da eliminare");

    }

    @Override
    protected void performAction(model.utenteModel user) {
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Sei sicuro di voler eliminare definitivamente l'utente " + user.getNome() + " (ID#" + user.getIdentificativo() + 
            ")?\nQuesta operazione non può essere annullata.", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String deletedUserEmail = user.getEmail();
            String currentUserEmail = model.sessionManager.getInstance().getCurrentUser().getEmail();           
        	
            controller.deleteUserController deleteCtrl = new controller.deleteUserController();
            
            boolean success = deleteCtrl.deleteUser(user.getIdentificativo()); 
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Utente " + user.getNome() + " (ID#" + user.getIdentificativo() + 
                		") eliminato con successo!", "Eliminazione completata", JOptionPane.INFORMATION_MESSAGE);              
              
                if(deletedUserEmail.equals(currentUserEmail) ) {	//In case an user deletes itself
                	this.parentFrame.dispose();
                	new loginFrame().setVisible(true);
                } else if (this.parentFrame != null) {
                	this.parentFrame.setVisible(true);
                }
                                
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Errore di connessione col server o utente inesistente.", "Errore", JOptionPane.ERROR_MESSAGE);                
            }
        }
    }
}