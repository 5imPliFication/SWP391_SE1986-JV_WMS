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

        <main class="main-content p-1">
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
                <form action="update-permissions" method="post" id="permissionForm">
                    <c:forEach items="${roleList}" var="role">
                        <input type="hidden" name="roleIds" value="${role.id}" />
                    </c:forEach>

                    <div class="card mb-4 shadow-sm">
                        <!-- ROLE HEADER -->
                        <div class="card-header">

                            <div class="row mb-3 align-items-end w-100">

                                <!-- ACTION -->
                                <div class="col-md-2">
                                    <label class="form-label fw-semibold">Action (CRUD)</label>
                                    <select id="actionSelect" class="form-select">
                                        <option value="">-- All --</option>

                                        <c:set var="actionListRendered" value="" />
                                        <c:forEach items="${allPermissions}" var="p">
                                            <c:set var="parts" value="${fn:split(p.name, '_')}" />
                                            <c:set var="action" value="${parts[0]}" />

                                            <c:if test="${not fn:contains(actionListRendered, action)}">
                                                <option value="${action}">${action}</option>
                                                <c:set var="actionListRendered" value="${actionListRendered}${action}," />
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>

                                <!-- OBJECT -->
                                <div class="col-md-2">
                                    <label class="form-label fw-semibold">Object</label>
                                    <select id="objectSelect" class="form-select">
                                        <option value="">-- All --</option>

                                        <c:set var="objectListRendered" value="" />
                                        <c:forEach items="${allPermissions}" var="p">
                                            <c:set var="parts" value="${fn:split(p.name, '_')}" />
                                            <c:set var="object" value="${parts[1]}" />

                                            <c:if test="${not fn:contains(objectListRendered, object)}">
                                                <option value="${object}">${object}</option>
                                                <c:set var="objectListRendered" value="${objectListRendered}${object}," />
                                            </c:if>
                                        </c:forEach>
                                    </select>
                                </div>

                                <!-- BUTTON CUỐI CÙNG BÊN PHẢI -->
                                <div class="col-md-8 d-flex justify-content-end">
                                    <button type="submit" class="btn btn-success mt-4">
                                        <i class="fas fa-save"></i> Save changes
                                    </button>
                                </div>

                            </div>

                        </div>


                        <!-- PERMISSION TABLE -->
                        <div class="card-body p-2">
                            <div style="overflow-x: auto;">   <!-- Cho phép scroll ngang nếu nhiều role -->

                                <table class="table table-bordered mb-0 permission-table">

                                    <colgroup>
                                        <!-- 3 cột cố định đầu -->
                                        <col style="width: 220px;">   <!-- Permission -->
                                        <col style="width: 300px;">   <!-- Description -->

                                        <!-- Mỗi role 1 cột cố định -->
                                        <c:forEach items="${roleList}" var="role">
                                            <col style="width: 120px;">
                                        </c:forEach>
                                    </colgroup>

                                    <thead class="table-primary text-center">
                                        <tr>
                                            <th>Permission</th>
                                            <th>Description</th>
                                                <c:forEach items="${roleList}" var="role">
                                                <th>
                                                    <c:choose>
                                                        <c:when test="${fn:contains(sessionScope.userPermissions, 'UPDATE_ROLE')}">
                                                            <a href="edit-role?id=${role.id}">
                                                                ${role.name}
                                                            </a>
                                                        </c:when>    
                                                        <c:otherwise>
                                                            ${role.name}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </th>
                                            </c:forEach>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach items="${allPermissions}" var="p">
                                            <tr data-permission-name="${p.name}">
                                                <td class="fw-semibold">${p.name}</td>
                                                <td>${p.description}</td>


                                                <c:forEach items="${roleList}" var="role">
                                                    <td class="text-center
                                                        <c:if test='${!role.active}'>fw-bold text-dark bg-secondary bg-opacity-25</c:if>
                                                            ">

                                                            <input type="checkbox"
                                                                   class="form-check-input permission-checkbox
                                                            <c:if test='${!role.active}'>opacity-50</c:if>"
                                                            name="perm_${role.id}"
                                                            value="${p.id}"
                                                            <c:if test="${!role.active}">disabled</c:if>
                                                            <c:forEach items="${role.permissions}" var="rp">
                                                                <c:if test="${rp.id == p.id}">checked</c:if>
                                                            </c:forEach>
                                                            />

                                                    </td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </tbody>

                                </table>
                            </div>

                        </div>
                </form>
            </div>
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
    <script>
        const actionSelect = document.getElementById("actionSelect");
        const objectSelect = document.getElementById("objectSelect");

        function filterTableByPermission() {
            const action = actionSelect.value;
            const object = objectSelect.value;

            document.querySelectorAll("tbody tr").forEach(row => {
                const permissionName = row.dataset.permissionName; // ví dụ: CREATE_USER

                let show = true;

                if (action) {
                    if (!permissionName.startsWith(action + "_")) {
                        show = false;
                    }
                }

                if (object) {
                    if (!permissionName.endsWith("_" + object)) {
                        show = false;
                    }
                }

                row.style.display = show ? "" : "none";
            });
        }

        actionSelect.addEventListener("change", filterTableByPermission);
        objectSelect.addEventListener("change", filterTableByPermission);
    </script>
