/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KEERTHI
 */
package ui;

import models.User;
import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginFrame() {
        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("ShopVibe - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        mainPanel.setBackground(Color.WHITE);

        // Left side - branding
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(52, 152, 219));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        /*JLabel logoLabel = new JLabel("️");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 80));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createVerticalStrut(20));*/

        JLabel titleLabel = new JLabel("ShopVibe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(10));

        JLabel subtitleLabel = new JLabel("Your Online Store");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(subtitleLabel);
        leftPanel.add(Box.createVerticalStrut(40));

        JLabel feature1 = new JLabel("*Easy Shopping");
        feature1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feature1.setForeground(Color.WHITE);
        feature1.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(feature1);
        leftPanel.add(Box.createVerticalStrut(15));

        JLabel feature2 = new JLabel("*Fast Checkout");
        feature2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feature2.setForeground(Color.WHITE);
        feature2.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(feature2);
        leftPanel.add(Box.createVerticalStrut(15));

        JLabel feature3 = new JLabel("*Secure Payment");
        feature3.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feature3.setForeground(Color.WHITE);
        feature3.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(feature3);

        leftPanel.add(Box.createVerticalGlue());

        // Right side - login form
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(240, 240, 240));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        // Heading
        JLabel headingLabel = new JLabel("Welcome Back");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headingLabel.setForeground(new Color(44, 62, 80));
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(headingLabel);
        rightPanel.add(Box.createVerticalStrut(10));

        // Subheading
        JLabel subheadingLabel = new JLabel("Sign in to continue");
        subheadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subheadingLabel.setForeground(new Color(127, 140, 141));
        subheadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(subheadingLabel);
        rightPanel.add(Box.createVerticalStrut(30));

        // Error label
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(new Color(231, 76, 60));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(errorLabel);
        rightPanel.add(Box.createVerticalStrut(15));

        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(new Color(44, 62, 80));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(usernameLabel);
        rightPanel.add(Box.createVerticalStrut(5));

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(new Color(44, 62, 80));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setMaximumSize(new Dimension(300, 40));
        rightPanel.add(usernameField);
        rightPanel.add(Box.createVerticalStrut(20));

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(44, 62, 80));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(passwordLabel);
        rightPanel.add(Box.createVerticalStrut(5));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(new Color(44, 62, 80));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setMaximumSize(new Dimension(300, 40));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createVerticalStrut(30));

        // Login button
        JButton loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setMaximumSize(new Dimension(300, 45));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(41, 128, 185));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
        });
        rightPanel.add(loginButton);
        rightPanel.add(Box.createVerticalStrut(25));

        // Separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 1));
        rightPanel.add(separator);
        rightPanel.add(Box.createVerticalStrut(25));

        // Signup section
        JPanel signupPanel = new JPanel();
        signupPanel.setBackground(new Color(240, 240, 240));
        signupPanel.setMaximumSize(new Dimension(300, 50));

        JLabel signupText = new JLabel("New user? ");
        signupText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signupText.setForeground(new Color(127, 140, 141));
        signupPanel.add(signupText);

        JButton signupButton = new JButton("Register Here");
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        signupButton.setForeground(new Color(52, 152, 219));
        signupButton.setBackground(new Color(240, 240, 240));
        signupButton.setBorder(BorderFactory.createEmptyBorder());
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.setFocusPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.addActionListener(e -> {
            new SignupFrame();
            dispose();
        });
        signupPanel.add(signupButton);

        rightPanel.add(signupPanel);
        rightPanel.add(Box.createVerticalGlue());

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty()) {
            errorLabel.setText("Enter username");
            return;
        }

        if (password.isEmpty()) {
            errorLabel.setText("Enter password");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                user.setId(rs.getInt("id"));
                user.setRole(rs.getString("role"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));

                System.out.println("User logged in: " + user);

                if ("admin".equals(user.getRole())) {
                    new AdminDashboard(user);
                } else {
                    new CustomerDashboard(user);
                }
                dispose();
            } else {
                errorLabel.setText("Invalid credentials");
                passwordField.setText("");
            }

        } catch (SQLException ex) {
            errorLabel.setText("Database error");
            System.err.println("Error: " + ex.getMessage());
        }
    }
}