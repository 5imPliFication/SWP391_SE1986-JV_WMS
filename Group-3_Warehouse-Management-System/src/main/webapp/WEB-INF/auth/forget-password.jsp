<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password - Laptop Warehouse</title>
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
                    </div>
                    
                    <div class="col-md-6 p-5">
                        <div class="mb-4">
                            <h3 class="fw-bold">Forgot Password?</h3>
                            <p class="text-muted">Enter your email to request a password reset</p>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>
                        <c:if test="${not empty status}">
                            <div class="alert alert-success">
                                ${status}
                            </div>
                        </c:if>

                        <form method="POST" action="${pageContext.request.contextPath}/forget-password">
                            <div class="mb-3">
                                <label for="email" class="form-label fw-bold">Email Address</label>
                                <input type="email" id="email" name="email" class="form-control form-control-lg" placeholder="your.email@company.com" required autofocus>
                            </div>

                            <div class="alert alert-info mt-3 p-3" style="font-size: 0.9rem;">
                                Your request will be sent to the administrator. The administrator will send a reset password link to your email after confirming your request.
                            </div>

                            <div class="d-grid gap-2 mb-3 mt-4">
                                <button type="submit" class="btn btn-primary btn-lg fw-bold">Send Request</button>
                            </div>
                            
                            <div class="text-center mt-3">
                                <span class="text-muted">Remember your password?</span>
                                <a href="${pageContext.request.contextPath}/login" class="text-decoration-none fw-bold">Back to Login</a>
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
