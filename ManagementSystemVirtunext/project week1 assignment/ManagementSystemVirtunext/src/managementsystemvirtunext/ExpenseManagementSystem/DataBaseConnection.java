/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managementsystemvirtunext.ExpenseManagementSystem;

/**
 *
 * @author Admin
 */
import java.sql.*;
 import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DataBaseConnection {
  

    private static final String URL = "jdbc:mysql://localhost:3306/virtuNext";
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
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        conn.setAutoCommit(true); // Ensure auto-commit is enabled
        return conn;
    }
    
    public static void main(String[] args) {
        try{
             getConnection();
        
        }
        catch(SQLException e){
            System.out.println("connection failed"+e);
        }
       
    }

}

    

