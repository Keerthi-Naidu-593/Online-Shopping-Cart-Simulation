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

        System.out.println("✓ CheckoutFrame - Total items: " + cartItems.size());
        System.out.println("✓ CheckoutFrame - Total amount: " + total);

        setupUI();
    }

    private void setupUI() {
        setTitle("Checkout - Order Summary");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // ===== HEADING =====
        JLabel heading = new JLabel("Order Summary");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(new Color(44, 62, 80));
        mainPanel.add(heading, BorderLayout.NORTH);

        // ===== CONTENT PANEL =====
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(240, 240, 240));

        // ===== CARD PANEL =====
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        // ===== CUSTOMER INFO SECTION =====
        JPanel customerSection = createSection("Customer Information");
        cardPanel.add(customerSection);

        JPanel custDetails = new JPanel();
        custDetails.setBackground(Color.WHITE);
        custDetails.setLayout(new BoxLayout(custDetails, BoxLayout.Y_AXIS));
        custDetails.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        custDetails.add(createDetailRow("Name:", currentUser.getUsername()));
        custDetails.add(Box.createVerticalStrut(8));
        custDetails.add(createDetailRow("Email:", currentUser.getEmail()));
        custDetails.add(Box.createVerticalStrut(8));
        custDetails.add(createDetailRow("Phone:", currentUser.getPhone() != null ? currentUser.getPhone() : "N/A"));
        custDetails.add(Box.createVerticalStrut(8));
        custDetails.add(createDetailRow("Address:", currentUser.getAddress() != null ? currentUser.getAddress() : "N/A"));

        cardPanel.add(custDetails);
        cardPanel.add(Box.createVerticalStrut(15));

        // ===== SEPARATOR =====
        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        cardPanel.add(sep1);
        cardPanel.add(Box.createVerticalStrut(15));

        // ===== ORDER ITEMS SECTION =====
        JPanel itemsSection = createSection("Order Items");
        cardPanel.add(itemsSection);
        cardPanel.add(Box.createVerticalStrut(10));

        if (cartItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("No items in cart");
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 13));
            emptyLabel.setForeground(new Color(231, 76, 60));
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            cardPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < cartItems.size(); i++) {
                CartItem item = cartItems.get(i);
                JPanel itemPanel = createItemPanel(item);
                cardPanel.add(itemPanel);
                
                if (i < cartItems.size() - 1) {
                    cardPanel.add(Box.createVerticalStrut(8));
                }
            }
        }

        cardPanel.add(Box.createVerticalStrut(20));

        // ===== SEPARATOR =====
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        cardPanel.add(sep2);
        cardPanel.add(Box.createVerticalStrut(15));

        // ===== TOTAL SECTION =====
        JPanel totalPanel = new JPanel();
        totalPanel.setBackground(new Color(52, 152, 219));
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        totalPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 30, 12));

        JLabel totalLabel = new JLabel(String.format("Total Amount: ₹%.2f", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(Color.WHITE);
        totalPanel.add(totalLabel);

        cardPanel.add(totalPanel);

        // ===== SCROLL PANE =====
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(240, 240, 240));
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JButton confirmBtn = UITheme.createSuccessButton("Confirm & Download Bill");
        confirmBtn.setPreferredSize(new Dimension(250, 50));
        confirmBtn.addActionListener(e -> confirmOrder());
        btnPanel.add(confirmBtn);

        JButton cancelBtn = UITheme.createDangerButton("Cancel Order");
        cancelBtn.setPreferredSize(new Dimension(150, 50));
        cancelBtn.addActionListener(e -> {
            new CustomerDashboard(currentUser);
            dispose();
        });
        btnPanel.add(cancelBtn);

        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // ===== HELPER: CREATE SECTION HEADING =====
    private JPanel createSection(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(44, 62, 80));
        panel.add(label);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    // ===== HELPER: CREATE DETAIL ROW =====
    private JPanel createDetailRow(String label, String value) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel keyLabel = new JLabel(label);
        keyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        keyLabel.setForeground(new Color(44, 62, 80));
        keyLabel.setPreferredSize(new Dimension(100, 25));
        panel.add(keyLabel);

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        valLabel.setForeground(new Color(52, 73, 94));
        panel.add(valLabel);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    // ===== HELPER: CREATE ITEM PANEL =====
    private JPanel createItemPanel(CartItem item) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(247, 250, 252));
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Left: Product Info
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(247, 250, 252));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel("• " + item.getProduct().getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(new Color(44, 62, 80));
        leftPanel.add(nameLabel);

        JLabel detailsLabel = new JLabel("Quantity: " + item.getQuantity() + " × ₹" + String.format("%.2f", item.getProduct().getPrice()));
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        detailsLabel.setForeground(new Color(127, 140, 141));
        leftPanel.add(detailsLabel);

        panel.add(leftPanel, BorderLayout.CENTER);

        // Right: Subtotal
        JLabel subtotalLabel = new JLabel("₹" + String.format("%.2f", item.getSubtotal()));
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        subtotalLabel.setForeground(new Color(46, 204, 113));
        subtotalLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(subtotalLabel, BorderLayout.EAST);

        return panel;
    }

    private void confirmOrder() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

            System.out.println("✓ Order created with ID: " + orderId);

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

            System.out.println("✓ Order items inserted");

            // Clear Cart
            PreparedStatement clearStmt = con.prepareStatement("DELETE FROM cart WHERE user_id = ?");
            clearStmt.setInt(1, currentUser.getId());
            clearStmt.executeUpdate();

            System.out.println("✓ Cart cleared");

            // Generate PDF Bill
            BillGenerator.generatePdfBill(orderId, currentUser, cartItems, total);

            JOptionPane.showMessageDialog(this, 
                "✅ Order placed successfully!\n\n" +
                "Order ID: " + orderId + "\n" +
                "Total: ₹" + String.format("%.2f", total) + "\n\n" +
                "Your PDF bill has been generated.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);

            new CustomerDashboard(currentUser);
            dispose();

        } catch (SQLException ex) {
            System.err.println(" Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}