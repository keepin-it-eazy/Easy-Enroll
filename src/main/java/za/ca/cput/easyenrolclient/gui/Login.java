/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import za.ca.cput.easyenrolclient.Client;
import za.ca.cput.easyenrolclient.domain.Student;

/**
 *
 * @author samuk
 */
public class Login extends JFrame {

    private JLabel lblLogin, lblUsername, lblPassword;
    private JTextField txtUsername, txtPassword;
    private JButton btnLogin;
    private Font geistFont;

    public Login() {
        super();

        setLayout(null);
        lblLogin = new JLabel("Login");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 36));

        lblLogin.setBounds(160, 11, 251, 50);

        lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 24));

        lblUsername.setBounds(50, 90, 251, 50);

        txtUsername = new JTextField(15);

        txtUsername.setBounds(40, 130, 270, 30);

        lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 24));

        lblPassword.setBounds(50, 160, 251, 50);

        txtPassword = new JTextField(15);

        txtPassword.setBounds(40, 200, 270, 30);

        btnLogin = new JButton("Login");

        btnLogin.setBounds(98, 300, 200, 35);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();

                // Input validation
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int id;
                try {
                    id = Integer.parseInt(username);
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Username must be a valid numeric ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    txtUsername.setText("");
                    txtPassword.setText("");
                    return;
                }

                // Send credentials to server
                String response = Client.communicate(id, password);

                if ("student".equalsIgnoreCase(response)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose();

                    StudentGui myGUI = new StudentGui(id);
                    myGUI.setTitle("Client");
                    myGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    myGUI.setSize(500, 700);
                    myGUI.setVisible(true);
                } else if ("admin".equalsIgnoreCase(response)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose();

                    AdminGui admin = new AdminGui();
                    admin.setTitle("Easy Enrol");
                    admin.setPreferredSize(new Dimension(600, 400));
                    admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    admin.setLocationRelativeTo(null);
                    admin.setResizable(true);
                    admin.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    txtUsername.setText("");
                    txtPassword.setText("");
                }
            }
        });

        add(lblLogin);
        add(lblUsername);
        add(txtUsername);
        add(lblPassword);
        add(txtPassword);
        add(btnLogin);

    }

    public static void main(String[] args) {
    }
}
