
package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ca.cput.easyenrolclient.connection.DBConnection;

public class LoginDAO {
    
    private Connection con;
    private ResultSet rs;
    private PreparedStatement pstmt;

    public LoginDAO() {
        try {
            this.con = DBConnection.derbyConnection();
        } catch (SQLException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String authenticate(String username, String password) {
        try {
            // Check Student table - using correct column names
            String studentSql = "SELECT student_email, student_password FROM Student WHERE student_email = ? AND student_password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(studentSql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return "student"; 
                    }
                }
            }

            // Check Admin table - using correct column names  
            String adminSql = "SELECT email, password FROM Admin WHERE email = ? AND password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(adminSql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return "admin"; 
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "error"; // Return error instead of invalid for SQL issues
        }
        
        return "invalid"; 
    }
    
    // Close connection method
    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

    

