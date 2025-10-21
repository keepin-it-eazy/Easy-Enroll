package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import za.ca.cput.easyenrolclient.connection.DBConnection;

import za.ca.cput.easyenrolclient.domain.Student;

/**
 *
 *
 */
public class StudentDAO {

    private Connection con;
    private Statement stmt;
    private PreparedStatement pstmt;

    public StudentDAO() {
        try {
            con = DBConnection.derbyConnection();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    public String addStudent(Student student) {
        String checkSql = "SELECT * FROM student WHERE STUDENTID = ? OR STUDENTEMAIL = ?";
        String insertSql = "INSERT INTO student(STUDENTID, STUDENTNAME, STUDENTEMAIL, STUDENTPASSWORD) VALUES (?,?,?,?)";

        try (
                PreparedStatement checkStmt = con.prepareStatement(checkSql)) {

            checkStmt.setInt(1, student.getStudentId());
            checkStmt.setString(2, student.getEmail());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return "duplicate";
                }
            }

            try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                pstmt.setInt(1, student.getStudentId());
                pstmt.setString(2, student.getName());
                pstmt.setString(3, student.getEmail());
                pstmt.setString(4, student.getPassword());

                int ok = pstmt.executeUpdate();

                if (ok > 0) {
                    JOptionPane.showMessageDialog(null, "Student successfully saved.");
                    return "success";
                } else {
                    JOptionPane.showMessageDialog(null, "Could not save the student.");
                    return "failed";
                }
            }

        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + sqlException.getMessage());
            return "error";
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return "error";
        }
    }

    public void closeResources() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing connection: " + e.getMessage());
        }
        System.exit(0);
    }
}
