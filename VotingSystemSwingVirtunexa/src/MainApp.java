/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
import models.*;
import views.*;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                VotingSystem system = new VotingSystem();
                
                JFrame frame = new JFrame("Voting System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                
                JTabbedPane tabs = new JTabbedPane();
                
                // Admin tab
                tabs.add("Admin", new AdminPanel(system));
                
                // Voting tab - in a real system, you'd have proper voter authentication
                String voterId = JOptionPane.showInputDialog("Enter your voter ID:");
                if (voterId != null && !voterId.trim().isEmpty()) {
                    tabs.add("Vote", new VotingPanel(system, voterId));
                }
                
                // Results tab
                tabs.add("Results", new ResultPanel(system));
                
                frame.add(tabs);
                frame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error initializing system: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}