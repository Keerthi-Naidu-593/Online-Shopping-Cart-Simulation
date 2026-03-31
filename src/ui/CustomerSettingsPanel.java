/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import database.DatabaseConnection;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CustomerSettingsPanel extends JPanel {

    private User currentUser;

    private JTextField usernameField, emailField, phoneField, addressField;
    private JPasswordField passwordField, confirmField;

    private JLabel errorLabel;

    public CustomerSettingsPanel(User currentUser) {
        this.currentUser = currentUser;

        setupUI();
        loadUserDetails();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== HEADER =====
        JLabel heading = new JLabel("Account Settings");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
        add(heading, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Update Details"));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Label.foreground", new Color(0, 120, 215));

        // Username (DISABLED)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        usernameField.setEditable(false); // ✅ DISABLED
        usernameField.setBackground(new Color(230, 230, 230)); // grey look
        usernameField.setForeground(Color.DARK_GRAY);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);

        phoneField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Address:"), gbc);

        addressField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Confirm Password:"), gbc);

        confirmField = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(confirmField, gbc);

        // Error Label
        errorLabel = new JLabel("");
        errorLabel.setForeground(new Color(231, 76, 60));
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(errorLabel, gbc);

        // ===== BUTTONS =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton updateBtn = UITheme.createPrimaryButton("Update");
        updateBtn.addActionListener(e -> updateDetails());
        btnPanel.add(updateBtn);

        JButton deleteBtn = UITheme.createDangerButton("Delete Account");
        deleteBtn.addActionListener(e -> deleteAccount());
        btnPanel.add(deleteBtn);

        gbc.gridy = 7;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    // ===== LOAD USER DATA =====
    private void loadUserDetails() {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT * FROM users WHERE id = ?")) {

            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                addressField.setText(rs.getString("address"));
                passwordField.setText(rs.getString("password"));
                confirmField.setText(rs.getString("password"));
            }

        } catch (SQLException ex) {
            System.err.println("Error loading user: " + ex.getMessage());
        }
    }

    // ===== UPDATE =====
    private void updateDetails() {

        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            errorLabel.setText("Invalid email");
            return;
        }

        if (phone.isEmpty() || phone.length() != 10 || !phone.matches("\\d+")) {
            errorLabel.setText("Phone must be 10 digits");
            return;
        }

        if (address.isEmpty() || address.length() < 10) {
            errorLabel.setText("Address must be at least 10 characters");
            return;
        }

        if (password.length() < 6) {
            errorLabel.setText("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords don't match");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "UPDATE users SET email=?, phone=?, address=?, password=? WHERE id=?")) {

            stmt.setString(1, email);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setString(4, password);
            stmt.setInt(5, currentUser.getId());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Updated successfully");
            errorLabel.setText("");

        } catch (SQLException ex) {
            errorLabel.setText("Update failed");
        }
    }

    // ===== DELETE ACCOUNT =====
    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account?",
                "Warning",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DatabaseConnection.getConnection()) {

            PreparedStatement cartStmt = con.prepareStatement(
                    "DELETE FROM cart WHERE user_id = ?");
            cartStmt.setInt(1, currentUser.getId());
            cartStmt.executeUpdate();

            PreparedStatement userStmt = con.prepareStatement(
                    "DELETE FROM users WHERE id = ?");
            userStmt.setInt(1, currentUser.getId());
            userStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account deleted");

            new LoginFrame();
            SwingUtilities.getWindowAncestor(this).dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}