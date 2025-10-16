package za.ca.cput.easyenrolclient.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ca.cput.easyenrolclient.Client;
import za.ca.cput.easyenrolclient.dao.enrollDao;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.enrollment;

/**
 *
 * @author 240971051
 */
public class StudentGui extends JFrame {

    private JLabel lbl, lbl2, lbl3;
    private JTable table, tbl;
    private DefaultTableModel model, dfmodel;
    private JPanel pnl1, pnl2, pnl3, pnl4;
    private JButton enroll, Cancel;
    private JTabbedPane tpane;
    private static String studentId;
    enrollDao dao = new enrollDao();
    private Socket server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<Course> selectedCourses = new ArrayList<>();
    private ArrayList<Course> duplicates = new ArrayList<>();

    public StudentGui(String studentId) {
        this.studentId = studentId;

        pnl1 = new JPanel();
        lbl = new JLabel("EasyEnroll");
        lbl.setFont(new Font("Ariel", Font.BOLD, 24));
        pnl1.add(lbl);

        tpane = new JTabbedPane();
        //  lbl2 = new JLabel("Available courses");
        pnl2 = new JPanel();
        // pnl2.add(lbl2);
        String[] Column = {" Code", "Subject Name"};
        String[][] subjects = {{"ISA262S", "Information Systems Analysis"},
        {"MAF152S", "Multimedia Applications Fundamentals"},
        {"ADP262S", "Applications Development Practice"},
        {"ADF262S", "Applications Development Fundamentals"},
        {"ICE262S", "Artificial Intelligence"},
        {"CNF262S", "Communications Networks Fundamentals"},
        {"PRT262S", "Project"},
        {"INM262S", "Information Management"},
        {"PRC262S", "Professionals Communications"}};

        model = new DefaultTableModel(subjects, Column);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        pnl2.add(scrollPane);

        pnl3 = new JPanel();

        pnl3 = new JPanel();
        String[] myColumnNames = {"Subject Code", "Subject name"};
        String[][] mySubjects = {{}};
        dfmodel = new DefaultTableModel(myColumnNames, 0);
        tbl = new JTable(dfmodel);
        JScrollPane scrllpane = new JScrollPane(tbl);
        pnl3.add(scrllpane);

        tpane.addTab("my Courses", pnl3);
        tpane.addTab("Available courses", pnl2);
        tpane.addTab("my Courses", pnl3);

        pnl4 = new JPanel();
        enroll = new JButton("Enroll");
        pnl4.add(enroll);
        Cancel = new JButton("Cancel");
        pnl4.add(Cancel);

        add(pnl1, BorderLayout.NORTH);
        add(pnl4, BorderLayout.SOUTH);
        add(tpane);

        enroll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                int[] selectedRows = table.getSelectedRows();

                

                for (int row : selectedRows) {

                    String courseId = table.getValueAt(row, 0).toString();
                    String courseName = table.getValueAt(row, 1).toString();
                    selectedCourses.add(new Course(courseId, courseName));
                    dfmodel.addRow(new Object[]{courseId, courseName});
                }
                boolean hasDuplicates = false;
                
                for (int i = 0; i < selectedCourses.size(); i++) {
                    for (int j = i + 1; j < selectedCourses.size(); j++) {
                        if (selectedCourses.get(i).getCourseCode().equals(selectedCourses.get(j).getCourseCode()) && !duplicates.contains(selectedCourses.get(i))) {
                            hasDuplicates = true;
                            duplicates.add(selectedCourses.get(i));
                            break;
                        }
                    }
                }
                System.out.println(duplicates.toString());
                if (hasDuplicates) {
                    JOptionPane.showMessageDialog(null, "You have already chosen this course");
                    return;
                } else {
                    enrollment enroll = new enrollment(studentId, selectedCourses);
                    String response = Client.sendEnrollment(enroll);
                    if (response.equalsIgnoreCase("success")) {
                        JOptionPane.showMessageDialog(null, "Successfully enrolled");
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "Enrollment unsuccessful");
                    }
                }

            }

        });
        Cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == Cancel) {
                    System.exit(0);
                }

            }
        });

    }

    public static void main(String[] args) {
        StudentGui mygui = new StudentGui(studentId);
        mygui.setTitle("EasyEnrol");
        mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mygui.setSize(700, 600);
        mygui.setVisible(true);

    }
}
