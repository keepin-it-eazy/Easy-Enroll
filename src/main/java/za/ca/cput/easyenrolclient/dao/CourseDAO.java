
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
        try{
           this.con= DBConnection.derbyConnection();
        }catch(SQLException sqle){
            JOptionPane.showInternalMessageDialog(null, "SQL Exception "+ sqle.getMessage());
        }
    }
    
    public void addCourse(Course course){
        String sql= "INSERT INTO course(course_id, course_code, course_name) VALUES (?,?,?)";

          try{  pstmt= con.prepareStatement(sql);
                pstmt.setInt(1, course.getCourseId());
                pstmt.setString(2, course.getCourseCode());
                pstmt.setString(3, course.getCourseName());
                
           int ok = pstmt.executeUpdate();  

            if (ok > 0) {
                JOptionPane.showMessageDialog(null, "Course successfully saved.");
            } else {
                JOptionPane.showMessageDialog(null, "Could not save the course.");
            }            
          }catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "SQL Error" + sqlException.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "SQL Error" + e.getMessage());
        }finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch(Exception exception) {
               
            }
        }
          
          
        
    }
    
    
public ArrayList<Course> getAllCourses() {
    ArrayList<Course> courses = new ArrayList<>();
    String sql = "SELECT course_id, course_code, course_name FROM course ORDER BY course_code";
    
    try { 
        
        pstmt = con.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            int courseId = rs.getInt("course_id");
            String courseCode = rs.getString("course_code");
            String courseName = rs.getString("course_name");
            
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
            if (con != null) con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing connection: " + e.getMessage());
        }
        System.exit(0);
    }
    
}
