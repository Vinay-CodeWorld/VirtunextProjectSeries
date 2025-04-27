package managementsystemvirtunext.ExpenseManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center screen

        // Outer container with background color
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(240, 248, 255)); // Light blueish

        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(350, 400));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel title = new JLabel("Welcome Back!");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JButton loginBtn = new JButton("Login");
        JButton toSignup = new JButton("Create Account");
        loginBtn.setBackground(new Color(33, 150, 243));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        toSignup.setBorderPainted(false);
        toSignup.setForeground(new Color(33, 150, 243));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        toSignup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Layout
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(new JLabel("Username"));
        cardPanel.add(userField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(new JLabel("Password"));
        cardPanel.add(passField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(loginBtn);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(toSignup);

        // Add card to background panel
        backgroundPanel.add(cardPanel);

        add(backgroundPanel);

        // Login Action
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            try (Connection conn = DataBaseConnection.getConnection()) {
                String sql = "SELECT id, full_name FROM account WHERE username = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String fullName = rs.getString("full_name");

                    JOptionPane.showMessageDialog(this, "Welcome back, " + fullName + "!");
                    new ExpenseManager(userId, username).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Switch to Signup
        toSignup.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
