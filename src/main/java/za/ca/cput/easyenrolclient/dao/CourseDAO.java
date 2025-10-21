package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import za.ca.cput.easyenrolclient.connection.DBConnection;
import za.ca.cput.easyenrolclient.domain.Course;

/**
 *
 *
 */
public class CourseDAO {

    private Connection con;
    private Statement stmt;
    private PreparedStatement pstmt;

    public CourseDAO() {
        try {
            this.con = DBConnection.derbyConnection();
        } catch (SQLException sqle) {
            JOptionPane.showInternalMessageDialog(null, "SQL Exception " + sqle.getMessage());
        }
    }

    public String addCourse(Course course) {
        String checkSql = "SELECT * FROM course WHERE courseCode = ?";
        String insertSql = "INSERT INTO course(courseid, courseCode, courseName) VALUES (?,?,?)";

        try (
                PreparedStatement checkStmt = con.prepareStatement(checkSql)) {

            checkStmt.setString(1, course.getCourseCode());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return "duplicate";
                }
            }

            try (PreparedStatement pstmt = con.prepareStatement(insertSql)) {
                pstmt.setInt(1, course.getCourseId());
                pstmt.setString(2, course.getCourseCode());
                pstmt.setString(3, course.getCourseName());

                int ok = pstmt.executeUpdate();
                if (ok > 0) {
                    return "success";
                } else {
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

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        String sql = "SELECT courseId, courseCode, courseName FROM course ORDER BY courseCode";

        try (PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int courseId = rs.getInt("courseId");
                String courseCode = rs.getString("courseCode");
                String courseName = rs.getString("courseName");

                courses.add(new Course(courseId, courseCode, courseName));
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
            e.printStackTrace();
        }

        return courses;
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
