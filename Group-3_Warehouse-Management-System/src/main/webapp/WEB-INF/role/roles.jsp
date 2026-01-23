<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Role Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    </head>
    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp" />

        <main class="main-content">
            <div class="container-fluid py-4">
                <h2 class="mb-4">Role & Permission Management</h2>
                <c:if test="${param.msg == 'delete_success'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>
                        Xóa role thành công!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${param.msg == 'error'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        Có lỗi xảy ra khi xóa role!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${fn:contains(sessionScope.userPermissions, 'CREATE_ROLE')}">
                    <a href="create-role" class="btn btn-primary mb-3">
                        <i class="fas fa-plus-circle"></i> Create new role
                    </a>
                </c:if>

                <!-- Sua tu cho nay-->
                <c:forEach items="${roleList}" var="role">

                    <div class="card mb-4 shadow-sm">

                        <!-- ROLE HEADER -->
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h4 class="mb-0 text-primary fw-bold">
                                    ${role.name}
                            </h4>

                            <div>
                                <c:if test="${fn:contains(sessionScope.userPermissions, 'UPDATE_ROLE')}">
                                    <a href="edit-role?id=${role.id}" class="btn btn-outline-primary btn-sm">
                                        Edit
                                    </a>
                                </c:if>

                                <c:if test="${fn:contains(sessionScope.userPermissions, 'DELETE_ROLE')}">
                                    <form action="roles" method="post" class="d-inline"
                                          onsubmit="return confirm('Xóa role ${role.name}?')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${role.id}">
                                        <button class="btn btn-outline-danger btn-sm">
                                            Delete
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                        </div>

                        <!-- PERMISSION TABLE -->
                        <div class="card-body p-0">
                            <table class="table table-bordered mb-0">

                                <thead class="table-primary text-center">
                                <tr>
                                    <th>Permission</th>
                                    <th>Description</th>
                                    <th>Status</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:set var="rolePermissionIds"
                                       value="${requestScope['rolePermissionIds_' += role.id]}" />

                                <c:forEach items="${allPermissions}" var="p">
                                    <tr>
                                        <td class="fw-semibold">${p.name}</td>
                                        <td>${p.description}</td>
                                        <td class="text-center">
                                            <input type="checkbox"
                                                   class="form-check-input permission-checkbox"
                                                   data-role-id="${role.id}"
                                                   data-permission-id="${p.id}"
                                                   <c:if test="${rolePermissionIds.contains(p.id)}">checked</c:if>
                                            >
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>

                            </table>
                        </div>
                    </div>

                </c:forEach>


            </div>
        </main>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.querySelectorAll('.permission-checkbox').forEach(cb => {
                cb.addEventListener('change', function () {
                    fetch('update-role-permission', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body:
                            'roleId=' + this.dataset.role +
                            '&permissionId=' + this.dataset.permission +
                            '&checked=' + this.checked
                    });
                });
            });
        </script>
