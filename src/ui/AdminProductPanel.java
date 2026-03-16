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
import javax.swing.table.JTableHeader;

public class AdminProductPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, stockField, descField;
    private JComboBox<String> categoryField;
    private int selectedProductId = -1;
    private JLabel statusLabel;
   
    public AdminProductPanel() {
        setupUI();
        loadProducts();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Heading
        JLabel heading = new JLabel("Manage Products");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
        add(heading, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Category", "Price (₹)", "Stock"};
        tableModel = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        
        JTableHeader header = productTable.getTableHeader();
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
        productTable.getTableHeader().setReorderingAllowed(false);
        productTable.getTableHeader().setResizingAllowed(false);
        productTable.setRowHeight(30);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setBackground(Color.WHITE);
        productTable.setGridColor(new Color(224, 224, 224));
        productTable.setFont(new Font("Arial", Font.PLAIN, 12));
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        productTable.getTableHeader().setBackground(new Color(44, 62, 80));
        productTable.getTableHeader().setForeground(Color.WHITE);

        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    selectedProductId = (Integer) tableModel.getValueAt(row, 0);
                    loadSelectedProduct();
                    statusLabel.setText("*Product selected. Click Update or Delete.");
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 15));
        UIManager.put("Label.foreground", new Color(0, 120, 215));
        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Product Name:"), gbc);
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN,13 ));
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Category:"), gbc);
        String[] cats = {"Electronics", "Clothing", "Books", "Home & Garden", "Sports"};
        categoryField = new JComboBox<>(cats);
        categoryField.setBackground(Color.WHITE);
        gbc.gridx = 3;
        formPanel.add(categoryField, gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        descField = new JTextField(15);
        descField.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(descField, gbc);

        // Row 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Price (₹):"), gbc);
        priceField = new JTextField(10);
        priceField.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Stock:"), gbc);
        stockField = new JTextField(10);
        stockField.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 3;
        formPanel.add(stockField, gbc);

        // Status Label
        statusLabel = new JLabel("Select a product from table to edit");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(0, 120, 2));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        formPanel.add(statusLabel, gbc);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addBtn = UITheme.createSuccessButton("Add Product");
        addBtn.addActionListener(e -> addProduct());
        btnPanel.add(addBtn);

        JButton updateBtn = UITheme.createPrimaryButton("Update");
        updateBtn.addActionListener(e -> updateProduct());
        btnPanel.add(updateBtn);

        JButton deleteBtn = UITheme.createDangerButton("Delete");
        deleteBtn.addActionListener(e -> deleteProduct());
        btnPanel.add(deleteBtn);

        JButton clearBtn = UITheme.createWarningButton("Clear");
        clearBtn.addActionListener(e -> clearForm());
        btnPanel.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM products ORDER BY id");
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    "₹ " + String.format("%.2f", rs.getDouble("price")),
                    rs.getInt("stock")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedProduct() {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM products WHERE id = ?")) {
            stmt.setInt(1, selectedProductId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                descField.setText(rs.getString("description"));
                priceField.setText(String.valueOf(rs.getDouble("price")));
                stockField.setText(String.valueOf(rs.getInt("stock")));
                categoryField.setSelectedItem(rs.getString("category"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProduct() {
        if (nameField.getText().isEmpty() || priceField.getText().isEmpty() || stockField.getText().isEmpty()) {
            statusLabel.setText("❌ Fill all required fields");
            JOptionPane.showMessageDialog(this, "Fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "INSERT INTO products (name, description, price, stock, category) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, nameField.getText());
            stmt.setString(2, descField.getText());
            stmt.setDouble(3, Double.parseDouble(priceField.getText()));
            stmt.setInt(4, Integer.parseInt(stockField.getText()));
            stmt.setString(5, (String) categoryField.getSelectedItem());
            stmt.executeUpdate();
            statusLabel.setText("✓ Product added successfully");
            JOptionPane.showMessageDialog(this, "✓ Product added", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadProducts();
            clearForm();
        } catch (SQLException ex) {
            statusLabel.setText("❌ Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        if (selectedProductId == -1) {
            statusLabel.setText("❌ Select a product first");
            JOptionPane.showMessageDialog(this, "Select a product first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "UPDATE products SET name=?, description=?, price=?, stock=?, category=? WHERE id=?")) {
            stmt.setString(1, nameField.getText());
            stmt.setString(2, descField.getText());
            stmt.setDouble(3, Double.parseDouble(priceField.getText()));
            stmt.setInt(4, Integer.parseInt(stockField.getText()));
            stmt.setString(5, (String) categoryField.getSelectedItem());
            stmt.setInt(6, selectedProductId);
            stmt.executeUpdate();
            statusLabel.setText("✓ Product updated successfully");
            JOptionPane.showMessageDialog(this, "✓ Product updated", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadProducts();
            clearForm();
        } catch (SQLException ex) {
            statusLabel.setText("❌ Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        if (selectedProductId == -1) {
            statusLabel.setText("❌ Select a product first");
            JOptionPane.showMessageDialog(this, "Select a product first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("DELETE FROM products WHERE id=?")) {
            stmt.setInt(1, selectedProductId);
            stmt.executeUpdate();
            statusLabel.setText("✓ Product deleted successfully");
            JOptionPane.showMessageDialog(this, "✓ Product deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadProducts();
            clearForm();
        } catch (SQLException ex) {
            statusLabel.setText("❌ Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        descField.setText("");
        priceField.setText("");
        stockField.setText("");
        categoryField.setSelectedIndex(0);
        selectedProductId = -1;
        productTable.clearSelection();
        statusLabel.setText("Select a product from table to edit");
    }
}