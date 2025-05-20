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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.HashMap;
 import java.util.List;

public class ResultPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private VotingSystem system;
    private JTextArea resultArea;

    public ResultPanel(VotingSystem system) {
        this.system = system;
        setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh Results");
        refreshBtn.addActionListener(e -> refreshResults());
        add(refreshBtn, BorderLayout.SOUTH);

        refreshResults();
    }

    private void refreshResults() {
        try {
            Map<String, Integer> results = getVoteResults(); //changed to method
            Map<String, Candidate> candidates = system.getCandidates();
            //Map<String, String> parties = system.getParties(); //No getParties() method

            StringBuilder sb = new StringBuilder();
            sb.append("ELECTION RESULTS\n");
            sb.append("================\n\n");

            if (results.isEmpty()) {
                sb.append("No votes have been cast yet.\n");
            } else {
                sb.append(String.format("%-20s %-15s %-10s\n", "Candidate", "Party", "Votes"));
                sb.append(String.format("%-20s %-15s %-10s\n", "---------", "-----", "-----"));

                for (Map.Entry<String, Integer> entry : results.entrySet()) {
                    Candidate c = candidates.get(entry.getKey());
                    String partyName = (c != null) ? c.getParty() : "N/A"; //handle null candidate
                    sb.append(String.format("%-20s %-15s %-10d\n",
                            c.getName(), partyName, entry.getValue()));
                }

                int totalVotes = results.values().stream().mapToInt(Integer::intValue).sum();
                sb.append("\nTotal votes cast: ").append(totalVotes).append("\n");
            }

            resultArea.setText(sb.toString());
        } catch (Exception ex) { //changed to Exception
            resultArea.setText("Error loading results: " + ex.getMessage());
        }
    }

    private Map<String, Integer> getVoteResults() {
        List<VoteRecord> votes = system.getVotes();
        Map<String, Integer> results = new HashMap<>();

        if (votes != null) {  //null check
            for (VoteRecord vote : votes) {
                String candidateId = vote.getCandidateId();
                results.put(candidateId, results.getOrDefault(candidateId, 0) + 1);
            }
        }
        return results;
    }
}