# SWP391_SE1986-JV_WMS
Laptop Warehouse Management System

# 📦 Warehouse Management System (WMS)

A web-based **Warehouse Management System** built with Java (JSP/Servlet) to manage inventory, product items (serial), and import/export operations efficiently.

---

## 🚀 Features

### 🔐 Authentication & Authorization
- **Login / Logout**: Secure access to the system.
- **Role-based Access Control**: Different permissions for Admin, Manager, and Staff.

### 📦 Product Management
- **CRUD Operations**: Create, update, and delete products.
- **Category Management**: Organize products into categories.
- **Stock Tracking**: Real-time tracking of stock quantities.

### 🔢 Product Item (Serial Management)
- **Serial Tracking**: Manage individual product items using unique serial numbers.
- **Validation**: Prevent duplicate serials during the import process.
- **Status Tracking**: Monitor the status of each item individually.

### 📥 Import Management
- **Order Creation**: Seamlessly create import orders.
- **Batch Entry**: Add multiple product items with their respective serials.
- **Validation**: Auto-validate serial numbers, pricing, and quantities.
- **Processing**: Save and process orders into the database.

### 📤 Export Management
- **Order Creation**: Manage outgoing shipments.
- **Serial Assignment**: Assign specific serial numbers to export products.
- **Transaction Safety**: Handle database transactions via JDBC to ensures data integrity.
- **Export History**: Keep a log of all past export activities.

### 📊 Reports & History
- **Audit Trails**: View comprehensive import/export history.
- **Advanced Filtering**: Filter records by date, status, or order code.
- **User Tracking**: Display information about users who created or processed the orders.

---

## 🛠️ Tech Stack

* **Backend:** Java Servlet, JSP
* **Frontend:** HTML, CSS, Bootstrap
* **Database:** SQL Server
* **ORM/Connectivity:** JDBC
* **Version Control:** Git

---

## 🗂️ Project Structure

```text
src/
├── controller/       # Servlet controllers
├── dao/              # Data access layer (CRUD)
├── dto/              # Data transfer objects
├── model/            # Entity models
├── service/          # Business logic
├── validator/        # Input validation
└── utils/            # Utility classes (DB connection, Helpers)

web/
├── views/            # JSP pages
├── css/              # Stylesheets
├── js/               # Client-side scripts
└── assets/           # Images and static icons
