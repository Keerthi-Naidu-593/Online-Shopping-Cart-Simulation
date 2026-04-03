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
import utils.BillGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import models.CartItem;           // ADD THIS
import models.Product;  
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.JLabel;
import java.awt.Component;

public class OrderHistoryPanel extends JPanel {
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private User currentUser;
    private List<OrderInfo> orders;

    public OrderHistoryPanel(User currentUser) {
        this.currentUser = currentUser;
        this.orders = new ArrayList<>();

        setupUI();
        loadOrders();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Heading
        JLabel heading = new JLabel("Order History");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
        add(heading, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(240, 240, 240));

        String[] columns = {"Order ID", "Order Date", "Total (₹)", "Items", "Preview", "Download"};
        tableModel = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Only Preview & Download columns are editable
            }
        };

        
        ordersTable = new JTable(tableModel);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        ordersTable.getTableHeader().setResizingAllowed(false);
        ordersTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        int row = ordersTable.rowAtPoint(e.getPoint());
        int col = ordersTable.columnAtPoint(e.getPoint());

        ordersTable.repaint(); // refresh table to update hover effect
    }
});
        ordersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            c.setBackground(new Color(220, 240, 255));
        } else {
            c.setBackground(Color.WHITE);
        }

        setForeground(Color.BLACK);
        return c;
    }
});
        
        

// ✅ ADD HERE
JTableHeader header = ordersTable.getTableHeader();
header.setPreferredSize(new Dimension(100, 40));

