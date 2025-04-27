package managementsystemvirtunext.ExpenseManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignupFrame extends JFrame {
    public SignupFrame() {
        setTitle("Sign Up");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center screen

        // Outer container with background color
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(new Color(240, 248, 255)); // Light blueish

        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(350, 450));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form fields
        JTextField nameField = createFormField();
        JTextField emailField = createFormField();
        JTextField usernameField = createFormField();
        JPasswordField passField = new JPasswordField();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Buttons
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBackground(new Color(33, 150, 243));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFocusPainted(false);
        signupBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton toLogin = new JButton("Already have an account? Login");
        toLogin.setBorderPainted(false);
        toLogin.setForeground(new Color(33, 150, 243));
        toLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Layout
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(new JLabel("Full Name"));
        cardPanel.add(nameField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(new JLabel("Email"));
        cardPanel.add(emailField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(new JLabel("Username"));
        cardPanel.add(usernameField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(new JLabel("Password"));
        cardPanel.add(passField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(signupBtn);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(toLogin);

        // Add card to background panel
        backgroundPanel.add(cardPanel);
        add(backgroundPanel);

        // Signup Action
        signupBtn.addActionListener(e -> {
            String fullName = nameField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = new String(passField.getPassword());

            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DataBaseConnection.getConnection()) {
                // Check if user exists
                if (userExists(conn, email, username)) {
                    JOptionPane.showMessageDialog(this, "Username or email already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert new user
                String sql = "INSERT INTO account (full_name, email, username, password) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, fullName);
                    pstmt.setString(2, email);
                    pstmt.setString(3, username);
                    pstmt.setString(4, password);
                    
                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        new LoginFrame().setVisible(true);
                        dispose();
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Switch to Login
        toLogin.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private JTextField createFormField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return field;
    }

    private boolean userExists(Connection conn, String email, String username) throws SQLException {
        String sql = "SELECT 1 FROM account WHERE email = ? OR username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignupFrame().setVisible(true));
    }
}