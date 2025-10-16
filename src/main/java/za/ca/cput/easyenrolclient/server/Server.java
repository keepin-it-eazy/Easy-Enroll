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
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void listen() {
        // Start listening for client connections
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

    public static void receiveData() {
        try {

            String username, password;
            boolean loggedIn = false;

            while (true) {
                username = (String) in.readObject();
                password = (String) in.readObject();
                dao = new LoginDAO();
                String response = dao.authenticate(username, password);

                out.writeObject(response);
                out.flush();

                if (!response.equalsIgnoreCase("invalid")) {
                    loggedIn = true; // exit loop once valid
                    processEnrollment();
                }
            }

        } catch (IOException e) {
            System.out.println("IO Exception at ReceiveData");
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFound Exception at ReceiveData");
            ex.printStackTrace();
        }
    }

    public static void processEnrollment() {

        try {

            enrollment tempEnroll;
            while ((tempEnroll = (enrollment) in.readObject()) != null) {

                enrollDao dao = new enrollDao();
                String response;
                response = dao.Enrollment(tempEnroll);

                out.writeObject(response);
                out.flush();
            }
        } catch (IOException ex) {
            System.out.println("IO Exception at processEnrollment: ");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found exception: ");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server.listen();
    }
}
