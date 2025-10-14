
package za.ca.cput.easyenrolclient.gui;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author 240971051
 */
public class StudentGui extends JFrame {
    private JLabel lbl ,lbl2,lbl3;
    private JTable table;
    private DefaultTableModel model;
    private JPanel pnl1 , pnl2,pnl3,pnl4;
    private JButton enroll, Cancel;
    private JTabbedPane tpane ;
    
    
    public StudentGui(){    
     super();
    pnl1 = new JPanel();
    lbl = new JLabel("EasyEnroll");
    lbl.setFont(new Font("Ariel", Font.BOLD, 24));
    pnl1.add(lbl);
    
    
    tpane = new JTabbedPane();
   //  lbl2 = new JLabel("Available courses");
     pnl2 = new JPanel();
    // pnl2.add(lbl2);
    String[] Column = {" Code", "Subject Name"};
    String[][]subjects = {{"ISA262S", "Information Systems Analysis"},
                           {"MAF152S", "Multimedia Applications Fundamentals"},
                           {"ADP262S" , "Applications Development Practice"},
                           {"ADF262S", "Applications Development Fundamentals"},
                           {"ICE262S", "Artificial Intelligence"},
                           {"CNF262S", "Communications Networks Fundamentals"},
                           {"PRT262S", "Project"},
                           {"INM262S", "Information Management"},
                           {"PRC262S", "Professionals Communications"}};
    
      model = new DefaultTableModel(subjects ,Column);
      table = new JTable(model);
       JScrollPane scrollPane = new JScrollPane(table);
      pnl2.add(scrollPane);
     lbl3 = new JLabel("My Courses");
     pnl3 = new JPanel();
     pnl3.add(lbl3);
    
     tpane.addTab("Available courses", pnl2);
    tpane.addTab("my Courses", pnl3);
    
    
      pnl4 = new JPanel();
      enroll = new JButton("Enroll");
      pnl4.add(enroll);
      Cancel = new JButton("Cancel");
        pnl4.add(Cancel);
        
    add(pnl1,BorderLayout.NORTH);
    add(pnl4,BorderLayout.SOUTH);
    add(tpane);
     
     
      
    
    
    
}
     public static void main(String[] args) {
          StudentGui mygui = new StudentGui();
           mygui.setTitle("EasyEnrol");
           mygui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           mygui.setSize(700, 600);
           mygui.setVisible(true);
            
    }
}