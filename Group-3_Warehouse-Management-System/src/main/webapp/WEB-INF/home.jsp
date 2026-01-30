<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/9/2026
  Time: 1:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <title>Home</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    </head>
    <body >     
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

        <jsp:include page="/WEB-INF/common/sidebar.jsp" />

        <!-- Main Content -->
        <main class="main-content">
            <!-- Header -->
            <header class="header">
                <div class="header-left">
                    <h2>Welcome Back, <%= user.getFullName() != null ? user.getFullName() : user.getFullName() %>!</h2>
                    <p>Here's what's happening in your warehouse today</p>
                </div>
                <div class="user-info">
                    <a  href="<%= request.getContextPath() %>/user/profile" class="user-info">
                        <div class="user-avatar"><%= initial %>
                        </div>
                        <div class="user-details">
                            <h4><%= user.getFullName() != null ? user.getFullName() : "Unknown User" %></h4>
                            <p>
                                <%= user.getRole() != null && user.getRole().getName() != null
                                    ? user.getRole().getName().toUpperCase()
                                    : "USER" %>
                            </p>

                        </div>
                    </a>
                    <a href="logout" class="btn-logout">Logout</a>
                </div>
            </header>

            <!-- Recent Activity -->
            <c:if test="${sessionScope.user.role.name == 'Admin'}">
                <jsp:include page="/WEB-INF/views/home/admin-dashboard.jsp"/>
            </c:if>

            <!-- ================= MANAGER DASHBOARD ================= -->
            <c:if test="${sessionScope.user.role.name == 'Manager'}">
                <jsp:include page="/WEB-INF/views/home/manager-dashboard.jsp"/>
            </c:if>

            <!-- ================= STAFF DASHBOARD ================= -->
            <c:if test="${sessionScope.user.role.name == 'Salesman'}">
                <jsp:include page="/WEB-INF/views/home/salesman-dashboard.jsp"/>
            </c:if>

            <!-- ================= WAREHOUSE KEEPER DASHBOARD ================= -->
            <c:if test="${sessionScope.user.role.name == 'Warehouse'}">
                <jsp:include page="/WEB-INF/views/home/salesman-dashboard.jsp"/>
            </c:if>
        </main>
    </body>
</html>
