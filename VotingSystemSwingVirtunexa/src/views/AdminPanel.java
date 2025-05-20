 package views;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
 import java.util.HashMap;

// Panel for administrator functions
public class AdminPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private VotingSystem system;
    private JLabel statusLabel;

    // Constructor: Sets up the admin panel UI
    public AdminPanel(VotingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        // Status panel
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Election Status: " + (system.isElectionStarted() ? "ACTIVE" : "INACTIVE"));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);

        // Control panel
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));

        JButton startBtn = new JButton("Start Election");
        startBtn.addActionListener(this::startElection);

        JButton endBtn = new JButton("End Election");
        endBtn.addActionListener(this::endElection);

        controlPanel.add(startBtn);
        controlPanel.add(endBtn);
        add(controlPanel, BorderLayout.CENTER);

        // Navigation buttons
        JPanel navPanel = new JPanel(new GridLayout(1, 3));
        JButton partiesBtn = new JButton("Manage Parties");
        JButton candidatesBtn = new JButton("Manage Candidates");
        JButton votersBtn = new JButton("Manage Voters");

        partiesBtn.addActionListener(e -> showPartyManagement());
        candidatesBtn.addActionListener(e -> showCandidateManagement());
        votersBtn.addActionListener(e -> showVoterManagement());

        navPanel.add(partiesBtn);
        navPanel.add(candidatesBtn);
        navPanel.add(votersBtn);
        add(navPanel, BorderLayout.SOUTH);
    }

    // Method to handle starting the election
    private void startElection(ActionEvent e) {
        system.startElection();
        statusLabel.setText("Election Status: ACTIVE");
        JOptionPane.showMessageDialog(this, "Election started successfully!");

    }

    // Method to handle ending the election
    private void endElection(ActionEvent e) {
        system.endElection();
        statusLabel.setText("Election Status: INACTIVE");
        showResults();
        JOptionPane.showMessageDialog(this, "Election ended successfully!");

    }

    // Method to display the election results
    private void showResults() {
        Map<String, Integer> results = calculateResults(); // Get results from the new method.

        StringBuilder resultText = new StringBuilder("Election Results:\n\n");
        if (results.isEmpty()) {
            resultText.append("No votes have been cast yet.\n");
        } else {
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                Candidate c = system.getCandidates().get(entry.getKey());
                if (c != null) {
                    resultText.append(c.getName()).append(" (").append(c.getParty())
                            .append("): ").append(entry.getValue()).append(" votes\n");
                }
            }
        }

        JTextArea textArea = new JTextArea(resultText.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Election Results",
                JOptionPane.INFORMATION_MESSAGE);

    }

    // Calculate Vote Results
    private Map<String, Integer> calculateResults() {
        Map<String, Integer> results = new HashMap<>();
        List<VoteRecord> votes = system.getVotes(); // Get the votes

        for (VoteRecord vote : votes) {
            String candidateId = vote.getCandidateId();
            results.put(candidateId, results.getOrDefault(candidateId, 0) + 1);
        }
        return results;
    }

    // Method to show the party management panel
    private void showPartyManagement() {
        JFrame frame = new JFrame("Party Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.add(new PartyManagementPanel(system));
        frame.setVisible(true);
    }

    // Method to show the candidate management panel
    private void showCandidateManagement() {
        JFrame frame = new JFrame("Candidate Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.add(new CandidateManagementPanel(system));
        frame.setVisible(true);
    }

    // Method to show the voter management panel
    private void showVoterManagement() {
        JFrame frame = new JFrame("Voter Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.add(new VoterManagementPanel(system));
        frame.setVisible(true);
    }
}
