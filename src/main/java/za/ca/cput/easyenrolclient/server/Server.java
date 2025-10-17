/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ca.cput.easyenrolclient.dao.CourseDAO;
import za.ca.cput.easyenrolclient.dao.LoginDAO;
import za.ca.cput.easyenrolclient.dao.StudentDAO;
import za.ca.cput.easyenrolclient.dao.enrollDao;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.Student;
import za.ca.cput.easyenrolclient.domain.enrollment;

/**
 *
 * @author samuk
 */
public class Server {

    private static ServerSocket listener;
    private static Socket socket;
    private static LoginDAO dao;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void listen() {
        
        try {
            listener = new ServerSocket(6666, 1);
            System.out.println("Server is listening");
            socket = listener.accept();
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Now moving onto authenticating client");
            receiveData();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }
   
        private static void receiveData() {
        try {
            while (true) {
                String command = (String) in.readObject(); // read the command
                switch (command) {
                    case "login":
                        handleLogin();
                        break;
                    case "getCourses":
                        processCourseRetrieval();
                        break;
                    case "enroll":
                        handleEnrollment();
                        break;
                    case "addStudent":
                        handleAddStudent();
                        break;
                    case "addCourse":
                        handleAddCourse();
                        break;
                    case "getEnrollmentById":
                        handleGetEnrollmentsById();
                        break;
                    case "getStudentsByCourse":
                        handleGetStudentsByCourse();
                    default:
                        out.writeObject("invalidCommand");
                        out.flush();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection closed or error occurred.");
            e.printStackTrace();
        }
    }

    private static void handleLogin() throws IOException, ClassNotFoundException {
        int id = (Integer) in.readObject();
        String password = (String) in.readObject();
        LoginDAO dao = new LoginDAO();
        String response = dao.authenticate(id, password);
        out.writeObject(response);
        out.flush();
    }

    private static void handleEnrollment() throws IOException, ClassNotFoundException {
        enrollment enroll = (enrollment) in.readObject();
        enrollDao dao = new enrollDao();
        String response = dao.Enrollment(enroll);
        out.writeObject(response);
        out.flush();
    }

    private static void handleAddStudent() throws IOException, ClassNotFoundException {
        Student student = (Student) in.readObject();
        StudentDAO dao = new StudentDAO();
        String response = dao.addStudent(student);
        out.writeObject(response);
        System.out.println(response);
        out.flush();
    }
    private static void handleAddCourse() throws IOException, ClassNotFoundException {
        Course course = (Course) in.readObject();
        CourseDAO dao = new CourseDAO();
        String response = dao.addCourse(course);
        out.writeObject(response);
        System.out.println(response);
        out.flush();
    }
    private static void handleGetEnrollmentsById() throws IOException, ClassNotFoundException {
        try {
            int studentId = (Integer) in.readObject();
            enrollDao dao = new enrollDao();
            
            enrollment e = dao.getEnrollmentsByStudentId(studentId);
            
            out.writeObject(e);
            out.flush();
            System.out.println("Sent enrollment with " + e.getCourses().size() + " courses to client");
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void processCourseRetrieval() throws IOException {
        
            System.out.println("Processing course retrieval request...");
            
            CourseDAO courseDao = new CourseDAO();
            ArrayList<Course> courses = courseDao.getAllCourses();
            
            out.writeObject(courses);
            out.flush();
            
            System.out.println("Sent " + courses.size() + " courses to client");
     }
    private static void handleGetStudentsByCourse() throws IOException, ClassNotFoundException {
    try {
        String courseCode = (String) in.readObject(); 
        enrollDao dao = new enrollDao();
        ArrayList<Student> students = dao.getStudentsByCourse(courseCode);
        
        if (students == null) {
            students = new ArrayList<>();
        }
        
        out.writeObject(students); 
        out.flush();

        System.out.println("Sent " + students.size() + " students for course " + courseCode);
    } catch (SQLException ex) {
        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
         out.writeObject(new ArrayList<Student>());
         out.flush();
    }catch (ClassCastException ex) {
        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        // Send empty list on unexpected input
        out.writeObject(new ArrayList<Student>());
        out.flush();
    }
}



    public static void main(String[] args) {
        Server.listen();
    }
}
