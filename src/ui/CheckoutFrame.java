/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KEERTHI
 */

package ui;

import models.CartItem;
import models.User;
import database.DatabaseConnection;
import utils.BillGenerator;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckoutFrame extends JFrame {
    private User currentUser;
    private List<CartItem> cartItems;
    private double total;

    public CheckoutFrame(User currentUser, List<CartItem> cartItems) {
        this.currentUser = currentUser;
        this.cartItems = cartItems;
        this.total = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();

        setupUI();
    }

    private void setupUI() {
        setTitle("Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(UITheme.BG_SECONDARY);
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel heading = UITheme.createTitle("Order Summary");
        mainPanel.add(heading, BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(UITheme.BG_SECONDARY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(UITheme.BG_PRIMARY);
        cardPanel.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        cardPanel.setLayout(new GridBagLayout());

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(15, 15, 15, 15);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.gridwidth = GridBagConstraints.REMAINDER;

        // Customer Info
        JLabel customerLabel = UITheme.createSubheading("Customer Information");
        cardPanel.add(customerLabel, cardGbc);

        cardPanel.add(UITheme.createLabel("Name: " + currentUser.getUsername()), cardGbc);
        cardPanel.add(UITheme.createLabel("Email: " + currentUser.getEmail()), cardGbc);
        cardPanel.add(UITheme.createLabel("Phone: " + (currentUser.getPhone() != null ? currentUser.getPhone() : "N/A")), cardGbc);

        cardPanel.add(new JLabel(" "), cardGbc);

        // Order Items
        JLabel itemsLabel = UITheme.createSubheading("Order Items");
        cardPanel.add(itemsLabel, cardGbc);

        for (CartItem item : cartItems) {
            String itemText = String.format("%s x%d = ₹%.2f", item.getProduct().getName(), item.getQuantity(), item.getSubtotal());
            cardPanel.add(UITheme.createLabel("  • " + itemText), cardGbc);
        }

        cardPanel.add(new JLabel(" "), cardGbc);

        // Total
        JLabel totalLabel = new JLabel(String.format("Total Amount: ₹%.2f", total));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(UITheme.PRIMARY);
        cardPanel.add(totalLabel, cardGbc);

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        contentPanel.add(scrollPane, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(UITheme.BG_SECONDARY);

        JButton confirmBtn = UITheme.createSuccessButton("Confirm & Download Bill");
        confirmBtn.setPreferredSize(new Dimension(220, 45));
        confirmBtn.addActionListener(e -> confirmOrder());
        btnPanel.add(confirmBtn);

        JButton cancelBtn = UITheme.createDangerButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(150, 45));
        cancelBtn.addActionListener(e -> {
            new CustomerDashboard(currentUser);
            dispose();
        });
        btnPanel.add(cancelBtn);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void confirmOrder() {
        try (Connection con = DatabaseConnection.getConnection()) {
            // Create Order
            PreparedStatement orderStmt = con.prepareStatement(
                    "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, currentUser.getId());
            orderStmt.setDouble(2, total);
            orderStmt.executeUpdate();

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            int orderId = 0;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            // Add Order Items
            for (CartItem item : cartItems) {
                PreparedStatement itemStmt = con.prepareStatement(
                        "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)");
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProduct().getId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getProduct().getPrice());
                itemStmt.executeUpdate();
            }

            // Clear Cart
            PreparedStatement clearStmt = con.prepareStatement("DELETE FROM cart WHERE user_id = ?");
            clearStmt.setInt(1, currentUser.getId());
            clearStmt.executeUpdate();

            // Generate PDF Bill (opens automatically)
            BillGenerator.generatePdfBill(orderId, currentUser, cartItems, total);

            JOptionPane.showMessageDialog(this, 
                "Order placed successfully!\n\n" +
                "Order ID: " + orderId + "\n" +
                "Total Amount: ₹" + String.format("%.2f", total) + "\n\n" +
                "Your PDF bill has been generated and opened for download.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);

            new CustomerDashboard(currentUser);
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}