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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.JTableHeader;

public class AdminOrdersPanel extends JPanel {
    private JTable ordersTable, itemsTable;
    private DefaultTableModel ordersModel, itemsModel;

    public AdminOrdersPanel() {
        setupUI();
        loadOrders();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Order Management");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
        add(heading, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(new Color(240, 240, 240));

        // Left Panel - Orders
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(new Color(240, 240, 240));

        JLabel ordersLabel = new JLabel("Recent Orders");
        ordersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        leftPanel.add(ordersLabel, BorderLayout.NORTH);

        String[] ordersCols = {"Order ID", "Customer", "Date", "Total (₹)", "Status"};
        ordersModel = new DefaultTableModel(new Object[][]{}, ordersCols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(ordersModel);
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
        ordersTable.setRowHeight(30);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.setBackground(Color.WHITE);
        ordersTable.setGridColor(new Color(224, 224, 224));
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 11));
        ordersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        ordersTable.getTableHeader().setBackground(new Color(44, 62, 80));
        ordersTable.getTableHeader().setForeground(Color.WHITE);

        ordersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loadOrderItems();
            }
        });

        JScrollPane ordersScroll = new JScrollPane(ordersTable);
        ordersScroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        leftPanel.add(ordersScroll, BorderLayout.CENTER);

        // Right Panel - Order Items
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 240, 240));

        JLabel itemsLabel = new JLabel("Order Items");
        itemsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(itemsLabel, BorderLayout.NORTH);

        String[] itemsCols = {"Product", "Qty", "Price (₹)", "Subtotal (₹)"};
        itemsModel = new DefaultTableModel(new Object[][]{}, itemsCols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemsTable = new JTable(itemsModel);
        header = itemsTable.getTableHeader();
        header.setPreferredSize(new Dimension(100, 35));

         header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
    {
        setHorizontalAlignment(JLabel.CENTER);
        setBackground(new Color(44, 62, 80));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 13));
        //itemsTable.setGridColor(new Color(224, 224, 224));
        setOpaque(true);
    }
        });
        itemsTable.getTableHeader().setReorderingAllowed(false);
        itemsTable.getTableHeader().setResizingAllowed(false);
        itemsTable.setRowHeight(30);
        itemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemsTable.setBackground(Color.WHITE);
        itemsTable.setGridColor(new Color(224, 224, 224));
        itemsTable.setFont(new Font("Arial", Font.PLAIN, 11));
        itemsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        itemsTable.getTableHeader().setBackground(new Color(44, 62, 80));
        itemsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane itemsScroll = new JScrollPane(itemsTable);
        itemsScroll.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        rightPanel.add(itemsScroll, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(450);
        add(splitPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        JButton refreshBtn = UITheme.createPrimaryButton("Refresh");
        refreshBtn.addActionListener(e -> {
            loadOrders();
            itemsModel.setRowCount(0);
        });
        bottomPanel.add(refreshBtn);

        JButton exportBtn = UITheme.createSecondaryButton("Export");
        exportBtn.addActionListener(e -> exportOrders());
        bottomPanel.add(exportBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT o.id, u.username, o.order_date, o.total_amount, o.status " +
                     "FROM orders o JOIN users u ON o.user_id = u.id " +
                     "ORDER BY o.order_date DESC LIMIT 100")) {

            ResultSet rs = stmt.executeQuery();
            ordersModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("order_date"),
                        "₹ " + String.format("%.2f", rs.getDouble("total_amount")),
                        rs.getString("status")
                };
                ordersModel.addRow(row);
            }

            System.out.println("✓ Loaded orders");
        } catch (SQLException ex) {
            System.err.println("✗ Error loading orders: " + ex.getMessage());
        }
    }

    private void loadOrderItems() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow < 0) return;

        int orderId = (Integer) ordersModel.getValueAt(selectedRow, 0);

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT p.name, oi.quantity, oi.price FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?")) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            itemsModel.setRowCount(0);

            while (rs.next()) {
                double subtotal = rs.getInt("quantity") * rs.getDouble("price");
                Object[] row = {
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        "₹ " + String.format("%.2f", rs.getDouble("price")),
                        "₹ " + String.format("%.2f", subtotal)
                };
                itemsModel.addRow(row);
            }
        } catch (SQLException ex) {
            System.err.println("✗ Error loading order items: " + ex.getMessage());
        }
    }

    private void exportOrders() {
        try {
            String fileName = "Orders_" + System.currentTimeMillis() + ".txt";
            java.io.FileWriter writer = new java.io.FileWriter(fileName);

            writer.write("════════════════════════════════════════════\n");
            writer.write("           ORDERS EXPORT REPORT             \n");
            writer.write("════════════════════════════════════════════\n\n");
            writer.write("Export Date: " + LocalDateTime.now() + "\n\n");

            writer.write(String.format("%-12s %-25s %-20s %-15s %-10s\n", 
                    "Order ID", "Customer", "Date", "Total (₹)", "Status"));
            writer.write("-".repeat(82) + "\n");

            for (int i = 0; i < ordersModel.getRowCount(); i++) {
                writer.write(String.format("%-12s %-25s %-20s %-15s %-10s\n",
                        ordersModel.getValueAt(i, 0),
                        ordersModel.getValueAt(i, 1),
                        ordersModel.getValueAt(i, 2),
                        ordersModel.getValueAt(i, 3),
                        ordersModel.getValueAt(i, 4)
                ));
            }

            writer.write("\n" + "=".repeat(82) + "\n");
            writer.write("Total Orders: " + ordersModel.getRowCount() + "\n");
            writer.close();

            Desktop.getDesktop().open(new java.io.File(fileName));
            JOptionPane.showMessageDialog(this, "✓ Orders exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}