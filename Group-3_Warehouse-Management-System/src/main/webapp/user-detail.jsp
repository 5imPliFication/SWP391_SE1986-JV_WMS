<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/9/2026
  Time: 9:34 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Detail Information</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f4f6f9;
        }
        .card {
            border-radius: 8px;
        }
        .table th {
            width: 35%;
            background-color: #f8f9fa;
        }
    </style>
</head>

<body>
<div class="container mt-5">

    <h3 class="text-center mb-4 fw-semibold">User Information</h3>

    <div class="card shadow-sm">
        <div class="card-body">

            <table class="table table-bordered align-middle mb-0">
                <tr>
                    <th>Full Name</th>
                    <td>${user.fullName}</td>
                </tr>
                <tr>
                    <th>Email</th>
                    <td>${user.email}</td>
                </tr>
                <tr>
                    <th>Role</th>
                    <td>
                        <span class="badge bg-info text-dark">
                            ${user.role.name}
                        </span>
                    </td>
                </tr>
                <tr>
                    <th>Status</th>
                    <td>
                        <c:choose>
                            <c:when test="${user.active}">
                                <span class="badge bg-success">ACTIVE</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-danger">INACTIVE</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>

            <div class="text-center mt-4">
                <a href="user-list" class="btn btn-secondary px-4">
                    Back
                </a>
            </div>

        </div>
    </div>

</div>
</body>
</html>
