package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        }  catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    public void addStudent(Student student) {
        String sql = "INSERT INTO student(STUDENT_ID, STUDENT_NAME, STUDENT_EMAIL, STUDENT_PASSWORD) VALUES (?,?,?,?)";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPassword());

            int ok = pstmt.executeUpdate();

            if (ok > 0) {
                JOptionPane.showMessageDialog(null, "Student successfully saved.");
            } else {
                JOptionPane.showMessageDialog(null, "Could not save the student.");
            }
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "SQL Error" + sqlException.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "SQL Error" + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception exception) {

            }
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
