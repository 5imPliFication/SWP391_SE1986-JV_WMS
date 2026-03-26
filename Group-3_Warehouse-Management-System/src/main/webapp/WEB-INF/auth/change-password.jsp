<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Change Password - Laptop Warehouse</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class="main-content">
    <h2>Change Password</h2>

    <div class="card p-4 mt-3" style="max-width: 600px;">
        <div class="d-flex align-items-center mb-4">
            <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center me-3"
                 style="width: 60px; height: 60px; font-size: 24px; font-weight: bold;">
                ${user.fullName != null ? user.fullName.substring(0, 1).toUpperCase() : 'U'}
            </div>
            <div>
                <h4 class="mb-0">${user.fullName != null ? user.fullName : 'User'}</h4>
                <p class="text-muted mb-0">${user.email}</p>
            </div>
        </div>

        <form method="POST" action="${pageContext.request.contextPath}/change-password">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    ${error}
                </div>
            </c:if>

            <div class="mb-3">
                <label for="currentPassword" class="form-label fw-bold">Current Password</label>
                <input type="password" id="currentPassword" name="currentPassword"
                       class="form-control" placeholder="Enter current password" required>
            </div>

            <div class="mb-3">
                <label for="newPassword" class="form-label fw-bold">New Password</label>
                <input type="password" id="newPassword" name="newPassword"
                       class="form-control" placeholder="Enter new password" required>
                <div class="form-text">
                    Password should be at least 8 characters including letters and numbers.
                </div>
            </div>

            <div class="d-flex gap-2 mt-4">
                <button type="submit" class="btn btn-primary">Update Password</button>
                <a href="${pageContext.request.contextPath}/user/profile" class="btn btn-secondary">Back to Profile</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>