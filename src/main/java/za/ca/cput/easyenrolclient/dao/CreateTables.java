
package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import za.ca.cput.easyenrolclient.connection.DBConnection;


public class CreateTables {
    private Connection con;
    
    public CreateTables() {
        try {
            con = DBConnection.derbyConnection();
            createTables(); // Call createTables in constructor
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + exception.getMessage());
        }
    }
   
    private void createTables() {
        try (Statement stmt = con.createStatement()) {
            // Creating the Students table
            stmt.execute("CREATE TABLE student (" +
                "student_id INT PRIMARY KEY, " +
                "student_name VARCHAR(100) NOT NULL, " +
                "student_email VARCHAR(30) NOT NULL, "+"student_password VARCHAR(10))");
            
            // Creating the Courses table
            stmt.execute("CREATE TABLE course (" +
                "course_id INT PRIMARY KEY, " +
                "course_code VARCHAR(10) NOT NULL, " +
                "course_name VARCHAR(50))");
           
//            // Creating the Enrollments table
//            stmt.execute("CREATE TABLE enrollments (" +
//                "student_number VARCHAR(20), " +
//                "course_code VARCHAR(20), " +
//                "enrollment_date DATE DEFAULT CURRENT_DATE, " +
//                "PRIMARY KEY (student_number, course_code))");
            
//            // Creating the Users table
//            stmt.execute("CREATE TABLE users (" +
//                "username VARCHAR(50) PRIMARY KEY, " +
//                "password VARCHAR(100) NOT NULL, " +
//                "role VARCHAR(20) NOT NULL, " +
//                "student_number VARCHAR(20))");
            
            // Adding a default admin
            stmt.execute("INSERT INTO users VALUES ('admin', 'admin123', 'admin', NULL)");
            
            System.out.println("Database tables created successfully!");
            
        } catch (SQLException e) {
            // Tables already exist - this is normal after first run
            System.out.println("Tables already exist or error: " + e.getMessage());
        }
    }
    
    // Optional: Method to manually trigger table creation
    public static void initializeDatabase() {
        new CreateTables();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
        new CreateTables();
    });
    }
}