<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/9/2026
  Time: 7:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Create New User</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    </head>
    <body class="d-flex justify-content-center align-items-center vh-100 bg-light">
        <jsp:include page="/common/sidebar.jsp" />

        <div class="container mt-5 main-content ">
            <div class="row justify-content-center">
                <div class="col-md-6">

                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">Create New User</h4>
                        </div>

                        <div class="card-body">
                            <form action="${pageContext.request.contextPath}/user-create" method="post">

                                <!-- Full Name -->
                                <div class="mb-3">
                                    <label class="form-label">Full Name</label>
                                    <input type="text"
                                           name="fullName"
                                           class="form-control"
                                           placeholder="Enter full name">
                                </div>

                                <!-- Email -->
                                <div class="mb-3">
                                    <label class="form-label">Email</label>
                                    <input type="email"
                                           name="email"
                                           class="form-control"
                                           placeholder="Enter email">
                                </div>

                                <!-- Password -->
                                <div class="mb-3">
                                    <label class="form-label">Password</label>
                                    <input type="password"
                                           name="password"
                                           class="form-control"
                                           placeholder="Enter password">
                                </div>

                                <!-- Role -->
                                <div class="mb-3">
                                    <label class="form-label">Role</label>
                                    <select name="role" class="form-select">
                                        <option value="">-- Select Role --</option>
                                        <option value="1">Admin</option>
                                        <option value="2">Staff</option>
                                    </select>
                                </div>

                                <!-- Buttons -->
                                <div class="d-flex justify-content-between">
                                    <a href="/user-list"
                                       class="btn btn-secondary">
                                        Back
                                    </a>
                                    <button type="submit" class="btn btn-primary">
                                        Create User
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger mt-3">
                                ${error}
                            </div>
                        </c:if>
                    </div>
                    <div>
                        <c:if test="${not empty success}">
                            <div class="alert alert-success mt-3">
                                ${success}
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
