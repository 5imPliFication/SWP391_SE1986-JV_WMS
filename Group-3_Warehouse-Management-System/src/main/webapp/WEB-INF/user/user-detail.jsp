<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>User Detail Information</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body class="d-flex justify-content-center mt-5 vh-100 bg-light">
        <jsp:include page="/WEB-INF/common/sidebar.jsp" />

        <div class="container main-content">

            <h3 class="text-center mb-4 fw-semibold">Edit User Information</h3>

            <div class="card shadow-sm">
                <div class="card-body">

                    <!-- FORM UPDATE -->
                    <form action="user" method="post">

                        <input type="hidden" name="id" value="${user.id}">
                        <input type="hidden" name="active" value="${user.active}" id="activeValue">

                        <table class="table table-bordered align-middle mb-0">

                            <!-- FULL NAME -->
                            <tr>
                                <th>Full Name</th>
                                <td>
                                    <input type="text"
                                           name="fullName"
                                           class="form-control"
                                           value="${user.fullName}"
                                           required>
                                </td>
                            </tr>

                            <!-- EMAIL -->
                            <tr>
                                <th>Email</th>
                                <td>
                                    <input type="email"
                                           name="email"
                                           class="form-control"
                                           value="${user.email}"
                                           required>
                                </td>
                            </tr>

                            <!-- ROLE -->
                            <tr>
                                <th>Role</th>
                                <td>
                                    <select name="roleId" class="form-select">
                                        <c:forEach items="${roles}" var="r">
                                            <option value="${r.id}"
                                                    ${r.id == user.role.id ? "selected" : ""}>
                                                ${r.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>

                            <!-- STATUS TOGGLE -->
                            <tr>
                                <th>Status</th>
                                <td>
                                    <button type="button"
                                            id="statusBtn"
                                            class="btn ${user.active ? 'btn-success' : 'btn-danger'}"
                                            onclick="toggleStatus()">
                                        ${user.active ? 'ACTIVE' : 'INACTIVE'}
                                    </button>
                                </td>
                            </tr>

                        </table>

                        <!-- BUTTONS -->
                        <div class="text-center mt-4">
                            <button type="submit" class="btn btn-primary px-4">
                                Save Changes
                            </button>

                            <a href="user/list" class="btn btn-secondary px-4 ms-2">
                                Back
                            </a>
                        </div>

                    </form>

                </div>
            </div>
        </div>

        <script>
            function toggleStatus() {
                const btn = document.getElementById("statusBtn");
                const input = document.getElementById("activeValue");

                if (input.value === "true") {
                    input.value = "false";
                    btn.textContent = "INACTIVE";
                    btn.classList.remove("btn-success");
                    btn.classList.add("btn-danger");
                } else {
                    input.value = "true";
                    btn.textContent = "ACTIVE";
                    btn.classList.remove("btn-danger");
                    btn.classList.add("btn-success");
                }
            }
        </script>

    </body>
</html>
