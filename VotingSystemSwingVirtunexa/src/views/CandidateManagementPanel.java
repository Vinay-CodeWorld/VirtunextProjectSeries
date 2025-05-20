/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

// Panel for managing candidates
public class CandidateManagementPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private VotingSystem system;
    private DefaultTableModel tableModel;

    // Constructor: Sets up the candidate management panel UI
    public CandidateManagementPanel(VotingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Party"}, 0);
        refreshTable();
        JTable table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        JTextField id = new JTextField(5);
        JTextField name = new JTextField(10);
        JTextField party = new JTextField(10); // Changed to JTextField
        JButton addBtn = new JButton("Add Candidate");

        form.add(new JLabel("ID:"));
        form.add(id);
        form.add(new JLabel("Name:"));
        form.add(name);
        form.add(new JLabel("Party:"));
        form.add(party); // Added party text field
        form.add(addBtn);

        addBtn.addActionListener(e -> {
            system.addCandidate(id.getText(), name.getText(), party.getText());
            refreshTable();
            id.setText("");
            name.setText("");
            party.setText(""); // Clear party field
        });

        add(form, BorderLayout.SOUTH);
    }

    // Method to refresh the candidate table
    private void refreshTable() {
        tableModel.setRowCount(0);
        Map<String, Candidate> candidates = system.getCandidates();

        for (Candidate c : candidates.values()) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getParty()});
        }

    }
}