<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/9/2026
  Time: 1:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.User" %>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home.css">
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }

    // Get first letter of name for avatar
    String initial = user.getFullName() != null && !user.getFullName().isEmpty()
            ? user.getFullName().substring(0, 1).toUpperCase()
            : user.getFullName().substring(0, 1).toUpperCase();
%>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-header">
        <h1>Warehouse</h1>
        <p>Management System</p>
    </div>
    <ul class="sidebar-menu">
        <li><a href="home" class="active"><span></span><span>Dashboard</span></a></li>
        <li><a href="inventory"><span></span><span>Inventory</span></a></li>
        <li><a href="orders"><span></span><span>Orders</span></a></li>
        <li><a href="suppliers"><span></span><span>Suppliers</span></a></li>
        <li><a href="customers"><span></span><span>Customers</span></a></li>
        <li><a href="reports"><span></span><span>Reports</span></a></li>
        <li><a href="settings"><span>⚙</span><span>Settings</span></a></li>
    </ul>
</aside>

<!-- Main Content -->
<main class="main-content">
    <!-- Header -->
    <header class="header">
        <div class="header-left">
            <h2>Welcome Back, <%= user.getFullName() != null ? user.getFullName() : user.getFullName() %>!</h2>
            <p>Here's what's happening in your warehouse today</p>
        </div>
        <div class="user-info">
            <div class="user-avatar"><%= initial %>
            </div>
            <div class="user-details">
                <h4><%= user.getFullName() != null ? user.getFullName() : user.getFullName() %>
                </h4>
                <p><%= user.getRole() != null ? user.getRole().toUpperCase() : "USER" %>
                </p>
            </div>
            <a href="logout" class="btn-logout">Logout</a>
        </div>
    </header>

    <!-- Stats Grid -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-header">
                <div class="stat-icon blue">💻</div>
            </div>
            <div class="stat-content">
                <h3>1,234</h3>
                <p>Total Laptops in Stock</p>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-header">
                <div class="stat-icon green"></div>
            </div>
            <div class="stat-content">
                <h3>89</h3>
                <p>Orders This Month</p>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-header">
                <div class="stat-icon purple">🏭</div>
            </div>
            <div class="stat-content">
                <h3>24</h3>
                <p>Active Suppliers</p>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-header">
                <div class="stat-icon orange">⚠️</div>
            </div>
            <div class="stat-content">
                <h3>12</h3>
                <p>Low Stock Alerts</p>
            </div>
        </div>
    </div>

    <!-- Quick Actions -->
    <div class="quick-actions">
        <h3>Quick Actions</h3>
        <div class="action-buttons">
            <a href="add-product" class="action-btn">
                <span>➕</span>
                <span>Add New Product</span>
            </a>
            <a href="new-order" class="action-btn">
                <span>🛒</span>
                <span>Create Order</span>
            </a>
            <a href="add-supplier" class="action-btn">
                <span>🏢</span>
                <span>Add Supplier</span>
            </a>
            <a href="stock-report" class="action-btn">
                <span>📋</span>
                <span>Generate Report</span>
            </a>
        </div>
    </div>

    <!-- Recent Activity -->
    <div class="recent-activity">
        <h3>Recent Activity</h3>
        <div class="activity-item">
            <h4>New order received - #ORD-2026-001</h4>
            <p>Dell Latitude 5420 x10 units - 2 hours ago</p>
        </div>
        <div class="activity-item">
            <h4>Stock updated - MacBook Pro 14"</h4>
            <p>15 units added to inventory - 5 hours ago</p>
        </div>
        <div class="activity-item">
            <h4>Low stock alert - HP EliteBook 840</h4>
            <p>Only 3 units remaining - 1 day ago</p>
        </div>
    </div>
</main>
</body>
</html>