header.setDefaultRenderer(new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBackground(new Color(44, 62, 80));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setOpaque(true);

        return label;
    }
});
        ordersTable.setRowHeight(40);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.setBackground(Color.WHITE);
        ordersTable.setGridColor(new Color(224, 224, 224));
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ordersTable.setOpaque(true);
        ordersTable.setFillsViewportHeight(true);

        // Custom renderer for Preview button
        ordersTable.getColumn("Preview").setCellRenderer(new ButtonRenderer());
        ordersTable.getColumn("Preview").setCellEditor(new PreviewButtonEditor(new JCheckBox(), this));

        // Custom renderer for Download button
        ordersTable.getColumn("Download").setCellRenderer(new ButtonRenderer());
        ordersTable.getColumn("Download").setCellEditor(new DownloadButtonEditor(new JCheckBox(), this));

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);
        
    }
      private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
    button.setBackground(normalColor);

    button.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            button.setBackground(hoverColor);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            button.setBackground(normalColor);
        }
    });
}
    private void loadOrders() {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT id, order_date, total_amount FROM orders WHERE user_id = ? ORDER BY order_date DESC")) {

            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            orders.clear();
            tableModel.setRowCount(0);

            while (rs.next()) {
                int orderId = rs.getInt("id");
                String orderDate = rs.getString("order_date");
                double totalAmount = rs.getDouble("total_amount");
                int itemCount = getOrderItemCount(orderId);

                OrderInfo order = new OrderInfo(orderId, orderDate, totalAmount, itemCount);
                orders.add(order);

                Object[] row = {
                        orderId,
                        orderDate,
                        "₹ " + String.format("%.2f", totalAmount),
                        itemCount + " items",
                        "️ Preview",
                        "️ Download"
                };
                tableModel.addRow(row);
            }

            System.out.println("✓ Loaded " + orders.size() + " orders");

        } catch (SQLException ex) {
            System.err.println("✗ Error loading orders: " + ex.getMessage());
        }
    }

    private int getOrderItemCount(int orderId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) as cnt FROM order_items WHERE order_id = ?")) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException ex) {
            System.err.println("✗ Error: " + ex.getMessage());
        }
        return 0;
    }

    private void previewBill(int orderId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT oi.product_id, p.name, oi.quantity, oi.price FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?")) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            // Get order details
            PreparedStatement orderStmt = con.prepareStatement("SELECT total_amount, order_date FROM orders WHERE id = ?");
            orderStmt.setInt(1, orderId);
            ResultSet orderRs = orderStmt.executeQuery();

            if (!orderRs.next()) {
                JOptionPane.showMessageDialog(this, "Order not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalAmount = orderRs.getDouble("total_amount");
            String orderDate = orderRs.getString("order_date");

            // Build preview content
            StringBuilder previewText = new StringBuilder();
            previewText.append("╔════════════════════════════════════════╗\n");
            previewText.append("║          ORDER BILL PREVIEW            ║\n");
            previewText.append("╚════════════════════════════════════════╝\n\n");

            previewText.append("Order ID: ").append(orderId).append("\n");
            previewText.append("Date: ").append(orderDate).append("\n");
            previewText.append("Customer: ").append(currentUser.getUsername()).append("\n");
            previewText.append("Email: ").append(currentUser.getEmail()).append("\n");
            previewText.append("Phone: ").append(currentUser.getPhone()).append("\n");
            previewText.append("Address: ").append(currentUser.getAddress()).append("\n\n");

            previewText.append("────────────────────────────────────────\n");
            previewText.append(String.format("%-30s %10s\n", "Product", "Subtotal"));
            previewText.append("────────────────────────────────────────\n");

            while (rs.next()) {
                String productName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double subtotal = quantity * price;

                previewText.append(String.format("%-20s x%d = ₹%-8.2f\n", 
                    productName.substring(0, Math.min(20, productName.length())), 
                    quantity, subtotal));
            }

            previewText.append("────────────────────────────────────────\n");
            previewText.append(String.format("%-30s ₹%.2f\n", "TOTAL AMOUNT:", totalAmount));
            previewText.append("════════════════════════════════════════\n\n");
            previewText.append("Thank you for your purchase! ✨\n");

            // Show preview dialog
            JDialog previewDialog = new JDialog();
            previewDialog.setTitle("Bill Preview - Order #" + orderId);
            previewDialog.setSize(500, 600);
            previewDialog.setLocationRelativeTo(this);
            previewDialog.setModal(true);

            JPanel contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            contentPanel.setBackground(Color.WHITE);

            JTextArea textArea = new JTextArea(previewText.toString());
            textArea.setFont(new Font("Courier New", Font.PLAIN, 11));
            textArea.setEditable(false);
            textArea.setBackground(Color.WHITE);
            textArea.setForeground(new Color(44, 62, 80));

            JScrollPane scrollPane = new JScrollPane(textArea);
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            // Buttons
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            btnPanel.setBackground(Color.WHITE);

            JButton downloadBtn = new JButton(" Download PDF");
            downloadBtn.setFont(new Font("Arial", Font.BOLD, 12));
            downloadBtn.setBackground(new Color(46, 204, 113));
            downloadBtn.setForeground(Color.WHITE);
            downloadBtn.setPreferredSize(new Dimension(150, 40));
            addHoverEffect(downloadBtn,
        new Color(46, 204, 113),   // normal green
        new Color(39, 174, 96));   // darker green

// 🔥 IMPORTANT FIXES
            downloadBtn.setFocusPainted(false);
            downloadBtn.setBorderPainted(false);
            downloadBtn.setOpaque(true);

            downloadBtn.addActionListener(e -> {
                   downloadBill(orderId);
                    previewDialog.dispose();
            });
            btnPanel.add(downloadBtn);
           

            JButton closeBtn = new JButton(" Close");
            closeBtn.setFont(new Font("Arial", Font.BOLD, 12));
            closeBtn.setBackground(new Color(231, 76, 60)); 
            closeBtn.setForeground(Color.WHITE);
            closeBtn.setPreferredSize(new Dimension(120, 40));
            addHoverEffect(closeBtn,
        new Color(231, 76, 60),    // normal red
        new Color(192, 57, 43));   // darker red
// 🔥 IMPORTANT FIXES
            closeBtn.setFocusPainted(false);
            closeBtn.setBorderPainted(false);
            closeBtn.setOpaque(true);

            closeBtn.addActionListener(e -> previewDialog.dispose());
            btnPanel.add(closeBtn);

            contentPanel.add(btnPanel, BorderLayout.SOUTH);
            previewDialog.add(contentPanel);
            previewDialog.setVisible(true);

        } catch (SQLException ex) {
            System.err.println("✗ Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading preview!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void downloadBill(int orderId) {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement orderStmt = con.prepareStatement(
                     "SELECT user_id, total_amount FROM orders WHERE id = ?")) {

            orderStmt.setInt(1, orderId);
            ResultSet orderRs = orderStmt.executeQuery();

            if (!orderRs.next()) {
                JOptionPane.showMessageDialog(this, "Order not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalAmount = orderRs.getDouble("total_amount");

            // Get order items
            PreparedStatement itemsStmt = con.prepareStatement(
                    "SELECT oi.product_id, p.name, p.description, oi.quantity, oi.price FROM order_items oi " +
                    "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?");
            itemsStmt.setInt(1, orderId);
            ResultSet itemsRs = itemsStmt.executeQuery();

            List<CartItem> cartItems = new ArrayList<>();
            while (itemsRs.next()) {
                Product product = new Product(
                        itemsRs.getString("name"),
                        itemsRs.getString("description"),
                        itemsRs.getDouble("price"),
                        0, ""
                );
                product.setId(itemsRs.getInt("product_id"));

                CartItem item = new CartItem(product, itemsRs.getInt("quantity"));
                cartItems.add(item);
            }

            // Generate PDF
            BillGenerator.generatePdfBill(orderId, currentUser, cartItems, totalAmount);

            JOptionPane.showMessageDialog(this, 
                "✅ Bill downloaded successfully!\n\n" +
                "Order ID: " + orderId + "\n" +
                "File saved in your Downloads folder", 
                "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            System.err.println("✗ Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error downloading bill!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== BUTTON RENDERER =====
      private static class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        setText(value == null ? "" : value.toString());

        Point mousePoint = table.getMousePosition();
        boolean isHovered = false;

        if (mousePoint != null) {
            int hoverRow = table.rowAtPoint(mousePoint);
            int hoverCol = table.columnAtPoint(mousePoint);
            isHovered = (hoverRow == row && hoverCol == column);
        }

        if (column == 4) { // Preview
            if (isHovered) {
                setBackground(new Color(41, 128, 185)); // darker blue
            } else {
                setBackground(new Color(52, 152, 219)); // normal blue
            }
        } else { // Download
            if (isHovered) {
                setBackground(new Color(39, 174, 96)); // darker green
            } else {
                setBackground(new Color(46, 204, 113)); // normal green
            }
        }

        return this;
    }
}

    // ===== PREVIEW BUTTON EDITOR =====
   private class PreviewButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private int clickedRow;
    private OrderHistoryPanel parentPanel;

    public PreviewButtonEditor(JCheckBox checkBox, OrderHistoryPanel parentPanel) {
        super(checkBox);
        this.parentPanel = parentPanel;

        button = new JButton();
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        addHoverEffect(button,
        new Color(52, 152, 219),   // normal blue
        new Color(41, 128, 185));  // darker blue
        button.addActionListener(e -> {
            int orderId = (Integer) ordersTable.getValueAt(clickedRow, 0);
            parentPanel.previewBill(orderId);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clickedRow = row;

        // 🔥 Important: set color here (not only constructor)
        button.setBackground(new Color(52, 152, 219));

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}

    // ===== DOWNLOAD BUTTON EDITOR =====
   private class DownloadButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private int clickedRow;
    private OrderHistoryPanel parentPanel;

    public DownloadButtonEditor(JCheckBox checkBox, OrderHistoryPanel parentPanel) {
        super(checkBox);
        this.parentPanel = parentPanel;

        button = new JButton();
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        addHoverEffect(button,
        new Color(46, 204, 113),   // normal green
        new Color(39, 174, 96));   // darker green
        button.addActionListener(e -> {
            int orderId = (Integer) ordersTable.getValueAt(clickedRow, 0);
            parentPanel.downloadBill(orderId);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clickedRow = row;

        // 🔥 Important: set color here
        button.setBackground(new Color(46, 204, 113));

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}

    // ===== HELPER CLASSES =====
    private static class OrderInfo {
        int orderId;
        String orderDate;
        double totalAmount;
        int itemCount;

        OrderInfo(int orderId, String orderDate, double totalAmount, int itemCount) {
            this.orderId = orderId;
            this.orderDate = orderDate;
            this.totalAmount = totalAmount;
            this.itemCount = itemCount;
        }
    }
}