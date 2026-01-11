<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Role Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    </head>
    <body>
        <jsp:include page="/common/sidebar.jsp" />

        <main class="main-content">
            <div class="container-fluid py-4">
                <h2 class="mb-4">Role & Permission Management</h2>

                <a href="create-role" class="btn btn-primary mb-3">
                    <i class="fas fa-plus-circle"></i> Create new role
                </a>

                <div class="card shadow-sm">
                    <div class="card-body">
                        <table class="table table-hover align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th style="width: 20%">Role Name</th>
                                    <th style="width: 40%">Permissions</th>
                                    <th style="width: 15%">Status</th>
                                    <th style="width: 15%">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${roleList}" var="role">
                                    <tr>
                                        <td class="fw-bold text-primary">${role.name}</td>

                                        <td>
                                            <c:forEach items="${role.permission}" var="p">
                                                <span class="badge bg-info text-dark me-1" style="font-weight: 500;">
                                                    ${p.name}
                                                </span>
                                            </c:forEach>
                                            <c:if test="${empty role.permission}">
                                                <span class="text-muted small fst-italic">Chưa cấp quyền</span>
                                            </c:if>
                                        </td>

                                        <td>
                                            <form action="change_role_status" method="post" class="d-inline">
                                                <input type="hidden" name="roleId" value="${role.id}">
                                                <input type="hidden" name="currentStatus" value="${role.active}">

                                                <c:choose>
                                                    <c:when test="${role.active}">
                                                        <button type="submit" class="btn btn-success btn-sm w-75">ACTIVE</button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button type="submit" class="btn btn-danger btn-sm w-75">DEACTIVE</button>
                                                    </c:otherwise>
                                                </c:choose>
                                            </form>
                                        </td>

                                        <td>
                                            <div class="btn-group" role="group">
                                                <a href="edit-role?id=${role.id}" class="btn btn-outline-primary btn-sm">
                                                    <i class="fas fa-edit"></i> Edit
                                                </a>

                                                <form action="roles" method="post" class="d-inline" onsubmit="return confirm('Bạn có chắc muốn xóa Role: ${role.name}?')">
                                                    <input type="hidden" name="action" value="delete">
                                                    <input type="hidden" name="id" value="${role.id}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm" style="border-top-left-radius: 0; border-bottom-left-radius: 0;">
                                                        <i class="fas fa-trash"></i> Delete
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </main>

        <script src="