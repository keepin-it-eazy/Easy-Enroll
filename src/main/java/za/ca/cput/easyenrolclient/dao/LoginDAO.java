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
    
    
    public String authenticate(String username, String password) {
        try {
            
            String sql = "SELECT studentId, password FROM Student WHERE studentId = ? AND password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return "student"; 
                    }
                }
            }

            
            sql = "SELECT email, password FROM Admin WHERE email = ? AND password = ?";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
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
        }
        //if there's no credentials in db then just return invalid
        return "invalid"; 
    }
}

    

