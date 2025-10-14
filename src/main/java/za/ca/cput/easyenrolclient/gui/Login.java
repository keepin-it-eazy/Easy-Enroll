/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ca.cput.easyenrolclient.gui;

import java.awt.Color;
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
        try {
            // Load Geist font once
            geistFont = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("C:\\Users\\samuk\\Downloads\\Geist\\Geist-VariableFont_wght.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
            geistFont = new Font("SansSerif", Font.PLAIN, 16); // fallback
        }
        setLayout(null);
        lblLogin = new JLabel("Login");
        lblLogin.setFont(geistFont.deriveFont(Font.BOLD, 34f));
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setBounds(160, 11, 251, 50);

        lblUsername = new JLabel("Username");
        lblUsername.setFont(geistFont.deriveFont(Font.PLAIN, 20f));
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(50, 90, 251, 50);

        txtUsername = new JTextField(15);
        txtUsername.setBackground(Color.BLACK);
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.setBorder(new LineBorder(Color.GRAY, 3));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(30), new EmptyBorder(5, 10, 5, 10)));
        txtUsername.setBounds(40, 130, 270, 30);

        lblPassword = new JLabel("Password");
        lblPassword.setFont(geistFont.deriveFont(Font.PLAIN, 20f));
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(50, 160, 251, 50);

        txtPassword = new JTextField(15);
        txtPassword.setBackground(Color.BLACK);
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setBorder(new LineBorder(Color.GRAY, 3));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(30), new EmptyBorder(5, 10, 5, 10)));
        txtPassword.setBounds(40, 200, 270, 30);

        btnLogin = new RoundedButton("Login");
        btnLogin.setFont(geistFont.deriveFont(Font.BOLD, 20f));
        btnLogin.setFocusPainted(true);
        btnLogin.setBorderPainted(true);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(41, 111, 208));
        //btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // btnLogin.setBorder(new RoundedBorder(30));
        btnLogin.setBounds(98, 300, 200, 35);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();
                String response = Client.communicate(username, password);

                if ("student".equalsIgnoreCase(response)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose(); 
                    
                    StudentGui myGUI = new StudentGui(username);
                    myGUI.setTitle("Client");
                    myGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    myGUI.setSize(500, 700);
                    myGUI.setVisible(true);
                }else if("admin".equalsIgnoreCase(response)){
                    //construct admin dashboard
                    JOptionPane.showMessageDialog(null, "This would trigger student dashboard");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
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
        getContentPane().setBackground(new Color(2, 0, 0));

    }

    public static void main(String[] args) {
    }
}
