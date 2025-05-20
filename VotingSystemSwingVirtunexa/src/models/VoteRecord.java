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

public class VoteRecord implements Serializable {
   private String voterId;
   private String candidateId;

   public VoteRecord(String var1, String var2) {
      this.voterId = var1;
      this.candidateId = var2;
   }

   public String getVoterId() {
      return this.voterId;
   }

   public String getCandidateId() {
      return this.candidateId;
   }
}
