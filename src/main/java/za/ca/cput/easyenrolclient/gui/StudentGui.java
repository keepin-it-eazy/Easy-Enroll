

package za.ca.cput.easyenrolclient.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ca.cput.easyenrolclient.Client;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.enrollment;

public class StudentGui extends JFrame {

    private JLabel lbl;
    private JTable table, tbl;
    private DefaultTableModel model, dfmodel;
    private JPanel pnl1, pnl2, pnl3, pnl4;
    private JButton enroll, Cancel, refreshBtn;
    private JTabbedPane tpane;
    private String studentEmail; // Changed to email to match enrollment table
    private ArrayList<Course> selectedCourses = new ArrayList<>();

    public StudentGui(String studentEmail) {
        this.studentEmail = studentEmail;

        initializeGUI();
        loadAvailableCourses();
    }

    private void initializeGUI() {
        pnl1 = new JPanel();
        lbl = new JLabel("EasyEnroll - Welcome, " + studentEmail);
        lbl.setFont(new Font("Ariel", Font.BOLD, 20));
        pnl1.add(lbl);

        tpane = new JTabbedPane();

        // Available Courses Panel
        pnl2 = new JPanel(new BorderLayout());
        String[] columns = {"Course Code", "Course Name"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        pnl2.add(new JLabel("Available Courses:"), BorderLayout.NORTH);
        pnl2.add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        refreshBtn = new JButton("Refresh Courses");
        refreshBtn.addActionListener(e -> loadAvailableCourses());
        pnl2.add(refreshBtn, BorderLayout.SOUTH);

        // My Courses Panel
        pnl3 = new JPanel(new BorderLayout());
        String[] myColumns = {"Course Code", "Course Name"};
        dfmodel = new DefaultTableModel(myColumns, 0);
        tbl = new JTable(dfmodel);
        JScrollPane myScrollPane = new JScrollPane(tbl);
        pnl3.add(new JLabel("My Selected Courses:"), BorderLayout.NORTH);
        pnl3.add(myScrollPane, BorderLayout.CENTER);

        tpane.addTab("Available Courses", pnl2);
        tpane.addTab("My Selected Courses", pnl3);

        // Buttons Panel
        pnl4 = new JPanel();
        enroll = new JButton("Enroll Selected");
        pnl4.add(enroll);
        Cancel = new JButton("Cancel");
        pnl4.add(Cancel);

        add(pnl1, BorderLayout.NORTH);
        add(tpane, BorderLayout.CENTER);
        add(pnl4, BorderLayout.SOUTH);

        setupEventHandlers();
    }

    private void loadAvailableCourses() {
        try {
            // Clear existing courses
            model.setRowCount(0);
            
            // Get courses from server via client
            ArrayList<Course> courses = Client.getAvailableCourses();
            
            // Populate table with courses
            for (Course course : courses) {
                model.addRow(new Object[]{
                    course.getCourseCode(),
                    course.getCourseName()
                });
            }
            
            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No courses available.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void setupEventHandlers() {
        enroll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enrollSelectedCourses();
            }
        });

        Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(StudentGui.this, 
                    "Are you sure you want to exit?", "Confirm Exit", 
                    JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void enrollSelectedCourses() {
        int[] selectedRows = table.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one course to enroll.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedCourses.clear();
        
        // Get selected courses
        for (int row : selectedRows) {
            String courseCode = table.getValueAt(row, 0).toString();
            String courseName = table.getValueAt(row, 1).toString();
            selectedCourses.add(new Course(courseCode, courseName));
            
            // Add to my courses table
            dfmodel.addRow(new Object[]{courseCode, courseName});
        }

        // Check for duplicates in selection
        if (hasDuplicateSelections()) {
            JOptionPane.showMessageDialog(this, "You have selected duplicate courses. Please check your selection.", "Duplicate Courses", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Send enrollment to server
        enrollment enrollRequest = new enrollment(studentEmail, selectedCourses);
        String response = Client.sendEnrollment(enrollRequest);
        
        if ("success".equalsIgnoreCase(response)) {
            JOptionPane.showMessageDialog(this, "Successfully enrolled in " + selectedCourses.size() + " course(s)!", "Enrollment Success", JOptionPane.INFORMATION_MESSAGE);
            clearSelection();
        } else if ("duplicate".equalsIgnoreCase(response)) {
            JOptionPane.showMessageDialog(this, "You are already enrolled in one or more of these courses.", "Duplicate Enrollment", JOptionPane.WARNING_MESSAGE);
        } else {
           // JOptionPane.showMessageDialog(this, "Enrollment failed. Please try again.", "Enrollment Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hasDuplicateSelections() {
        for (int i = 0; i < selectedCourses.size(); i++) {
            for (int j = i + 1; j < selectedCourses.size(); j++) {
                if (selectedCourses.get(i).getCourseCode().equals(selectedCourses.get(j).getCourseCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearSelection() {
        table.clearSelection();
        selectedCourses.clear();
    }

    public static void main(String[] args) {
        // For testing only - in real usage, studentEmail comes from login
        SwingUtilities.invokeLater(() -> {
            StudentGui mygui = new StudentGui("test@example.com");
            mygui.setTitle("EasyEnrol - Student Portal");
            mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mygui.setSize(800, 600);
            mygui.setLocationRelativeTo(null);
            mygui.setVisible(true);
        });
    }
}