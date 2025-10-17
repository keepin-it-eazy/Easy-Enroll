/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.Student;
import za.ca.cput.easyenrolclient.domain.enrollment;
import za.ca.cput.easyenrolclient.gui.Login;

/**
 *
 * @author samuk
 */
public class Client {

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void connect() {
        try {
            System.out.println("Connect is running");
            socket = new Socket("127.0.0.1", 6666);
            // Step 1: create channels
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
      
    public static String communicate(int id, String password) {
        try {
            out.writeObject("login");
            out.writeObject(id);
            out.writeObject(password);
            out.flush();
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "error";
        }
    }

    
    public static String sendEnrollment(enrollment enroll) {
        try {
            out.writeObject("enroll");
            out.writeObject(enroll);
            out.flush();
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "failed";
        }
    }

    
    public static String sendStudent(Student s) {
        try {
            out.writeObject("addStudent");
            out.writeObject(s);
            out.flush();
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "failed";
        }
    }
    public static String sendCourse(Course c) {
        try {
            out.writeObject("addCourse");
            out.writeObject(c);
            out.flush();
            return (String) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "failed";
        }
    }
    public static ArrayList<Course> getAvailableCourses() {
        try {
            
            out.writeObject("getCourses");
            out.flush();
            
            
            ArrayList<Course> courses = (ArrayList<Course>) in.readObject();
            System.out.println("Received " + courses.size() + " courses from server");
            return courses;
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Error retrieving courses", ex);
            return new ArrayList<>(); // Return empty list on error
        }
    }
    public static enrollment getEnrollmentById(int studentId){
        try {
            out.writeObject("getEnrollmentById");
            out.flush();
            out.writeObject(studentId);
            out.flush();
            
            enrollment e = (enrollment) in.readObject();
            System.out.println("Received " + e.getCourses().size() + " enrolled courses for student " + studentId);
            return e;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return new enrollment(studentId, new ArrayList<>());
        }
        
    }
    public static ArrayList<Student> getStudentsByCourse(String courseCode) {
    try {
        out.writeObject("getStudentsByCourse"); 
        out.flush();

        out.writeObject(courseCode); 
        out.flush();
        Object response = in.readObject();
       
        if (response instanceof ArrayList<?>) {
           
            ArrayList<Student> students = (ArrayList<Student>) response;
            System.out.println("Received " + students.size() + " students for course " + courseCode);
            return students;
        } else {
            
            System.err.println("Unexpected server response: " + response);
            JOptionPane.showMessageDialog(null, response.toString(), "Server Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }

    } catch (IOException | ClassNotFoundException ex) {
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        return new ArrayList<>();
    }
}


    public static void main(String[] args) {

        Client.connect();
        Login loginGUI = new Login();
        loginGUI.setTitle("Client");
        loginGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginGUI.setSize(430, 500);
        loginGUI.setVisible(true);
    }
}


