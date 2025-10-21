package za.ca.cput.easyenrolclient.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ca.cput.easyenrolclient.Client;
import static za.ca.cput.easyenrolclient.Client.sendStudent;
import static za.ca.cput.easyenrolclient.Client.sendCourse;
import za.ca.cput.easyenrolclient.dao.*;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.Student;
import za.ca.cput.easyenrolclient.domain.enrollment;

public class AdminGui extends JFrame implements ActionListener {

    private JTabbedPane tabbedPane;
    private JLabel lblStudName, lblStudID, lblEmail, lblPassword, lblCourseID, lblCourseCode, lblCourseName, lblSearchStudentID;
    private JTextField txtStudName, txtStudID, txtEmail, txtPassword, txtCourseID, txtCourseCode, txtCourseName;
    private JButton btnAddStudent, btnAddCourse, btnExitStudent, btnExitCourse, btnSearchStudent, btnSearchCourse;
    private StudentDAO sdao;
    private CourseDAO cdao;

    // New components for enrollment tabs
    private JTextField txtSearchStudentID, txtSearchCourseID;
    private JTable tblStudentResults, tblCourseResults;
    private DefaultTableModel studentTableModel, courseTableModel;

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

        lblSearchStudentID = new JLabel("Enter Student ID:");
        txtSearchStudentID = new JTextField(10);
        btnSearchStudent = new JButton("Search");
        studentTableModel = new DefaultTableModel(new Object[]{"Course Code", "Course Name"}, 0);
        tblStudentResults = new JTable(studentTableModel);
        JScrollPane scrollStudentTable = new JScrollPane(tblStudentResults);

        JPanel pnlSearchStudent = new JPanel(new BorderLayout());
        JPanel pnlStudentSearchTop = new JPanel();
        pnlStudentSearchTop.add(lblSearchStudentID);
        pnlStudentSearchTop.add(txtSearchStudentID);
        pnlStudentSearchTop.add(btnSearchStudent);

        pnlSearchStudent.add(pnlStudentSearchTop, BorderLayout.NORTH);
        pnlSearchStudent.add(scrollStudentTable, BorderLayout.CENTER);
        btnSearchStudent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = txtSearchStudentID.getText().trim();
                if (!id.isEmpty()) {
                    try {
                        int studentId = Integer.parseInt(id);
                        populateStudentEnrollmentTable(studentId);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Student ID must be a number");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid student id");
                }
            }

        });

        JLabel lblSearchCourseID = new JLabel("Enter Course ID:");
        txtSearchCourseID = new JTextField(10);
        btnSearchCourse = new JButton("Search");
        courseTableModel = new DefaultTableModel(new Object[]{"Student ID", "Student Name"}, 0);
        tblCourseResults = new JTable(courseTableModel);
        JScrollPane scrollCourseTable = new JScrollPane(tblCourseResults);
        JPanel pnlSearchCourse = new JPanel(new BorderLayout());
        JPanel pnlCourseSearchTop = new JPanel();
        pnlCourseSearchTop.add(lblSearchCourseID);
        pnlCourseSearchTop.add(txtSearchCourseID);
        pnlCourseSearchTop.add(btnSearchCourse);

        pnlSearchCourse.add(pnlCourseSearchTop, BorderLayout.NORTH);
        pnlSearchCourse.add(scrollCourseTable, BorderLayout.CENTER);

        tabbedPane.add("Students", pnlStudent);
        tabbedPane.add("Courses", pnlCourse);
        tabbedPane.add("Student Enrollments", pnlSearchStudent);
        tabbedPane.add("Course Enrollments", pnlSearchCourse);

        this.add(pnlNorth, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);

        btnSearchCourse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String courseCode = txtSearchCourseID.getText().trim();
                if (courseCode.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a course code.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                ArrayList<Student> students = Client.getStudentsByCourse(courseCode);

                courseTableModel.setRowCount(0);

                for (Student s : students) {
                    courseTableModel.addRow(new Object[]{
                        s.getStudentId(),
                        s.getName()
                    });
                }

                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No students found for course: " + courseCode, "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

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

        if (studentID.isEmpty() || studentName.isEmpty() || studentEmail.isEmpty() || studentPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int sID = Integer.parseInt(studentID);
            Student s = new Student(sID, studentName, studentEmail, studentPassword);
            String response = sendStudent(s);
            if (response.equalsIgnoreCase("success")) {
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (response.equalsIgnoreCase("duplicate")) {
                JOptionPane.showMessageDialog(this, "Student already exists", "Failed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Unsuccessful", "Failed", JOptionPane.INFORMATION_MESSAGE);
            }
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

        if (courseID.isEmpty() || courseCode.isEmpty() || courseName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cID = Integer.parseInt(courseID);
            Course c = new Course(cID, courseCode, courseName);
            String response = sendCourse(c);

            if (response.equalsIgnoreCase("success")) {
                JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (response.equalsIgnoreCase("duplicate")) {
                JOptionPane.showMessageDialog(null, "Course already exists!");
            } else {
                JOptionPane.showMessageDialog(this, "Unsuccessful", "Failed", JOptionPane.INFORMATION_MESSAGE);
            }
            clearCourseFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Course ID must be a valid number!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding course: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearStudentFields() {
        txtStudID.setText("");
        txtStudName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    private void clearCourseFields() {
        txtCourseID.setText("");
        txtCourseCode.setText("");
        txtCourseName.setText("");
    }

    private void populateStudentEnrollmentTable(int studentId) {
        try {

            studentTableModel.setRowCount(0);

            enrollment e = Client.getEnrollmentById(studentId);

            for (Course c : e.getCourses()) {
                studentTableModel.addRow(new Object[]{c.getCourseCode(), c.getCourseName()});
            }

            if (e.getCourses().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No courses found for student ID " + studentId,
                        "No Data", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error retrieving student enrollment: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AdminGui();
    }
}
