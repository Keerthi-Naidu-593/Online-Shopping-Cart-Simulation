# 🛍️ Shopping Cart Application (ShopVibe)

A complete **Java Swing-based e-commerce application** with user authentication, product management, shopping cart, and order history features.

---

## 📋 Features

### 👤 **User Management**
- ✅ User registration with validation
- ✅ User login with authentication
- ✅ Separate roles: Customer & Admin
- ✅ Mandatory fields: Username, Email, Phone (10 digits), Address

### 🛒 **Shopping Features**
- ✅ Browse products by category
- ✅ Search products by name
- ✅ Add products to cart
- ✅ Update cart quantities (no duplicates)
- ✅ Remove items from cart
- ✅ View total amount in ₹ (Indian Rupee)
- ✅ Checkout with PDF bill generation

### 📦 **Admin Features**
- ✅ Add new products
- ✅ Update product details
- ✅ Delete products
- ✅ View all orders
- ✅ View order items
- ✅ Export orders to text file

### 📄 **Order Management**
- ✅ Generate PDF bills after checkout
- ✅ Auto-open PDF for download
- ✅ View order history
- ✅ Re-download previous bills anytime

### 💰 **Currency**
- ✅ All prices in **₹ (Indian Rupee)**
- ✅ Professional formatting

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| **Frontend** | Java Swing GUI |
| **Backend** | Java |
| **Database** | MySQL 8.0+ |
| **PDF Generation** | iTextPDF 5.5.13.3 |
| **JDBC Driver** | MySQL Connector 9.6.0 |
| **Build Tool** | Apache Ant |

---

## 📦 Project Structure

```
ShoppingCartApp/
├── src/
│   ├── models/
│   │   ├── User.java
│   │   ├── Product.java
│   │   ├── CartItem.java
│   │   └── Order.java
│   ├── database/
│   │   └── DatabaseConnection.java
│   ├── ui/
│   │   ├── UITheme.java
│   │   ├── LoginFrame.java
│   │   ├── SignupFrame.java
│   │   ├── CustomerDashboard.java
│   │   ├── CustomerSettingsPanel.java
│   │   ├── AdminDashboard.java
│   │   ├── AdminUserPanel.java
│   │   ├── ProductCatalogPanel.java
│   │   ├── ShoppingCartPanel.java
│   │   ├── AdminProductPanel.java
│   │   ├── AdminOrdersPanel.java
│   │   ├── OrderHistoryPanel.java
│   │   └── CheckoutFrame.java
│   ├── utils/
│   │   └── BillGenerator.java
│   └── Main.java
├── lib/
│   ├── mysql-connector-j-9.6.0.jar
│   └── itextpdf-5.5.13.3.jar
├── build.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 11 or higher**
- **MySQL 8.0 or higher**
- **Apache Ant**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Keerthi-Naidu-593/Online-Shopping-Cart-Simulation.git
   cd shopping-cart-app
   ```

2. **Setup MySQL Database**
   ```bash
   mysql -u root -p < database.sql
   ```
   
   Or run the SQL manually:
   ```sql
   CREATE DATABASE shopping_cart_db;
   USE shopping_cart_db;
   
   -- Run all CREATE TABLE statements from setup.sql
   ```

3. **Configure Database Connection**
   - Edit `src/database/DatabaseConnection.java`
   - Update database URL, username, and password if needed

4. **Add Libraries**
   - Place JAR files in `lib/` folder:
     - `mysql-connector-j-8.0.33.jar`
     - `itextpdf-5.5.13.3.jar`

5. **Build the Project**
   ```bash
   ant build
   ```

6. **Run the Application**
   ```bash
   ant run
   ```

---

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL,
    role VARCHAR(20) DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Products Table
```sql
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    category VARCHAR(50)
);
```

### Cart Table
```sql
CREATE TABLE cart (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    UNIQUE KEY unique_cart (user_id, product_id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(product_id) REFERENCES products(id)
);
```

### Orders Table
```sql
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'completed',
    FOREIGN KEY(user_id) REFERENCES users(id)
);
```

### Order Items Table
```sql
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY(order_id) REFERENCES orders(id),
    FOREIGN KEY(product_id) REFERENCES products(id)
);
```

---

## 👤 Default Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin123`
- Email: `admin@shopping.com`

---

## 📱 Usage

### Customer Flow
1. **Register** → Create new account with all required details
2. **Login** → Sign in with username & password
3. **Browse** → View products by category or search
4. **Add to Cart** → Select quantity and add items
5. **Checkout** → Review order and complete purchase
6. **Download Bill** → PDF bill auto-opens after checkout
7. **View History** → Check previous orders and re-download bills

### Admin Flow
1. **Login** → Sign in with admin credentials
2. **Manage Products** → Add, Update, Delete products
3. **View Orders** → See all customer orders
4. **Export** → Export orders to text file

---

## 🎨 UI Features

- ✅ Modern, clean interface with Swing components
- ✅ Responsive design with proper layouts
- ✅ Color-coded buttons (Blue, Green, Red, Yellow)
- ✅ Error messages with clear feedback
- ✅ Professional typography with Segoe UI/Arial fonts
- ✅ Non-editable, non-resizable tables for data integrity
- ✅ Smooth navigation between screens

---

## 🔐 Security Features

- ✅ Password hashing (can be enhanced with bcrypt)
- ✅ SQL Prepared Statements (prevents SQL injection)
- ✅ Role-based access control
- ✅ Non-editable tables
- ✅ Input validation
- ✅ Database constraints

---

## 📝 Sample Data

The application includes **25 sample products** across 5 categories:
- **Electronics** (5 items)
- **Clothing** (5 items)
- **Books** (5 items)
- **Home & Garden** (5 items)
- **Sports** (5 items)

---

## 🐛 Known Issues

None currently! Please report any bugs.

---

## 🚦 Future Enhancements

- [ ] Payment gateway integration
- [ ] Email notifications
- [ ] Product reviews & ratings
- [ ] Wishlist feature
- [ ] Discount codes
- [ ] Inventory tracking
- [ ] Multiple user addresses
- [ ] Order tracking
- [ ] Admin dashboard with analytics

---

## 📄 License

This project is open source and available under the MIT License.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@Keerthi-Naidu-593](https://github.com/Keerthi-Naidu-593)
- Email: nkeerthi539@gmail.com

---

## 📞 Support

For support, email nkeerthi539@gmail.com or open an issue on GitHub.

---

## ✨ Acknowledgments

- Built with Java Swing
- Database: MySQL
- PDF Generation: iText
- Thanks to the open-source community!

---

**Happy Shopping! 🛍️**
