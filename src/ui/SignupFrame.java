/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author KEERTHI
 */
package ui;

import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignupFrame extends JFrame {
    private JTextField usernameField, emailField, phoneField;
    private JTextArea addressArea;
    private JPasswordField passwordField, confirmField;
    private JLabel errorLabel;

    public SignupFrame() {
        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("Shopping Cart - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        mainPanel.setBackground(Color.WHITE);

        // Left side
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(46, 204, 113));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 30, 50, 30));

        /*JLabel logoLabel = new JLabel("✨");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 70));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createVerticalStrut(20));*/

        JLabel titleLabel = new JLabel("Join Us");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(10));

        JLabel subtitleLabel = new JLabel("Start shopping today");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(subtitleLabel);
        leftPanel.add(Box.createVerticalStrut(40));

        String[] benefits = {"*Great Deals", "* Fast Shipping", "*Exclusive Offers", "*Best Support"};
        for (String benefit : benefits) {
            JLabel benefitLabel = new JLabel(benefit);
            benefitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            benefitLabel.setForeground(Color.WHITE);
            benefitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(benefitLabel);
            leftPanel.add(Box.createVerticalStrut(15));
        }

        leftPanel.add(Box.createVerticalGlue());

        // Right side - form
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(240, 240, 240));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel headingLabel = new JLabel("Create Account");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headingLabel.setForeground(new Color(44, 62, 80));
        headingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(headingLabel);
        rightPanel.add(Box.createVerticalStrut(15));

        JLabel subheadingLabel = new JLabel("Fill in your details");
        subheadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subheadingLabel.setForeground(new Color(127, 140, 141));
        subheadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(subheadingLabel);
        rightPanel.add(Box.createVerticalStrut(20));

        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        errorLabel.setForeground(new Color(231, 76, 60));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(errorLabel);
        rightPanel.add(Box.createVerticalStrut(10));

        // Username
        addField(rightPanel, "Username", () -> {
            usernameField = createTextField();
            return usernameField;
        });

        // Email
        addField(rightPanel, "Email", () -> {
            emailField = createTextField();
            return emailField;
        });

        // Phone
        addField(rightPanel, "Phone", () -> {
            phoneField = createTextField();
            return phoneField;
        });

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addressLabel.setForeground(new Color(44, 62, 80));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(addressLabel);
        rightPanel.add(Box.createVerticalStrut(5));

        addressArea = new JTextArea(2, 30);
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addressArea.setBackground(Color.WHITE);
        addressArea.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setPreferredSize(new Dimension(300, 50));
        addressArea.setMaximumSize(new Dimension(300, 50));
        rightPanel.add(addressArea);
        rightPanel.add(Box.createVerticalStrut(15));

        // Password
        addField(rightPanel, "Password", () -> {
            passwordField = createPasswordField();
            return passwordField;
        });

        // Confirm
        addField(rightPanel, "Confirm Password", () -> {
            confirmField = createPasswordField();
            return confirmField;
        });

        rightPanel.add(Box.createVerticalStrut(20));

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setMaximumSize(new Dimension(350, 60));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setOpaque(true);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setPreferredSize(new Dimension(150, 45));
        registerButton.addActionListener(e -> handleSignup());
        buttonPanel.add(registerButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setBackground(new Color(44, 62, 80));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(true);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(150, 45));
        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        buttonPanel.add(backButton);

        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createVerticalGlue());

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
        setVisible(true);
    }

    private void addField(JPanel panel, String label, java.util.function.Supplier<JTextField> fieldSupplier) {
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fieldLabel.setForeground(new Color(44, 62, 80));
        fieldLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(fieldLabel);
        panel.add(Box.createVerticalStrut(5));

        JTextField field = fieldSupplier.get();
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(44, 62, 80));
        field.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        field.setPreferredSize(new Dimension(300, 35));
        field.setMaximumSize(new Dimension(300, 35));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(44, 62, 80));
        field.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        field.setPreferredSize(new Dimension(300, 35));
        field.setMaximumSize(new Dimension(300, 35));
        return field;
    }

        private void handleSignup() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        // Validation
        if (username.isEmpty()) {
            errorLabel.setText("Username is required");
            usernameField.requestFocus();
            return;
        }

        if (username.length() < 3) {
            errorLabel.setText("Username must be at least 3 characters");
            usernameField.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            errorLabel.setText("Email is required");
            emailField.requestFocus();
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setText("Invalid email format");
            emailField.requestFocus();
            return;
        }

        // Phone validation (NOW MANDATORY)
        if (phone.isEmpty()) {
            errorLabel.setText("Phone number is required");
            phoneField.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            errorLabel.setText("Phone number must be exactly 10 digits");
            phoneField.requestFocus();
            return;
        }

        if (!phone.matches("\\d+")) {
            errorLabel.setText("Phone number must contain only digits");
            phoneField.requestFocus();
            return;
        }

        // Address validation (NOW MANDATORY)
        if (address.isEmpty()) {
            errorLabel.setText("Address is required");
            addressArea.requestFocus();
            return;
        }

        if (address.length() < 10) {
            errorLabel.setText("Address must be at least 10 characters");
            addressArea.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            errorLabel.setText("Password is required");
            passwordField.requestFocus();
            return;
        }

        if (password.length() < 6) {
            errorLabel.setText("Password must be at least 6 characters");
            passwordField.requestFocus();
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords don't match");
            confirmField.requestFocus();
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "INSERT INTO users (username, email, phone, address, password, role) VALUES (?, ?, ?, ?, ?, 'customer')")) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phone);  // NOW MANDATORY - NO NULL
            stmt.setString(4, address); // NOW MANDATORY - NO NULL
            stmt.setString(5, password);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Account created successfully!\n\nPlease login with your credentials.", "Success", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame();
            dispose();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                errorLabel.setText("Username or email already exists");
            } else {
                errorLabel.setText("Registration failed. Please try again.");
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }
         
}