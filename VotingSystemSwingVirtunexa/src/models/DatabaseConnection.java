/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Admin
 */


import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/voting_system";
    private static final String USER = "root";
    private static final String PASSWORD = "vinay@492003";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create tables if they don't exist
            stmt.execute("CREATE TABLE IF NOT EXISTS parties (" +
                         "id VARCHAR(10) PRIMARY KEY, " +
                         "name VARCHAR(100) NOT NULL)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS candidates (" +
                         "id VARCHAR(10) PRIMARY KEY, " +
                         "name VARCHAR(100) NOT NULL, " +
                         "party_id VARCHAR(10), " +
                         "FOREIGN KEY (party_id) REFERENCES parties(id))");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS voters (" +
                         "id VARCHAR(10) PRIMARY KEY, " +
                         "name VARCHAR(100) NOT NULL, " +
                         "has_voted BOOLEAN DEFAULT FALSE)");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS votes (" +
                         "id INT AUTO_INCREMENT PRIMARY KEY, " +
                         "voter_id VARCHAR(10), " +
                         "candidate_id VARCHAR(10), " +
                         "FOREIGN KEY (voter_id) REFERENCES voters(id), " +
                         "FOREIGN KEY (candidate_id) REFERENCES candidates(id))");
            
            stmt.execute("CREATE TABLE IF NOT EXISTS election_status (" +
                         "id INT PRIMARY KEY DEFAULT 1, " +
                         "is_active BOOLEAN DEFAULT FALSE, " +
                         "CONSTRAINT single_row CHECK (id = 1))");
            
            // Initialize election status if not exists
            stmt.execute("INSERT IGNORE INTO election_status (id, is_active) VALUES (1, FALSE)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}