<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Laptop Warehouse</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #5897e8;
            min-height: 100vh;
            display: flex;
            align-items: center;
        }
        .login-card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
            overflow: hidden;
        }
        .bg-login-left {
            background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
            color: white;
            padding: 3rem;
        }
        .feature-list {
            list-style: none;
            padding-left: 0;
            margin-top: 2rem;
        }
        .feature-list li {
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-xl-9 col-lg-10">
            <div class="card login-card">
                <div class="row g-0">
                    <div class="col-md-6 bg-login-left d-none d-md-flex flex-column justify-content-center">
                        <h2 class="fw-bold mb-3">Laptop Warehouse</h2>
                        <p class="text-light opacity-75">Comprehensive inventory management system for laptop distribution and tracking</p>
                        <ul class="feature-list">
                            <li>Real-time inventory tracking</li>
                            <li>Order management</li>
                            <li>Supplier coordination</li>
                            <li>Sales analytics & reporting</li>
                            <li>Multi-user access control</li>
                        </ul>
                    </div>
                    
                    <div class="col-md-6 p-5">
                        <div class="mb-4">
                            <h3 class="fw-bold">Welcome Back</h3>
                            <p class="text-muted">Sign in to access your dashboard</p>
                        </div>

                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null && !error.isEmpty()) {
                        %>
                        <div class="alert alert-danger">
                            <%= error %>
                        </div>
                        <% } %>

                        <form action="${pageContext.request.contextPath}/login" method="post">
                            <div class="mb-3">
                                <label for="email" class="form-label fw-bold">Email Address</label>
                                <input type="email" id="email" name="email" class="form-control form-control-lg" placeholder="your.email@company.com" required autofocus>
                            </div>

                            <div class="mb-4">
                                <label for="password" class="form-label fw-bold">Password</label>
                                <input type="password" id="password" name="password" class="form-control form-control-lg" placeholder="Enter your password" required>
                            </div>

                            <div class="d-grid gap-2 mb-3">
                                <button type="submit" class="btn btn-primary btn-lg fw-bold">Sign In</button>
                            </div>
                            
                            <div class="text-center">
                                <a href="${pageContext.request.contextPath}/forget-password" class="text-decoration-none">Forgot password?</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>

