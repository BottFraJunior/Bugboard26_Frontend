package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class showIssueFrame extends defaultFrame {		//Mockup M4 Frame

    private JFrame parentFrame;

    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JComboBox<String> subTypeCombo; 
    private JCheckBox assignedIssueCheck;
    private JPanel issuesListPanel;
    
    private List<model.issueModel> allIssues = new ArrayList<>();

    public showIssueFrame(JFrame parentFrame) {
        super("Riepilogo issues pubblicate");
        this.parentFrame = parentFrame;

        setSize(650, 500);
        toLeftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        JPanel topBarPanel = new JPanel();
        topBarPanel.setLayout(new BoxLayout(topBarPanel, BoxLayout.Y_AXIS));
        topBarPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topBarPanel.setOpaque(false);

        searchField = new JTextField("Cerca");
        searchField.setForeground(Color.GRAY);
        searchField.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(5, 5, 5, 5)));
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Cerca")) {
                	searchField.setText("");
                	searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                	searchField.setText("Cerca");
                	searchField.setForeground(Color.GRAY);
                }
            }
        });

        
        JPanel filterRow = new JPanel();
        filterRow.setLayout(new BoxLayout(filterRow, BoxLayout.X_AXIS));
        filterRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel filterLabel = new JLabel("Filtra/Ordina per: ");
        
        String[] filters = {"Nessuno", "Tipologia", "Stato", "Priorità"};
        filterCombo = new JComboBox<>(filters);
        filterCombo.setMaximumSize(new Dimension(100, 25));

        String[] types = {"Question", "Bug", "Documentation", "Feature"};
        subTypeCombo = new JComboBox<>(types);
        subTypeCombo.setMaximumSize(new Dimension(120, 25));
        subTypeCombo.setVisible(false); 
        
        assignedIssueCheck = new JCheckBox("Issue assegnate");
        assignedIssueCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
                
        

        filterRow.add(filterLabel);
        filterRow.add(filterCombo);
        filterRow.add(Box.createRigidArea(new Dimension(10, 0))); 
        filterRow.add(subTypeCombo);
        filterRow.add(Box.createHorizontalGlue());
        toLeftPanel.add(assignedIssueCheck);

        topBarPanel.add(searchField);
        topBarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topBarPanel.add(filterRow);
        
        toLeftPanel.add(topBarPanel);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        
        
        issuesListPanel = new JPanel();
        issuesListPanel.setLayout(new BoxLayout(issuesListPanel, BoxLayout.Y_AXIS));
        issuesListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(issuesListPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        toLeftPanel.add(scrollPane);
        toLeftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        controller.showIssueController controller = new controller.showIssueController();
        this.allIssues = controller.getAllIssues();

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateFilteredList(); }
            public void removeUpdate(DocumentEvent e) { updateFilteredList(); }
            public void changedUpdate(DocumentEvent e) { updateFilteredList(); }
        });

        filterCombo.addActionListener(e -> {
            boolean isTipologia = "Tipologia".equals(filterCombo.getSelectedItem());
            subTypeCombo.setVisible(isTipologia);
            updateFilteredList();
        });
        
        subTypeCombo.addActionListener(e -> updateFilteredList());
        
        assignedIssueCheck.addActionListener(e -> updateFilteredList()); 

        updateFilteredList();	//First load without filters

        
        
        JButton backBtn = buildButton("Indietro", 90, 35);
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            if (this.parentFrame != null) {
                this.parentFrame.setVisible(true);
            }
            this.dispose();
        });
        toLeftPanel.add(backBtn);
    }
    

    //Method that applies the various filters to the issue showed
    private void updateFilteredList() {
        issuesListPanel.removeAll();
        
        String textFilter = getCleanSearchText();
        String mainMode = (String) filterCombo.getSelectedItem();
        String selectedType = (String) subTypeCombo.getSelectedItem();
        boolean showOnlyMine = assignedIssueCheck != null && assignedIssueCheck.isSelected();
        String myEmail = model.sessionManager.getInstance().getCurrentUser().getEmail();
        
        List<model.issueModel> filteredIssues = filterIssues(allIssues, textFilter, mainMode, selectedType, showOnlyMine, myEmail);
        
        sortIssues(filteredIssues, mainMode);
        
        renderFilteredIssues(filteredIssues);
    }

    private String getCleanSearchText() {
        String rawSearchText = searchField.getText().trim();
        if (rawSearchText.equals("Cerca") && Color.GRAY.equals(searchField.getForeground())) {
            return ""; 
        }
        return rawSearchText.toLowerCase();
    }

    public static List<model.issueModel> filterIssues(List<model.issueModel> issues, String textFilter, 	//Tested
    		String mainMode, String selectedType, boolean showOnlyMine, String myEmail) {
        List<model.issueModel> result = new ArrayList<>();
        
        for (model.issueModel issue : issues) {
            String titolo = issue.getTitolo() != null ? issue.getTitolo() : "";
            String descrizione = issue.getDescrizione() != null ? issue.getDescrizione() : "";
            String tipologia = issue.getTipologia() != null ? issue.getTipologia() : "";
            
            boolean matchesSearch = textFilter.isEmpty() || 
                                    titolo.toLowerCase().contains(textFilter) || 
                                    descrizione.toLowerCase().contains(textFilter);
            
            boolean matchesType = true;
            if ("Tipologia".equals(mainMode) && selectedType != null) {
                matchesType = tipologia.equalsIgnoreCase(selectedType);
            }
            
            boolean matchesAssignee = true;
            if (showOnlyMine) {
                if (issue.getAssegnatario() == null || !issue.getAssegnatario().getEmail().equalsIgnoreCase(myEmail)) {
                    matchesAssignee = false;
                }
            }

            if (matchesSearch && matchesType && matchesAssignee) {
                result.add(issue);
            }
        }
        return result;
    }

    private void sortIssues(List<model.issueModel> filteredIssues, String mainMode) {
        if ("Stato".equals(mainMode)) {
            filteredIssues.sort(Comparator.comparingInt(issue -> getStatoWeight(issue.getStato())));
        } else if ("Priorità".equals(mainMode)) {
            filteredIssues.sort(Comparator.comparingInt(issue -> getPrioritaWeight(issue.getPriorita())));
        }
    }

    private void renderFilteredIssues(List<model.issueModel> filteredIssues) {
        if (filteredIssues.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nessuna issue corrisponde ai criteri di ricerca.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            issuesListPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            issuesListPanel.add(emptyLabel);
        } else {
            for (model.issueModel issue : filteredIssues) {
                String titolo = issue.getTitolo() != null ? issue.getTitolo() : "";
                String tipologia = issue.getTipologia() != null ? issue.getTipologia() : "";
                String stato = issue.getStato() != null ? issue.getStato() : "Todo";
                String priorita = issue.getPriorita() != null && !issue.getPriorita().isEmpty() ? issue.getPriorita() : "Nessuna";
                
                issuesListPanel.add(createIssueRow(titolo, tipologia, priorita, stato, issue));
            }
        }
        
        issuesListPanel.revalidate();
        issuesListPanel.repaint();
    }
    
    
    //Assign a weight to the 'Stato' tag filter: Todo(1), In progress(2), Done(3)
    private int getStatoWeight(String stato) {
        if (stato == null) return 1;
        switch (stato.toLowerCase()) {
            case "in progress": return 2;
            case "done": return 3;
            default: return 1; 
        }
    }

    //Assign a weight to the 'Priorità' tag filter: Alta(1), Media(2), Bassa(3), Nessuna(4)
    private int getPrioritaWeight(String priorita) {
        if (priorita == null) return 4;
        switch (priorita.toLowerCase()) {
            case "alta": return 1;
            case "media": return 2;
            case "bassa": return 3;
            default: return 4;
        }
    }
   
    
    
    //Method that builds an Issue Info Row
    private JPanel createIssueRow(String title, String type, String priority, String status, model.issueModel issue) {
        JPanel rowPanel = buildBaseRowPanel();

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        rowPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel badgesPanel = buildBadgesPanel(type, priority, status);
        rowPanel.add(badgesPanel, BorderLayout.EAST);

        attachRowMouseListener(rowPanel, issue);

        return rowPanel;
    }

    private JPanel buildBaseRowPanel() {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 60)); 
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        rowPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return rowPanel;
    }

    private JPanel buildBadgesPanel(String type, String priority, String status) {
        JPanel badgesPanel = new JPanel(new GridLayout(1, 3, 5, 0)); 
        badgesPanel.setPreferredSize(new Dimension(280, 40));
        badgesPanel.setOpaque(false);

        badgesPanel.add(createBadge("<html><center>Tipologia:<br>" + type + "</center></html>", Color.WHITE));
        badgesPanel.add(createBadge("<html><center>Priorità:<br>" + priority + "</center></html>", getPriorityColor(priority)));
        badgesPanel.add(createBadge(status, getStatusColor(status)));
        return badgesPanel;
    }

    private void attachRowMouseListener(JPanel rowPanel, model.issueModel issue) {
        rowPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { rowPanel.setBackground(new Color(245, 245, 250)); }
            
            @Override
            public void mouseExited(MouseEvent e) { rowPanel.setBackground(Color.WHITE); }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                String role = model.sessionManager.getInstance().getCurrentUser().getRuolo();
                if ("AMMINISTRAZIONE".equalsIgnoreCase(role)) {
                    new adminInfoIssueFrame(showIssueFrame.this, issue).setVisible(true);
                } else {
                    new baseInfoIssueFrame(showIssueFrame.this, issue).setVisible(true);
                }
                showIssueFrame.this.setVisible(false);                 
            }
        });
    }

   
    private JLabel createBadge(String text, Color bgColor) {
        JLabel badge = new JLabel(text, SwingConstants.CENTER);
        badge.setBackground(bgColor);
        badge.setFont(new Font("Arial", Font.PLAIN, 11));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createLineBorder(bgColor.equals(Color.WHITE) ? Color.LIGHT_GRAY : Color.GRAY));
        return badge;
    }

    private Color getPriorityColor(String priority) {
        switch (priority.toLowerCase()) {
            case "bassa": return new Color(200, 240, 200); 
            case "media": return new Color(255, 220, 180); 
            case "alta": return new Color(255, 180, 180);  
            default: return new Color(240, 240, 240);      
        }
    }

    private Color getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "todo": return new Color(210, 245, 255);        
            case "in progress": return new Color(140, 230, 230); 
            case "done": return new Color(100, 200, 230);        
            default: return Color.WHITE;
        }
    }
    
    //In order to keep updated the issues at each operation
    public void refreshData() {
        controller.showIssueController controller = new controller.showIssueController();
        this.allIssues = controller.getAllIssues();
        
        updateFilteredList();
    }
    
}