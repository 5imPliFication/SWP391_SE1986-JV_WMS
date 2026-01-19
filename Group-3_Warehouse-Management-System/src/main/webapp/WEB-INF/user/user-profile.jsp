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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    <!-- Link to your new CSS file -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/user-profile.css">
</head>
<body>

<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class="main-content">
    <h2>User Profile</h2>

    <!-- Optional: Profile Header -->
    <div class="profile-header">
        <div class="profile-avatar">
            ${user.fullName.substring(0, 1).toUpperCase()}
        </div>
        <div class="profile-info">
            <h3>${user.fullName}</h3>
            <p>${user.email}</p>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/user/profile" method="post">
        <table>
            <tr>
                <td>Full Name</td>
                <td>
                    <input type="text"
                           name="fullName"
                           value="${user.fullName}"
                           placeholder="Enter your full name"
                           required/>
                    <div class="field-hint">This name will be displayed across the system</div>
                </td>
            </tr>

            <tr>
                <td>Email</td>
                <td>
                    <input type="email"
                           name="email"
                           value="${user.email}"
                           placeholder="your.email@company.com"
                           required/>
                    <div class="field-hint">Used for notifications and account recovery</div>
                </td>
            </tr>

            <tr>
                <td>Role</td>
                <td>
                    <input type="text"
                           value="${user.role.name}"
                           disabled/>
                    <div class="field-hint">Contact administrator to change your role</div>
                </td>
            </tr>
            <tr>
                <td colspan="2 ">
                    <a type="submit" class="btn-outline-danger btn p-2" href="${pageContext.request.contextPath}/change-password">Change Password</a>
                </td>
            </tr>
            <tr>

                <td colspan="2">
                    <button type="submit">Update Profile</button>
                </td>
            </tr>
        </table>
    </form>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
                ${sessionScope.success}
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.fail}">
        <div class="alert alert-danger">
                ${sessionScope.fail}
        </div>
        <c:remove var="fail" scope="session"/>
    </c:if>

</div>
</body>
</html>
