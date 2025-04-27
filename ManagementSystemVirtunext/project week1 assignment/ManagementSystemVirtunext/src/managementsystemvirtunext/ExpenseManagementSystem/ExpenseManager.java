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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExpenseManager extends JFrame {
    private int userId;
    private String username;

    public ExpenseManager(int userId, String username) {
        this.userId = userId;
        this.username = username;
        
        setTitle("Expense Manager - " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with card layout
        JPanel mainPanel = new JPanel(new CardLayout());
        
        // Create different views
        JPanel dashboardPanel = createDashboardPanel();
        AddExpensePanel addExpensePanel = new AddExpensePanel(userId);
        ViewExpensesPanel viewExpensesPanel = new ViewExpensesPanel(userId);

        mainPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(addExpensePanel, "AddExpense");
        mainPanel.add(viewExpensesPanel, "ViewExpenses");

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        
        // Expenses Menu
        JMenu expenseMenu = new JMenu("Expenses");
        JMenuItem addExpenseItem = new JMenuItem("Add Expense");
        JMenuItem viewExpensesItem = new JMenuItem("View Expenses");
        
        expenseMenu.add(addExpenseItem);
        expenseMenu.add(viewExpensesItem);
        menuBar.add(expenseMenu);
        
        // Reports Menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem monthlyReportItem = new JMenuItem("Monthly Summary");
        reportsMenu.add(monthlyReportItem);
        menuBar.add(reportsMenu);
        
        setJMenuBar(menuBar);
        JMenu accountMenu = new JMenu("Account");
JMenuItem returnToWelcomeItem = new JMenuItem("Return to Welcome");
accountMenu.add(returnToWelcomeItem);
menuBar.add(accountMenu);

// Add this action listener for the return button
returnToWelcomeItem.addActionListener(e -> {
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to return to the welcome page?", 
        "Confirm", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        new LoginFrame().setVisible(true);
        dispose();
    }
});

        // Menu Actions
        addExpenseItem.addActionListener(e -> {
            ((CardLayout)mainPanel.getLayout()).show(mainPanel, "AddExpense");
        });
        
        viewExpensesItem.addActionListener(e -> {
            viewExpensesPanel.refreshExpenses();
            ((CardLayout)mainPanel.getLayout()).show(mainPanel, "ViewExpenses");
        });
        
        monthlyReportItem.addActionListener(e -> {
            showMonthlyReport();
        });

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Expense Manager", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    private void showMonthlyReport() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String sql = "SELECT category, SUM(amount) as total FROM expenses " +
                         "WHERE user_id = ? AND MONTH(expense_date) = MONTH(CURRENT_DATE()) " +
                         "GROUP BY category";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder report = new StringBuilder();
            report.append("Monthly Expense Summary:\n\n");
            
            double grandTotal = 0;
            while (rs.next()) {
                String category = rs.getString("category");
                double total = rs.getDouble("total");
                report.append(String.format("%-15s: $%.2f\n", category, total));
                grandTotal += total;
            }
            report.append(String.format("\n%-15s: $%.2f", "Total", grandTotal));
            
            JOptionPane.showMessageDialog(this, report.toString(), "Monthly Report", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}