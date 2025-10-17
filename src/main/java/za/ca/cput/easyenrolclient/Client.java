
package za.ca.cput.easyenrolclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import za.ca.cput.easyenrolclient.domain.Course;
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

    public static boolean connect() {
        try {
            System.out.println("Attempting to connect to server...");
            socket = new Socket("127.0.0.1", 6456);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server successfully");
            return true;
        } catch (IOException ex) {
            System.out.println("Failed to connect to server: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static String communicate(String username, String password) {
        try {
            // Sending request type first
            out.writeObject("LOGIN");
            out.writeObject(username);
            out.writeObject(password);
            out.flush();
            
            String response = (String) in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException ex) {
            return "error";
        }
    }
    
    public static String sendEnrollment(enrollment enroll) {
        try {
            // Send request type first
            out.writeObject("ENROLL");
            out.writeObject(enroll);
            out.flush();
            
            String response = (String) in.readObject();
            System.out.println("From SERVER>> " + response);
            return response;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            return "failed";
        } 
    }
    
    public static ArrayList<Course> getAvailableCourses() {
        try {
            // Send request type first
            out.writeObject("GET_COURSES");
            out.flush();
            
            // Receive courses from server
            ArrayList<Course> courses = (ArrayList<Course>) in.readObject();
            System.out.println("Received " + courses.size() + " courses from server");
            return courses;
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Error retrieving courses", ex);
            return new ArrayList<>(); // Return empty list on error
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

