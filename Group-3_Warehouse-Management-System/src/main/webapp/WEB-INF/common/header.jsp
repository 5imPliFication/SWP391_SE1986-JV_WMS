<%@ page import="com.example.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }

    String initial = "U";
    if (user.getFullName() != null && !user.getFullName().isEmpty()) {
        initial = user.getFullName().substring(0,1).toUpperCase();
    }
%>

<!DOCTYPE html>
<html>
    <head>

        <title>Header</title>

        <!-- Bootstrap CSS -->
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">

    </head>

    <body>

        <header class="bg-white shadow-lg sticky-top" style="margin-left: 300px">

            <div class="container-fluid d-flex justify-content-end p-2">

                <div class="dropdown d-flex align-items-center">

                    <!-- User info -->
                    <div class="mr-2 text-right">
                        <strong>
                            <%= user.getFullName() != null ? user.getFullName() : "Unknown User" %>
                        </strong><br>

                        <small class="text-muted">
                            <%= user.getRole() != null && user.getRole().getName() != null
                                    ? user.getRole().getName().toUpperCase()
                                    : "USER" %>
                        </small>
                    </div>

                    <!-- Avatar -->
                    <button class="btn p-0 border-0 rounded-circle bg-primary text-white
                            d-flex align-items-center justify-content-center"
                            id="userDropdown"
                            data-toggle="dropdown"
                            aria-haspopup="true"
                            aria-expanded="false"
                            style="width:40px;height:40px;font-weight:bold;">

                        <%= initial %>

                    </button>

                    <!-- Dropdown -->
                    <div class="dropdown-menu dropdown-menu-right"
                         aria-labelledby="userDropdown">

                        <a class="dropdown-item"
                           href="<%= request.getContextPath() %>/user/profile">
                            Profile
                        </a>

                        <a class="dropdown-item text-danger"
                           href="<%= request.getContextPath() %>/logout">
                            Logout
                        </a>

                    </div>

                </div>
            </div>
        </header>

        <!-- jQuery (bắt buộc cho Bootstrap 4) -->
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>

        <!-- Bootstrap JS -->
        <script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
