
package za.ca.cput.easyenrolclient.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import za.ca.cput.easyenrolclient.connection.DBConnection;

public class CreateTables {
    private Connection con;
    
    public CreateTables() {
        try {
            con = DBConnection.derbyConnection();
            dropExistingTables(); // Drop tables first
            createTables(); // Then create new ones
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + exception.getMessage());
        }
    }
   
    private void dropExistingTables() {
        try (Statement stmt = con.createStatement()) {
            // Drop tables in correct order to handle foreign key constraints
            String[] tables = {"enrollment", "student", "course", "admin"};
            
            for (String table : tables) {
                try {
                    stmt.execute("DROP TABLE " + table);
                    System.out.println("Dropped table: " + table);
                } catch (SQLException e) {
                    // Table might not exist, which is fine
                    System.out.println("Table " + table + " doesn't exist or couldn't be dropped: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error dropping tables: " + e.getMessage());
        }
    }
    
    private void createTables() {
        try (Statement stmt = con.createStatement()) {
            
            // Create Student table
            stmt.execute("CREATE TABLE student (" +
                "student_id INT PRIMARY KEY, " +
                "student_name VARCHAR(100) NOT NULL, " +
                "student_email VARCHAR(100) NOT NULL UNIQUE, " +
                "student_password VARCHAR(50) NOT NULL)");
           
            // Create Course table
            stmt.execute("CREATE TABLE course (" +
                "course_id INT PRIMARY KEY, " +
                "course_code VARCHAR(20) NOT NULL UNIQUE, " +
                "course_name VARCHAR(100) NOT NULL)");
           
            // Create Enrollment table
            stmt.execute("CREATE TABLE enrollment (" +
                "student_email VARCHAR(100), " +
                "course_code VARCHAR(20), " +
                "course_name VARCHAR(100), " +
                "PRIMARY KEY (student_email, course_code), " +
                "FOREIGN KEY (student_email) REFERENCES student(student_email), " +
                "FOREIGN KEY (course_code) REFERENCES course(course_code))");
            
            // Create Admin table
            stmt.execute("CREATE TABLE admin (" +
                "admin_id INT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE, " +
                "password VARCHAR(50) NOT NULL)");
            
            // Add default admin user
            stmt.execute("INSERT INTO admin VALUES (1, 'System Administrator', 'admin', 'admin123')");
            
            // Add sample students
            stmt.execute("INSERT INTO student VALUES (1, 'John Doe', 'student@example.com', 'password123')");
            stmt.execute("INSERT INTO student VALUES (2, 'Jane Smith', 'jane.smith@example.com', 'pass123')");
            
            // Add sample courses
            stmt.execute("INSERT INTO course VALUES (1, 'ADP262S', 'Applications Development Practice')");
            stmt.execute("INSERT INTO course VALUES (2, 'ISA262S', 'Information Systems Analysis')");
            stmt.execute("INSERT INTO course VALUES (3, 'MAF152S', 'Multimedia Applications Fundamentals')");
            stmt.execute("INSERT INTO course VALUES (4, 'ADF262S', 'Applications Development Fundamentals')");
            stmt.execute("INSERT INTO course VALUES (5, 'ICE262S', 'Artificial Intelligence')");
            stmt.execute("INSERT INTO course VALUES (6, 'CNF262S', 'Communications Networks Fundamentals')");
            stmt.execute("INSERT INTO course VALUES (7, 'PRT262S', 'Project')");
            stmt.execute("INSERT INTO course VALUES (8, 'INM262S', 'Information Management')");
            stmt.execute("INSERT INTO course VALUES (9, 'PRC262S', 'Professionals Communications')");
            
            System.out.println("Database tables created and populated successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void initializeDatabase() {
        new CreateTables();
    }
    
    public static void main(String[] args) {
        System.out.println("Initializing database...");
        initializeDatabase();
        System.out.println("Database initialization complete!");
    }
}