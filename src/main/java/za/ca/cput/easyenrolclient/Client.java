/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
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

    public static String communicate(String username, String password) {
        // The connection has been established - now send/receive.
        
        try {
            // Step 2: communicate
            
            out.writeObject(username);
            out.writeObject(password);

            out.flush();
            
            String response = (String) in.readObject();
            return response;

            /*
                out.close();
                in.close();
                socket.close();
             */
        } catch (IOException | ClassNotFoundException ex) {
            return "error";
        }
        
    }
    
    
    public static String sendEnrollment (enrollment enroll){
        
        try {
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
    
    

    public static void main(String[] args) {

        Client.connect();
        Login loginGUI = new Login();
        loginGUI.setTitle("Client");
        loginGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginGUI.setSize(430, 500);
        loginGUI.setVisible(true);
    }
}


