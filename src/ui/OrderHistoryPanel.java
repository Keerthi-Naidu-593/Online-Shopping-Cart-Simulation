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
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.JTableHeader;

public class OrderHistoryPanel extends JPanel {
    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private User currentUser;
    private List<Integer> orderIds;

    public OrderHistoryPanel(User currentUser) {
        this.currentUser = currentUser;
        this.orderIds = new ArrayList<>();

        setupUI();
        loadOrderHistory();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Order History");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
        add(heading, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Date", "Total (₹)", "Status", "Action"};
        tableModel = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        ordersTable = new JTable(tableModel);

        JTableHeader header = ordersTable.getTableHeader();
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
        ordersTable.getTableHeader().setReorderingAllowed(false);
        ordersTable.getTableHeader().setResizingAllowed(false);
        ordersTable.setRowHeight(40);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.setBackground(Color.WHITE);
        ordersTable.setGridColor(new Color(224, 224, 224));
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 12));
        ordersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        ordersTable.getTableHeader().setBackground(new Color(44, 62, 80));
        ordersTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        JButton downloadBtn = UITheme.createPrimaryButton("Download Bill");
        downloadBtn.setPreferredSize(new Dimension(150, 45));
        downloadBtn.addActionListener(e -> downloadBill());
        bottomPanel.add(downloadBtn);

        JButton refreshBtn = UITheme.createSecondaryButton("Refresh");
        refreshBtn.setPreferredSize(new Dimension(120, 45));
        refreshBtn.addActionListener(e -> loadOrderHistory());
        bottomPanel.add(refreshBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadOrderHistory() {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT o.id, o.order_date, o.total_amount, o.status FROM orders o " +
                     "WHERE o.user_id = ? ORDER BY o.order_date DESC")) {

            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);
            orderIds.clear();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                orderIds.add(orderId);

                Object[] row = {
                        orderId,
                        rs.getString("order_date"),
                        "₹ " + String.format("%.2f", rs.getDouble("total_amount")),
                        rs.getString("status"),
                        "Download"
                };
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No orders found!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            System.err.println("✗ Error loading orders: " + ex.getMessage());
        }
    }

    private void downloadBill() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "❌ Select an order first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT oi.product_id, oi.quantity, oi.price, p.name FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?")) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            List<models.CartItem> items = new ArrayList<>();
            double total = 0;

            while (rs.next()) {
                models.Product product = new models.Product("", "", rs.getDouble("price"), 0, "");
                product.setId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));

                models.CartItem item = new models.CartItem(product, rs.getInt("quantity"));
                items.add(item);
                total += item.getSubtotal();
            }

            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ No items found for this order!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BillGenerator.generatePdfBill(orderId, currentUser, items, total);
            JOptionPane.showMessageDialog(this, "✅ Bill PDF opened!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            System.err.println("✗ Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "❌ Error downloading bill!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}