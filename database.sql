-- ============================================
-- SHOPPING CART DATABASE SETUP
-- ============================================

-- Drop database if it exists (optional)
DROP DATABASE IF EXISTS shopping_cart_db;

-- Create database
CREATE DATABASE shopping_cart_db;
USE shopping_cart_db;

-- ============================================
-- USERS TABLE
-- ============================================
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    role VARCHAR(20) DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role)
);

-- ============================================
-- PRODUCTS TABLE
-- ============================================
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_stock (stock)
);

-- ============================================
-- SHOPPING CART TABLE
-- ============================================
CREATE TABLE cart (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_product (product_id),
    UNIQUE KEY unique_cart (user_id, product_id)
);

-- ============================================
-- ORDERS TABLE
-- ============================================
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'completed',
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_date (order_date),
    INDEX idx_status (status)
);

-- ============================================
-- ORDER ITEMS TABLE
-- ============================================
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_order (order_id),
    INDEX idx_product (product_id)
);

-- ============================================
-- INSERT ADMIN USER
-- ============================================
INSERT INTO users (username, password, email, phone, address, role) 
VALUES ('admin', 'admin123', 'admin@shopping.com', '9999999999', 'Admin Building, Main Street', 'admin');

-- ============================================
-- INSERT SAMPLE PRODUCTS
-- ============================================
INSERT INTO products (name, description, price, stock, category) VALUES
('Laptop', 'High-performance laptop with 16GB RAM and 512GB SSD', 999.99, 15, 'Electronics'),
('Smartphone', 'Latest model smartphone with 5G connectivity', 699.99, 25, 'Electronics'),
('Wireless Headphones', 'Premium wireless noise-cancelling headphones', 149.99, 50, 'Electronics'),
('USB-C Cable', 'Fast charging USB-C cable 2 meters', 19.99, 100, 'Electronics'),
('Laptop Stand', 'Adjustable aluminum laptop stand', 39.99, 40, 'Electronics'),

('Blue T-Shirt', 'Cotton casual blue t-shirt - Size M/L/XL', 29.99, 100, 'Clothing'),
('Black Jeans', 'Premium blue denim jeans with comfort fit', 59.99, 80, 'Clothing'),
('Casual Jacket', 'Lightweight casual jacket perfect for all seasons', 89.99, 35, 'Clothing'),
('Cotton Socks Pack', 'Pack of 5 pairs comfortable cotton socks', 14.99, 150, 'Clothing'),
('Leather Belt', 'Premium leather belt with stainless steel buckle', 34.99, 45, 'Clothing'),

('Java Programming Book', 'Learn Java programming in 30 days - 500+ pages', 45.99, 30, 'Books'),
('Python Basics', 'Complete Python guide for beginners', 39.99, 25, 'Books'),
('Web Development', 'HTML, CSS, JavaScript mastery course guide', 54.99, 20, 'Books'),
('Database Design', 'SQL and database design principles', 49.99, 18, 'Books'),
('Cloud Computing', 'AWS and cloud architecture fundamentals', 59.99, 22, 'Books'),

('Running Shoes', 'Professional athletic running shoes for marathon', 89.99, 40, 'Sports'),
('Basketball', 'Official size basketball - indoor/outdoor', 44.99, 30, 'Sports'),
('Yoga Mat', 'Non-slip yoga mat with carrying strap', 24.99, 60, 'Sports'),
('Dumbbells Set', '5kg dumbbells set for home gym - pair', 34.99, 50, 'Sports'),
('Water Bottle', 'Stainless steel water bottle 1 liter', 19.99, 80, 'Sports'),

('LED Desk Lamp', 'LED desk lamp with USB charging port', 39.99, 35, 'Home & Garden'),
('Desk Organizer', 'Wooden desk organizer with 5 compartments', 24.99, 55, 'Home & Garden'),
('Plant Pot', 'Ceramic plant pot with drainage - 8 inch', 14.99, 70, 'Home & Garden'),
('Wall Shelf', 'Floating wall shelf made of wood - 24 inch', 44.99, 25, 'Home & Garden'),
('Desk Chair', 'Ergonomic office chair with lumbar support', 149.99, 15, 'Home & Garden');

-- ============================================
-- VERIFY TABLES AND DATA
-- ============================================
SELECT 'USERS' AS Table_Name, COUNT(*) AS Record_Count FROM users
UNION ALL
SELECT 'PRODUCTS', COUNT(*) FROM products
UNION ALL
SELECT 'CART', COUNT(*) FROM cart
UNION ALL
SELECT 'ORDERS', COUNT(*) FROM orders
UNION ALL
SELECT 'ORDER_ITEMS', COUNT(*) FROM order_items;

-- ============================================
-- DISPLAY ADMIN USER (for testing)
-- ============================================
SELECT 'Admin User:' AS Info, username, email, role FROM users WHERE role = 'admin';

-- ============================================
-- DISPLAY SAMPLE PRODUCTS BY CATEGORY
-- ============================================
SELECT 'Electronics:' AS Category, COUNT(*) AS Count FROM products WHERE category = 'Electronics'
UNION ALL
SELECT 'Clothing', COUNT(*) FROM products WHERE category = 'Clothing'
UNION ALL
SELECT 'Books', COUNT(*) FROM products WHERE category = 'Books'
UNION ALL
SELECT 'Sports', COUNT(*) FROM products WHERE category = 'Sports'
UNION ALL
SELECT 'Home & Garden', COUNT(*) FROM products WHERE category = 'Home & Garden'; 
