package za.ca.cput.easyenrolclient.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import za.ca.cput.easyenrolclient.dao.*;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.Student;

/**
 *
 * @author keepingiteazy
 */
public class AdminGui extends JFrame implements ActionListener {

    private JTabbedPane tabbedPane;
    private JLabel lblStudName, lblStudID, lblEmail, lblPassword, lblCourseID, lblCourseCode, lblCourseName;
    private JTextField txtStudName, txtStudID, txtEmail, txtPassword, txtCourseID, txtCourseCode, txtCourseName;
    private JButton btnAddStudent, btnAddCourse, btnExitStudent, btnExitCourse;
    private StudentDAO sdao;
    private CourseDAO cdao;
    

    public AdminGui() {
     
        sdao = new StudentDAO();
        cdao = new CourseDAO();
        
        this.setTitle("Easy Enrol");
        this.setPreferredSize(new Dimension(600, 400)); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);

        JPanel pnlNorth = new JPanel();
        JLabel lblHead = new JLabel("Admin Control");
        lblHead.setFont(new Font("Arial", Font.BOLD, 24)); 
        pnlNorth.add(lblHead);

        tabbedPane = new JTabbedPane();

       
        lblStudID = new JLabel("Student ID: ");
        lblStudName = new JLabel("Student Name: ");
        lblEmail = new JLabel("Email: ");
        lblPassword = new JLabel("Password: ");

        txtStudID = new JTextField(10);
        txtStudName = new JTextField(20); 
        txtEmail = new JTextField(20);
        txtPassword = new JTextField(20);

        btnAddStudent = new JButton("Add Student");
        btnExitStudent = new JButton("Exit");

        JPanel pnlStudent = new JPanel(new BorderLayout());
        JPanel pnlStudentFields = new JPanel(new GridLayout(4, 2, 5, 5)); 
        pnlStudentFields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pnlStudentFields.add(lblStudID);
        pnlStudentFields.add(txtStudID);
        pnlStudentFields.add(lblStudName);
        pnlStudentFields.add(txtStudName);
        pnlStudentFields.add(lblEmail);
        pnlStudentFields.add(txtEmail);
        pnlStudentFields.add(lblPassword);
        pnlStudentFields.add(txtPassword);

        JPanel pnlStudentButtons = new JPanel(new FlowLayout());
        pnlStudentButtons.add(btnAddStudent);
        pnlStudentButtons.add(btnExitStudent);

        pnlStudent.add(pnlStudentFields, BorderLayout.CENTER);
        pnlStudent.add(pnlStudentButtons, BorderLayout.SOUTH);

        
        lblCourseID = new JLabel("Course ID: ");
        lblCourseCode = new JLabel("Course Code: ");
        lblCourseName = new JLabel("Course Name: ");

        txtCourseID = new JTextField(10);
        txtCourseCode = new JTextField(10);
        txtCourseName = new JTextField(20);

        btnAddCourse = new JButton("Add Course");
        btnExitCourse = new JButton("Exit");

        JPanel pnlCourse = new JPanel(new BorderLayout());
        JPanel pnlCourseFields = new JPanel(new GridLayout(3, 2, 5, 5)); 
        pnlCourseFields.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pnlCourseFields.add(lblCourseID);
        pnlCourseFields.add(txtCourseID);
        pnlCourseFields.add(lblCourseCode);
        pnlCourseFields.add(txtCourseCode);
        pnlCourseFields.add(lblCourseName);
        pnlCourseFields.add(txtCourseName);

        JPanel pnlCourseButtons = new JPanel(new FlowLayout());
        pnlCourseButtons.add(btnAddCourse);
        pnlCourseButtons.add(btnExitCourse);

        pnlCourse.add(pnlCourseFields, BorderLayout.CENTER);
        pnlCourse.add(pnlCourseButtons, BorderLayout.SOUTH);

        tabbedPane.add("Students", pnlStudent);
        tabbedPane.add("Courses", pnlCourse);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);

        // Add action listeners
        btnAddStudent.addActionListener(this);
        btnExitStudent.addActionListener(this);
        btnAddCourse.addActionListener(this);
        btnExitCourse.addActionListener(this);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddStudent) {
            addStudent();
        } else if (e.getSource() == btnAddCourse) {
            addCourse();
        } else if (e.getSource() == btnExitStudent || e.getSource() == btnExitCourse) {
            int response = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", "Confirm Exit", 
                JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    private void addStudent() {
        
        String studentID = txtStudID.getText().trim();
        String studentName = txtStudName.getText().trim();
        String studentEmail = txtEmail.getText().trim();
        String studentPassword = txtPassword.getText().trim();

        // Validation for field entries
        if (studentID.isEmpty() || studentName.isEmpty() || studentEmail.isEmpty() || studentPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int sID = Integer.parseInt(studentID);
            Student s = new Student(sID, studentName, studentEmail, studentPassword);
            sdao.addStudent(s);
            
            //JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearStudentFields();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Student ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addCourse() {
       String courseID = txtCourseID.getText().trim();
        String courseCode = txtCourseCode.getText().trim();
        String courseName = txtCourseName.getText().trim();

        // Validation for field entries
        if (courseID.isEmpty() || courseCode.isEmpty() || courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cID = Integer.parseInt(courseID);
            Course c = new Course(cID, courseCode, courseName);
            cdao.addCourse(c);
            
            //JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearCourseFields();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Student ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearStudentFields() {
        txtStudID.setText("");
        txtStudName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }
    
    private void clearCourseFields() {
        txtStudID.setText("");
        txtStudName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    public static void main(String[] args) {
        
               new AdminGui();
     
    }
}
