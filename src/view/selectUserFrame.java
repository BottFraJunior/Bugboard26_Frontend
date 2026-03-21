package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public abstract class selectUserFrame extends defaultFrame {	//Standardized class for building user selection frames 

    protected JFrame parentFrame;
    protected model.utenteModel selectedUser = null; 
    private JPanel usersListPanel;
    private JPanel lastSelectedRow = null;

    public selectUserFrame(JFrame parentFrame,String actionBtnName) {
        super("");
        this.parentFrame = parentFrame;

        setSize(400, 475);
        toLeftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usersListPanel = new JPanel();
        usersListPanel.setLayout(new BoxLayout(usersListPanel, BoxLayout.Y_AXIS));
        usersListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(usersListPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        toLeftPanel.add(scrollPane);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

                
        controller.selectUserController controller = new controller.selectUserController();
        List<model.utenteModel> allUsers = controller.getAllUsers();

        for (model.utenteModel utente : allUsers) {
            usersListPanel.add(createUserRow(utente));
        }
        

        JButton actionBtn = buildButton(actionBtnName, 100, 35);
        actionBtn.addActionListener(e -> {
            if (selectedUser == null) {
                JOptionPane.showMessageDialog(this, "È pregato di selezionare un utente dalla lista.", "Utente non selezionato", JOptionPane.WARNING_MESSAGE);
                return;
            }
            performAction(selectedUser);
        });

        JPanel actionBtnPanel = new JPanel();
        actionBtnPanel.setLayout(new BoxLayout(actionBtnPanel, BoxLayout.X_AXIS));
        actionBtnPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 

        actionBtnPanel.add(Box.createHorizontalGlue()); 
        actionBtnPanel.add(actionBtn);                
        actionBtnPanel.add(Box.createHorizontalGlue()); 

        toLeftPanel.add(actionBtnPanel);
        
        toLeftPanel.add(Box.createVerticalGlue()); 
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        JButton backBtn = buildButton("Indietro", 90, 35);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT); 
        backBtn.addActionListener(e -> {
            if (this.parentFrame != null) this.parentFrame.setVisible(true);
            this.dispose();
        });

        toLeftPanel.add(backBtn);
    }

    
    //Method to perform a customizable action after choosing a specific user from the list
    protected abstract void performAction(model.utenteModel user);	


    private JPanel createUserRow(model.utenteModel utente) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 50)); 
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true), 
                new EmptyBorder(10, 10, 10, 10)
        ));

        String name = utente.getNome();
        String id = utente.getIdentificativo();
        
        JLabel nameLabel = new JLabel(name + " ID#" + id, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rowPanel.add(nameLabel, BorderLayout.CENTER);

        rowPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        rowPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (lastSelectedRow != null) {
                	lastSelectedRow.setBackground(Color.WHITE);
                }
                rowPanel.setBackground(new Color(210, 245, 255));
                lastSelectedRow = rowPanel;
                selectedUser = utente;
            }
        });

        return rowPanel;
    }
}