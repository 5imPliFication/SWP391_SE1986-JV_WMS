<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 1/9/2026
  Time: 10:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile - Laptop Warehouse</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class="main-content">
    <h2>User Profile</h2>

    <div class="card p-4 mt-3" style="max-width: 600px;">
        <div class="d-flex align-items-center mb-4">
            <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center me-3"
                 style="width: 60px; height: 60px; font-size: 24px; font-weight: bold;">
                ${user.fullName.substring(0, 1).toUpperCase()}
            </div>
            <div>
                <h4 class="mb-0">${user.fullName}</h4>
                <p class="text-muted mb-0">${user.email}</p>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/user/profile" method="post">
            <div class="mb-3">
                <label class="form-label fw-bold">Full Name</label>
                <input type="text" name="fullName" class="form-control" value="${user.fullName}" placeholder="Enter your full name" required/>
                <div class="form-text">This name will be displayed across the system</div>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Email</label>
                <input type="text" class="form-control" value="${user.email}" disabled/>
                <div class="form-text">Used for notifications and account recovery</div>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Role</label>
                <input type="text" class="form-control" value="${user.role.name}" disabled/>
                <div class="form-text">Contact administrator to change your role</div>
            </div>

            <div class="d-flex gap-2 mt-4">
                <button type="submit" class="btn btn-primary">Update Profile</button>
                <a class="btn btn-outline-danger" href="${pageContext.request.contextPath}/change-password">Change Password</a>
            </div>
        </form>

        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success mt-3">
                ${sessionScope.success}
            </div>
            <c:remove var="success" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.fail}">
            <div class="alert alert-danger mt-3">
                ${sessionScope.fail}
            </div>
            <c:remove var="fail" scope="session"/>
        </c:if>
    </div>

</div>
</body>
</html>
