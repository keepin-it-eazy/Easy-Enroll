
package za.ca.cput.easyenrolclient.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ca.cput.easyenrolclient.dao.CourseDAO;
import za.ca.cput.easyenrolclient.dao.LoginDAO;
import za.ca.cput.easyenrolclient.dao.EnrollDao;
import za.ca.cput.easyenrolclient.domain.Course;
import za.ca.cput.easyenrolclient.domain.enrollment;

/**
 *
 * @author samuk
 */
public class Server {

    private static ServerSocket listener;
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void listen() {
        try {
            listener = new ServerSocket(6456);
            System.out.println("Server is listening");
            
            while (true) {
                socket = listener.accept();
                System.out.println("Client connected");
                
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                
                System.out.println("Streams initialized, waiting for requests...");
                receiveData();
            }
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    public static void receiveData() {
        try {
            while (true) {
                String requestType = (String) in.readObject();
                System.out.println("Received request: " + requestType);
                
                if ("LOGIN".equals(requestType)) {
                    handleLogin();
                } else if ("GET_COURSES".equals(requestType)) {
                    processCourseRetrieval();
                } else if ("ENROLL".equals(requestType)) {
                    processEnrollment();
                } else if ("EXIT".equals(requestType)) {
                    System.out.println("Client requested exit");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception at ReceiveData");
            ex.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                System.out.println("Connection closed");
            } catch (IOException ex) {
                System.out.println("Error closing resources: " + ex.getMessage());
            }
        }
    }
    
    private static void handleLogin() {
        try {
            String username = (String) in.readObject();
            String password = (String) in.readObject();
            System.out.println("Login attempt for user: " + username);
            
            LoginDAO dao = new LoginDAO();
            String response = dao.authenticate(username, password);

            out.writeObject(response);
            out.flush();
            System.out.println("Login response sent: " + response);
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void processCourseRetrieval() {
        try {
            System.out.println("Processing course retrieval request...");
            
            CourseDAO courseDao = new CourseDAO();
            ArrayList<Course> courses = courseDao.getAllCourses();
            
            out.writeObject(courses);
            out.flush();
            
            System.out.println("Sent " + courses.size() + " courses to client");
            
        } catch (IOException ex) {
            System.out.println("IO Exception at processCourseRetrieval: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Exception at processCourseRetrieval: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public static void processEnrollment() {
        try {
            System.out.println("Processing enrollment request...");
            
            enrollment tempEnroll = (enrollment) in.readObject();
            EnrollDao dao = new EnrollDao();
            String response = dao.Enrollment(tempEnroll);

            out.writeObject(response);
            out.flush();
            System.out.println("Enrollment response sent: " + response);
            
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Exception in processEnrollment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server.listen();
    }
}
