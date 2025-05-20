import models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

// Panel for handling the voting process
public class VotingPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private VotingSystem system;
    private String voterId;  // Hold the voter's ID

    // Constructor: Sets up the voting panel UI
    public VotingPanel(VotingSystem system, String voterId) {
        this.system = system;
        this.voterId = voterId; // Store the voter ID
        setLayout(new BorderLayout());

        // Check if the election has started
        if (!system.isElectionStarted()) {
            add(new JLabel("The election has not started yet. Please check back later."), BorderLayout.CENTER);
            return;
        }

        // Check if the voter is registered.
        if (!system.getVoters().containsKey(voterId)) {
            add(new JLabel("Invalid Voter ID.  You are not registered to vote.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        // Check if the voter has already voted.
        if (hasVoted()) { // Use the new method
            add(new JLabel("You have already voted. Thank you for participating!", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }

        ButtonGroup group = new ButtonGroup();
        JPanel optionsPanel = new JPanel(new GridLayout(0, 1));

        Map<String, Candidate> candidates = system.getCandidates();

        if (candidates.isEmpty()) {
            add(new JLabel("No candidates are available to vote for.", SwingConstants.CENTER), BorderLayout.CENTER);
            return;
        }
        for (Candidate candidate : candidates.values()) {
            JRadioButton radio = new JRadioButton(candidate.getName() + " (" + candidate.getParty() + ")");
            radio.setActionCommand(candidate.getId());
            group.add(radio);
            optionsPanel.add(radio);
        }

        add(new JScrollPane(optionsPanel), BorderLayout.CENTER);

        JButton voteButton = new JButton("Cast Vote");
        voteButton.addActionListener(this::castVote); // Use method reference
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(voteButton);
        add(buttonPanel, BorderLayout.SOUTH);



    }

    // Method to handle the voting process
    private void castVote(ActionEvent e) {
        ButtonGroup group = getButtonSelection();
        if (group == null || group.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select a candidate to vote for.");
            return;
        }

        String candidateId = group.getSelection().getActionCommand();
        system.castVote(voterId, candidateId);
        JOptionPane.showMessageDialog(this, "Vote cast successfully! Thank you for voting.");

        // Update UI to show that the voter has voted.
        removeAll();
        add(new JLabel("You have successfully voted. Thank you for participating!", SwingConstants.CENTER), BorderLayout.CENTER);
        revalidate();
        repaint();

    }

    //helper method
    private ButtonGroup getButtonSelection() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                Component[] innerComponents = scrollPane.getViewport().getComponents();
                for (Component innerComponent : innerComponents) {
                    if (innerComponent instanceof JPanel) {
                        JPanel panel = (JPanel) innerComponent;
                        ButtonGroup group = getButtonGroup(panel);
                        if (group != null) {
                            return group;
                        }
                    }
                }
            }
        }
        return null;
    }

    private ButtonGroup getButtonGroup(JPanel panel) {
        ButtonGroup foundGroup = null;
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JRadioButton) {
                JRadioButton radio = (JRadioButton) component;
                ButtonModel model = radio.getModel();
                if (model.getGroup() instanceof ButtonGroup) { //changed to if
                    ButtonGroup group = model.getGroup();
                    if (foundGroup == null) {
                        foundGroup = group; //find the first group
                    } else if (foundGroup != group) {
                        return null; //not the same group.  error
                    }
                } else {
                    return null; // Radio button not in a group
                }
            }
        }
        return foundGroup;
    }

    // Method to check if the voter has voted
    private boolean hasVoted() {
        List<VoteRecord> votes = system.getVotes();
        if(votes == null){
            return false;
        }
        for (VoteRecord vote : votes) {
            if (vote.getVoterId().equals(voterId)) {
                return true;
            }
        }
        return false;
    }
}