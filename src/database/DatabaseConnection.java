/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author KEERTHI
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/shopping_cart_db?serverTimezone=UTC&useSSL=false",
                    "root",
                    ""
            );
            System.out.println("✓ Database connected successfully!");
        } catch (Exception e) {
            System.out.println("✗ Database connection failed!");
            e.printStackTrace();
        }
        return con;
    }
}