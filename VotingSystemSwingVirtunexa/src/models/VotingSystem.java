/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */
import java.io.Serializable;
import java.util.*;

public class VotingSystem implements Serializable {
    private Map<String, Candidate> candidates = new HashMap<>();
    private Map<String, Voter> voters = new HashMap<>();
    private List<VoteRecord> votes = new ArrayList<>();
    private boolean electionStarted = false;

    public void addCandidate(String id, String name, String party) {
        candidates.put(id, new Candidate(id, name, party));
    }

    public void registerVoter(String id, String name) {
        voters.put(id, new Voter(id, name));
    }

    public void castVote(String voterId, String candidateId) {
        if (electionStarted && voters.containsKey(voterId) && candidates.containsKey(candidateId)) {
            votes.add(new VoteRecord(voterId, candidateId));
        }
    }

    public void startElection() {
        electionStarted = true;
    }

    public void endElection() {
        electionStarted = false;
    }

    public Map<String, Candidate> getCandidates() {
        return candidates;
    }

    public Map<String, Voter> getVoters() {
        return voters;
    }

    public List<VoteRecord> getVotes() {
        return votes;
    }

    public boolean isElectionStarted() {
        return electionStarted;
    }
}