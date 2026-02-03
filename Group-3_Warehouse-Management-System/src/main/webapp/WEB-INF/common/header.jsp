<%@ page import="com.example.model.User" %><%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/30/2026
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Title</title>
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
        <header class="header">
            <div class="header-left">
                <h2>Welcome Back, <%= user.getFullName() != null ? user.getFullName() : user.getFullName() %>!</h2>
                <p>Here's what's happening in your warehouse today</p>
            </div>
            <div class="user-info">
                <a href="<%= request.getContextPath() %>/user/profile" class="user-info">
                    <div class="user-avatar"><%= initial %>
                    </div>
                    <div class="user-details">
                        <h4><%= user.getFullName() != null ? user.getFullName() : "Unknown User" %>
                        </h4>
                        <p>
                            <%= user.getRole() != null && user.getRole().getName() != null
                                    ? user.getRole().getName().toUpperCase()
                                    : "USER" %>
                        </p>

                    </div>
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                    Logout
                </a>
            </div>
        </header>
    </body>
</html>
