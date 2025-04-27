package managementsystemvirtunext.ExpenseManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class AddExpensePanel extends JPanel {
    private JTextField amountField;
    private JTextField categoryField;
    private JTextArea descriptionArea;
    private JTextField dateField;
    private int userId;

    public AddExpensePanel(int userId) {
        this.userId = userId;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // Set the alignment for labels and fields
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("Add Expense");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 102, 204)); // Color for the title
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth to 1

        // Amount
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(amountLabel, gbc);
        gbc.gridx = 1;
        add(amountField, gbc);

        // Category
        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(categoryLabel, gbc);
        gbc.gridx = 1;
        add(categoryField, gbc);

        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true); // Enable line wrapping
        descriptionArea.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(descriptionLabel, gbc);
        gbc.gridx = 1;
        add(descriptionScrollPane, gbc);

        // Date
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateField = new JTextField(LocalDate.now().toString(), 15);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(dateLabel, gbc);
        gbc.gridx = 1;
        add(dateField, gbc);

        // Save button
        JButton saveButton = new JButton("Save Expense");
        saveButton.setBackground(new Color(0, 102, 204));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setFocusPainted(false); // Remove the focus border
        saveButton.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Make button span across two columns
        add(saveButton, gbc);
        
        saveButton.addActionListener(e -> saveExpense());
    }

    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String category = categoryField.getText();
            String description = descriptionArea.getText();
            LocalDate date = LocalDate.parse(dateField.getText());

            try (Connection conn = DataBaseConnection.getConnection()) {
                String sql = "INSERT INTO expenses (user_id, amount, category, description, expense_date) " +
                        "VALUES (?, ?, ?, ?, ?)";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setDouble(2, amount);
                stmt.setString(3, category);
                stmt.setString(4, description);
                stmt.setDate(5, Date.valueOf(date));

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Expense saved successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving expense: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        amountField.setText("");
        categoryField.setText("");
        descriptionArea.setText("");
        dateField.setText(LocalDate.now().toString());
    }
}
