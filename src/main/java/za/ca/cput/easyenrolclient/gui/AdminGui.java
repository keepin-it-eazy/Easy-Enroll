package za.ca.cput.easyenrolclient.gui;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author keepingiteazy
 */
public class AdminGui extends JFrame{
    private JTabbedPane tabbedPane;
    private JLabel lblStudName, lblStudNumber, lblCourseCode, lblCourseName;
    private JTextField txtStudName, txtStudNumber, txtCourseCode, txtCourseName;
    
    public AdminGui(){
        this.setTitle("Administration Control");
        this.setSize(1080,2400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        
        
        tabbedPane= new JTabbedPane();
        tabbedPane.setBounds(50, 50, 400, 400);
        
        lblStudName= new JLabel("Student Name: ");
        lblStudNumber= new JLabel("Student Number: ");
        
        txtStudName= new JTextField();
        txtStudNumber= new JTextField();
        
        JPanel pnlStudent= new JPanel();
        pnlStudent.setLayout(new GridLayout(2,2));
        pnlStudent.add(lblStudName);
         pnlStudent.add(txtStudName);
          pnlStudent.add(lblStudNumber);
           pnlStudent.add(txtStudNumber);
        
         lblCourseCode= new JLabel("Course Code: ");
        lblCourseName= new JLabel("Course Name: ");
        
        txtCourseCode= new JTextField();
        txtCourseName= new JTextField();
           
           
        JPanel pnlCourse= new JPanel();
         pnlCourse.setLayout(new GridLayout(2,2));
        pnlCourse.add(lblCourseCode);
         pnlCourse.add(txtCourseCode);
          pnlCourse.add(lblCourseName);
           pnlCourse.add(txtCourseName);
        
        tabbedPane.add("Students", pnlStudent);
        tabbedPane.add("Courses", pnlCourse);
        
        this.add(tabbedPane);
        
        this.pack();
        this.setVisible(true);
        
    }
    
    public static void main(String[] args) {
        new AdminGui();
    }
}
