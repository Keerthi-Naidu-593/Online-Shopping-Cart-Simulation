/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import database.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.table.JTableHeader;
import models.User;
public class AdminUserPanel extends JPanel {

    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private int selectedUserId = -1;
    private User currentAdmin;
    public AdminUserPanel(User currentUser) {
         this.currentAdmin = currentUser;
        setupUI();
        loadUsers();
    }

    private void setupUI() {
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));

        JLabel heading = new JLabel("Manage Users");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(44, 62, 80));
  
        totalLabel = new JLabel("Total Users: 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));
        totalLabel.setForeground(new Color(46, 204, 113));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(heading, BorderLayout.WEST);
        topPanel.add(totalLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = {"ID", "Name", "Email", "Role"};
        tableModel = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.getTableHeader().setReorderingAllowed(false);
        userTable.getTableHeader().setResizingAllowed(false);
        JTableHeader header = userTable.getTableHeader();
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

        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setBackground(Color.WHITE);
        userTable.setGridColor(new Color(224, 224, 224));
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));

        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = userTable.getSelectedRow();
                if (row >= 0) {
                    selectedUserId = (Integer) tableModel.getValueAt(row, 0);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));

        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel bottomPanel = new JPanel();
        //bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ===== LOAD USERS =====
    private void loadUsers() {
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT id, username, email, role FROM users");

            tableModel.setRowCount(0);
            int count = 0;

            while (rs.next()) {
                count++;
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role")
                });
            }

            totalLabel.setText("Total Users: " + count);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
        }
    }

    
    
}