/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KEERTHI
 */
package ui;

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

public class ProductCatalogPanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JSpinner quantitySpinner;
    private List<Product> allProducts;
    private User currentUser;
    private Runnable cartUpdateCallback;
    private JLabel cartCountLabel;

    public ProductCatalogPanel(User currentUser, Runnable cartUpdateCallback) {
        this.currentUser = currentUser;
        this.cartUpdateCallback = cartUpdateCallback;
        this.allProducts = new ArrayList<>();

        setupUI();
        loadProducts();
        updateCartCount();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Product Catalog");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        headerPanel.add(heading, gbc);
        
        
        UIManager.put("Label.font", new Font("Arial", Font.BOLD, 15));
        UIManager.put("Label.foreground", new Color(0, 120, 2));
        // Search Section
        JLabel searchLabel = new JLabel("Search:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        headerPanel.add(searchLabel, gbc);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        headerPanel.add(searchField, gbc);

        JButton searchBtn = UITheme.createPrimaryButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 35));
        searchBtn.addActionListener(e -> filterProducts());
        gbc.gridx = 2;
        headerPanel.add(searchBtn, gbc);

        cartCountLabel = new JLabel("Cart: 0 items");
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 13));
        cartCountLabel.setForeground(new Color(46, 204, 113));
        gbc.gridx = 3;
        headerPanel.add(cartCountLabel, gbc);

        // Category Filter
        JLabel categoryLabel = new JLabel("Category:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        headerPanel.add(categoryLabel, gbc);

        String[] categories = {"All Categories", "Electronics", "Clothing", "Books", "Home & Garden", "Sports"};
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setBackground(Color.WHITE);
        categoryFilter.addActionListener(e -> filterProducts());
        gbc.gridx = 1;
        headerPanel.add(categoryFilter, gbc);

        JButton resetBtn = UITheme.createSecondaryButton("Reset");
        resetBtn.setPreferredSize(new Dimension(100, 35));
        resetBtn.addActionListener(e -> resetFilters());
        gbc.gridx = 2;
        headerPanel.add(resetBtn, gbc);

        add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(240, 240, 240));

        String[] columns = {"ID", "Product Name", "Category", "Price (₹)", "Stock"};
        tableModel = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
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
        productTable.setRowHeight(35);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setBackground(Color.WHITE);
        productTable.setGridColor(new Color(224, 224, 224));
        productTable.setFont(new Font("Arial", Font.PLAIN, 12));
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        productTable.getTableHeader().setBackground(new Color(44, 62, 80));
        productTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Arial", Font.BOLD, 15));
        bottomPanel.add(qtyLabel);

        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setPreferredSize(new Dimension(80, 35));
        bottomPanel.add(quantitySpinner);

        JButton addBtn = UITheme.createSuccessButton("Add to Cart");
        addBtn.setPreferredSize(new Dimension(150, 45));
        addBtn.addActionListener(e -> addToCart());
        bottomPanel.add(addBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE stock > 0 ORDER BY id");

            allProducts.clear();

            while (rs.next()) {
                Product product = new Product(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category")
                );
                product.setId(rs.getInt("id"));
                allProducts.add(product);
            }

            displayProducts(allProducts);
            System.out.println("✓ Loaded " + allProducts.size() + " products");
        } catch (SQLException ex) {
            System.err.println("✗ Error loading products: " + ex.getMessage());
        }
    }

    private void displayProducts(List<Product> products) {
        tableModel.setRowCount(0);

        for (Product p : products) {
            Object[] row = {
                    p.getId(),
                    "  " + p.getName(),
                    p.getCategory(),
                    "₹ " + String.format("%.2f", p.getPrice()),
                    " " + p.getStock()
            };
            tableModel.addRow(row);
        }
    }

    private void filterProducts() {
        String searchText = searchField.getText().trim().toLowerCase();
        String selectedCategory = (String) categoryFilter.getSelectedItem();

        List<Product> filtered = new ArrayList<>();

        for (Product p : allProducts) {
            boolean matchesSearch = p.getName().toLowerCase().contains(searchText);
            boolean matchesCategory = "All Categories".equals(selectedCategory) || p.getCategory().equals(selectedCategory);

            if (matchesSearch && matchesCategory) {
                filtered.add(p);
            }
        }

        displayProducts(filtered);
    }

    private void resetFilters() {
        searchField.setText("");
        categoryFilter.setSelectedIndex(0);
        loadProducts();
    }

        private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "❌ Select a product!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int productId = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        int quantity = Integer.parseInt(((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().getText());
        Product selectedProduct = allProducts.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);

        if (selectedProduct == null) {
        return;
        }

       if (quantity > selectedProduct.getStock()) {
           JOptionPane.showMessageDialog(this,
            "❌ Only " + selectedProduct.getStock() + " items available!",
            "Error", JOptionPane.ERROR_MESSAGE);
          return;
          }

        try (Connection con = DatabaseConnection.getConnection()) {
            
            // Check if product already exists in cart
            PreparedStatement checkStmt = con.prepareStatement(
                    "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?");
            checkStmt.setInt(1, currentUser.getId());
            checkStmt.setInt(2, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Product already in cart - UPDATE quantity
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + quantity;

                // Check if new quantity exceeds stock
                if (newQuantity > selectedProduct.getStock()) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Cannot add! Only " + selectedProduct.getStock() + " items available.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement updateStmt = con.prepareStatement(
                        "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?");
                updateStmt.setInt(1, newQuantity);
                updateStmt.setInt(2, currentUser.getId());
                updateStmt.setInt(3, productId);
                updateStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, 
                    "✅ Quantity updated! Now " + newQuantity + "x " + selectedProduct.getName(), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                // Product NOT in cart - INSERT new
                PreparedStatement insertStmt = con.prepareStatement(
                        "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)");
                insertStmt.setInt(1, currentUser.getId());
                insertStmt.setInt(2, productId);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, 
                    "✅ " + quantity + "x " + selectedProduct.getName() + " added!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            quantitySpinner.setValue(1);
            productTable.clearSelection();
            updateCartCount();
            cartUpdateCallback.run();

        } catch (SQLException ex) {
            System.err.println("✗ Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "❌ Error adding to cart!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void updateCartCount() {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) as cnt FROM cart WHERE user_id = ?")) {

            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt("cnt");
            }

            cartCountLabel.setText("Cart: " + count + " items");
        } catch (SQLException ex) {
            System.err.println("✗ Error updating cart count");
        }
    }
}