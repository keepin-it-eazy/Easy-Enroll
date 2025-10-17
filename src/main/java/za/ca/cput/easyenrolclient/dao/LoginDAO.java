/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ca.cput.easyenrolclient.connection.DBConnection;


/**
 *
 * @author samuk
 */
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
    
    
    public String authenticate(int id, String password) {
        try {
            
            String sql = "SELECT studentId, StudentPassword FROM Student WHERE studentId = ? AND StudentPassword = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return "student"; 
                    }
                }
            }

            
            sql = "SELECT adminId, password FROM Admin WHERE adminId = ? AND password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return "admin"; 
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "invalid"; 
    }
}

    

