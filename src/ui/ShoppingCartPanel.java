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
import models.Product;
import models.User;
import database.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.JTableHeader;

public class ShoppingCartPanel extends JPanel {
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JLabel itemsCountLabel;
    private User currentUser;
    private List<CartItem> cartItems;
    private Runnable refreshCallback;

    public ShoppingCartPanel(User currentUser, Runnable refreshCallback) {
        this.currentUser = currentUser;
        this.cartItems = new ArrayList<>();
        this.refreshCallback = refreshCallback;

        setupUI();
        loadCart();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));

        JLabel heading = new JLabel("Shopping Cart");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
        headerPanel.add(heading, BorderLayout.WEST);

        itemsCountLabel = new JLabel("Items: 0");
        itemsCountLabel.setFont(new Font("Arial", Font.BOLD, 13));
        itemsCountLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(itemsCountLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(240, 240, 240));

        String[] columns = {"Product", "Price (₹)", "Quantity", "Subtotal (₹)"};
        tableModel = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(tableModel);
        cartTable = new JTable(tableModel);

        JTableHeader header = cartTable.getTableHeader();
        header.setPreferredSize(new Dimension(100, 35));

        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
    {
        setHorizontalAlignment(JLabel.CENTER);
        setBackground(new Color(44, 62, 80));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 13));
        setOpaque(true);
    }
});
        cartTable.getTableHeader().setReorderingAllowed(false);
        cartTable.getTableHeader().setResizingAllowed(false);
        cartTable.setRowHeight(40);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.setBackground(Color.WHITE);
        cartTable.setGridColor(new Color(224, 224, 224));
        cartTable.setFont(new Font("Arial", Font.PLAIN, 12));
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        cartTable.getTableHeader().setBackground(new Color(44, 62, 80));
        cartTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new BorderLayout(20, 15));
        bottomPanel.setPreferredSize(new Dimension(0, 120));

        // Total Section
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 15));
        totalPanel.setBackground(Color.WHITE);

        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(52, 152, 219));
        totalPanel.add(totalLabel);

        bottomPanel.add(totalPanel, BorderLayout.NORTH);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        btnPanel.setBackground(Color.WHITE);

        JButton removeBtn = UITheme.createDangerButton("Remove");
        removeBtn.setPreferredSize(new Dimension(130, 45));
        removeBtn.addActionListener(e -> removeItem());
        btnPanel.add(removeBtn);

        JButton clearBtn = UITheme.createWarningButton("Clear Cart");
        clearBtn.setPreferredSize(new Dimension(130, 45));
        clearBtn.addActionListener(e -> clearCart());
        btnPanel.add(clearBtn);

        JButton checkoutBtn = UITheme.createSuccessButton("Checkout");
        checkoutBtn.setPreferredSize(new Dimension(130, 45));
        checkoutBtn.addActionListener(e -> proceedToCheckout());
        btnPanel.add(checkoutBtn);

        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadCart() {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT c.id, p.id as pid, p.name, p.price, c.quantity FROM cart c " +
                     "JOIN products p ON c.product_id = p.id WHERE c.user_id = ? ORDER BY c.added_at DESC")) {

            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            cartItems.clear();
            tableModel.setRowCount(0);
            double total = 0;

            while (rs.next()) {
                Product product = new Product("", "", rs.getDouble("price"), 0, "");
                product.setId(rs.getInt("pid"));
                product.setName(rs.getString("name"));

                CartItem item = new CartItem(product, rs.getInt("quantity"));
                item.setCartId(rs.getInt("id"));
                cartItems.add(item);

                Object[] row = {
                        "  " + product.getName(),
                        "₹ " + String.format("%.2f", product.getPrice()),
                        rs.getInt("quantity"),
                        "₹ " + String.format("%.2f", item.getSubtotal())
                };
                tableModel.addRow(row);
                total += item.getSubtotal();
            }

            totalLabel.setText(String.format("Total: ₹%.2f", total));
            itemsCountLabel.setText("Items: " + cartItems.size());

        } catch (SQLException ex) {
            System.err.println("✗ Error loading cart: " + ex.getMessage());
        }
    }

    private void removeItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "❌ Select an item first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CartItem item = cartItems.get(selectedRow);

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM cart WHERE id = ?")) {

            stmt.setInt(1, item.getCartId());
            stmt.executeUpdate();

            loadCart();
            refreshCallback.run();
        } catch (SQLException ex) {
            System.err.println("✗ Error removing item: " + ex.getMessage());
        }
    }

    private void clearCart() {
        
         try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement checkStmt = con.prepareStatement(
                 "SELECT COUNT(*) FROM cart WHERE user_id = ?")) {

        checkStmt.setInt(1, currentUser.getId());
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            JOptionPane.showMessageDialog(this,
                    " Cart is already empty!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return; // ❗ STOP HERE
        }

    } catch (SQLException ex) {
        System.err.println(" Error checking cart: " + ex.getMessage());
        return;
    }

        int option = JOptionPane.showConfirmDialog(this, "Clear entire cart?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement("DELETE FROM cart WHERE user_id = ?")) {
                stmt.setInt(1, currentUser.getId());
                stmt.executeUpdate();

                loadCart();
                refreshCallback.run();
            } catch (SQLException ex) {
                System.err.println("✗ Error clearing cart: " + ex.getMessage());
            }
        }
    }

    private void proceedToCheckout() {
        // RELOAD CART BEFORE CHECKOUT
        loadCart();

        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("✓ Proceeding to checkout with " + cartItems.size() + " items");
        System.out.println("✓ Cart items: ");
        for (CartItem item : cartItems) {
            System.out.println("  - " + item.getProduct().getName() + " x" + item.getQuantity());
        }

        new CheckoutFrame(currentUser, new ArrayList<>(cartItems));
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    public double getTotal() {
        return cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}