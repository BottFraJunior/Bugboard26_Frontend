package view;

import javax.swing.*;
import java.awt.*;

public class defaultFrame extends JFrame {		//Standardized class for building frames 

    protected JPanel centerPanel;
    protected JPanel toLeftPanel;

    public defaultFrame(String mainTitle) {
        setTitle("BugBoard26");
        setSize(400, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(mainTitle);                      
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        
        toLeftPanel = new JPanel();                                            
        toLeftPanel.setLayout(new BoxLayout(toLeftPanel, BoxLayout.Y_AXIS));
        toLeftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        
        
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(separator);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                
        mainPanel.add(centerPanel);
        mainPanel.add(toLeftPanel);
        add(mainPanel);
        
    }

    //Methods for building predefined frame assets 

    protected <T extends JTextField> T buildInputField(String labelText, T field) {   
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        toLeftPanel.add(label);											//Default position: left
        toLeftPanel.add(field);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15))); 
        
        return field;
    }


    protected JButton buildButton(String text, int width, int height) {	
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, height));
        btn.setMinimumSize(new Dimension(width, height)); 
        btn.setMaximumSize(new Dimension(width, height));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        															//No default position
        return btn; 
    }
    
    protected JButton buildButton(String text) {	//Default sizes
    	return buildButton(text, 250, 40);
    }
    
    
    protected ButtonGroup buildRadioButtons(String labelText, String[] options) {
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        toLeftPanel.add(label);

        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            JRadioButton rb = new JRadioButton(options[i]);
            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) rb.setSelected(true); 
            group.add(rb);
            toLeftPanel.add(rb);
        }
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        return group;
    }
    
}