/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import za.ca.cput.easyenrolclient.connection.DBConnection;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.enrollment;
/**
 *
 * @author 240971051
 */
public class enrollDao {
    
    private Connection con;
    private ResultSet rs;
    private PreparedStatement pstmt;
    
    
       public enrollDao(){
       
         try {
            this.con = DBConnection.derbyConnection();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());

        }
         
         
       
       }
               
      public String Enrollment(enrollment enroll) {
        String query = "INSERT INTO ENROLLMENT ( STUDENTID, COURSECODE, COURSENAME) VALUES ( ?,?,?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            ArrayList<Course> courses = enroll.getCourses();
            for ( Course course : courses) {
                pstmt.setString(1, enroll.getStudentid());
                pstmt.setString(2, course.getCourseCode());
                pstmt.setString(3, course.getCourseName());
                pstmt.addBatch();
                
            }
            int i[] = pstmt.executeBatch();
            for (int arr : i){
                if (arr <= 0){
                    return "failed";
                }
            }
            return "success";
            
        } catch (SQLException ex) {
            Logger.getLogger(enrollDao.class.getName()).log(Level.SEVERE, null, ex);
            return "failed";
        }
        

    }    
               
}
