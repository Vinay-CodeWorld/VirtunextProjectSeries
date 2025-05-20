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

// Panel for managing political parties
public class PartyManagementPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private VotingSystem system;
    private DefaultTableModel tableModel;

    // Constructor: Sets up the party management panel UI
    public PartyManagementPanel(VotingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name"}, 0);
        refreshTable();
        JTable table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel();
        JTextField id = new JTextField(5);
        JTextField name = new JTextField(10);
        JButton addBtn = new JButton("Add Party");

        form.add(new JLabel("ID:"));
        form.add(id);
        form.add(new JLabel("Name:"));
        form.add(name);
        form.add(addBtn);

        addBtn.addActionListener(e -> {
            // Since Party class and addParty method are not present in the provided model,
            // this part will not work.  Added a message to user.
            JOptionPane.showMessageDialog(this, "Party management is not fully implemented in this version.", "Notice", JOptionPane.INFORMATION_MESSAGE);

        });

        add(form, BorderLayout.SOUTH);
    }

    private void refreshTable() {
        JOptionPane.showMessageDialog(this, "Party table cannot be refreshed.  Party management is not fully implemented.", "Notice", JOptionPane.INFORMATION_MESSAGE);
    }
}