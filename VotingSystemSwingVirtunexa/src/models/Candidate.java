package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
import java.io.Serializable;

public class Candidate implements Serializable {
    private String id;
    private String name;
    private String party;

    public Candidate(String id, String name, String party) {
        this.id = id;
        this.name = name;
        this.party = party;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getParty() { return party; }
}
