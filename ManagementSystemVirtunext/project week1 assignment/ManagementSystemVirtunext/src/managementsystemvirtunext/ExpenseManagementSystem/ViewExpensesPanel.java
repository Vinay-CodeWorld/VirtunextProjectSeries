/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managementsystemvirtunext.ExpenseManagementSystem;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class ViewExpensesPanel extends JPanel {
    private JTable expensesTable;
    private DefaultTableModel tableModel;
    private int userId;
    
    public ViewExpensesPanel(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        
        // Table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Category");
        tableModel.addColumn("Description");
        tableModel.addColumn("Date");
        
        expensesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expensesTable);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        JButton deleteButton = new JButton("Delete Selected");
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Button actions
        refreshButton.addActionListener(e -> refreshExpenses());
        deleteButton.addActionListener(e -> deleteSelectedExpense());
        
        refreshExpenses();
    }
    
    public void refreshExpenses() {
        tableModel.setRowCount(0); // Clear existing data
        
        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY expense_date DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("expense_id"),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getDate("expense_date")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + ex.getMessage(), 
                                       "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSelectedExpense() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an expense to delete", 
                                       "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int expenseId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete this expense?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DataBaseConnection.getConnection()) {
                String sql = "DELETE FROM expenses WHERE expense_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, expenseId);
                
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    refreshExpenses();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting expense: " + ex.getMessage(), 
                                           "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
