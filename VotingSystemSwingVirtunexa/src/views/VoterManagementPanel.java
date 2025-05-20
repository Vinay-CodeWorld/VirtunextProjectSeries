/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

/**
 *
 * @author Admin
 */
import models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

// Panel for managing voters
public class VoterManagementPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private VotingSystem system;
    private DefaultTableModel tableModel;

    // Constructor: Sets up the voter management panel UI
    public VoterManagementPanel(VotingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name"}, 0);
        refreshTable();
        JTable table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        JTextField id = new JTextField(5);
        JTextField name = new JTextField(10);
        JButton addBtn = new JButton("Register Voter");

        form.add(new JLabel("ID:"));
        form.add(id);
        form.add(new JLabel("Name:"));
        form.add(name);
        form.add(addBtn);

        addBtn.addActionListener(e -> {
            system.registerVoter(id.getText(), name.getText());
            refreshTable();
            id.setText("");
            name.setText("");
        });

        add(form, BorderLayout.SOUTH);
    }

    // Method to refresh the voter table
    private void refreshTable() {
        tableModel.setRowCount(0);
        Map<String, Voter> voters = system.getVoters();
        for (Voter v : voters.values()) {
            tableModel.addRow(new Object[]{v.getId(), v.getName()});
        }

    }
}