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
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ca.cput.easyenrolclient.dao.LoginDAO;
import za.ca.cput.easyenrolclient.dao.enrollDao;
import za.ca.cput.easyenrolclient.domain.enrollment;

/**
 *
 * @author samuk
 */
public class Server {

    private static ServerSocket listener;
    private static Socket socket;
    private static LoginDAO dao;

    public static void listen() {
        // Start listening for client connections
        try {
            listener = new ServerSocket(6666, 1);
            System.out.println("Server is listening");
            socket = listener.accept();
            System.out.println("Now moving onto authenticating client");
            receiveData();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    public static void receiveData() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            String username, password;
            boolean loggedIn = false;

            while (!loggedIn) {
                username = (String) in.readObject();
                password = (String) in.readObject();
                dao = new LoginDAO();
                String response = dao.authenticate(username, password);

                out.writeObject(response);
                out.flush();

                if (!response.equalsIgnoreCase("invalid")) {
                    loggedIn = true; // exit loop once valid
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void processEnrollment(enrollment enroll) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             enrollment tempEnroll;
            while (( tempEnroll = (enrollment) in.readObject()) != null) {
                
                enrollDao dao = new enrollDao();
                dao.Enrollment(tempEnroll);
                String response = "Enrollment successful for student: " + enroll.getStudentid();
                out.writeObject(response);
                out.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        Server.listen();
    }
}
