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
import javax.swing.*;

public class AdminDashboard extends JFrame {
    private User currentAdmin;
    private JTabbedPane tabbedPane;

    public AdminDashboard(User currentAdmin) {
        this.currentAdmin = currentAdmin;

        setTitle("Admin Dashboard - " + currentAdmin.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Top Bar
        JPanel topPanel = new JPanel();
        topPanel.setBackground(UITheme.SECONDARY);
        topPanel.setLayout(new java.awt.BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setForeground(java.awt.Color.WHITE);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        topPanel.add(titleLabel, java.awt.BorderLayout.WEST);

        JLabel userLabel = new JLabel("Admin: " + currentAdmin.getUsername());
        userLabel.setForeground(java.awt.Color.WHITE);
        topPanel.add(userLabel, java.awt.BorderLayout.CENTER);

        JButton logoutBtn = UITheme.createDangerButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        topPanel.add(logoutBtn, java.awt.BorderLayout.EAST);

        add(topPanel, java.awt.BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(UITheme.BG_SECONDARY);

        tabbedPane.addTab("Manage Products", new AdminProductPanel());
        tabbedPane.addTab("View Orders", new AdminOrdersPanel());

        add(tabbedPane, java.awt.BorderLayout.CENTER);

        setVisible(true);
    }
}