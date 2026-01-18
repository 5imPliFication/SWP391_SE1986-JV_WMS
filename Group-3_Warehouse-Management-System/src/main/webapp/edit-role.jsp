<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Update Role</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    </head>

    <body class="d-flex justify-content-center align-items-center vh-100 bg-light">
        <jsp:include page="/common/sidebar.jsp" />

        <form action="edit-role" method="post" class="border border-secondary rounded shadow bg-white w-50 p-4 main-content">
            <h2 class="text-center mb-4">Update Role</h2>

            <input type="hidden" name="roleID" value="${role.id}">

            <div class="mb-3">
                <label class="form-label fw-bold">Role Name</label>
                <input name="roleName" class="form-control" value="${role.name}" required>
            </div>

            <select name="permissionIds" class="selectpicker w-100" multiple data-live-search="true">
                <c:forEach items="${listRolePermission}" var="p">
                    <c:set var="isSelected" value="" />

                    <%-- Duyệt qua danh sách quyền HIỆN TẠI của Role này --%>
                    <c:forEach items="${role.permissions}" var="rp">
                        <%-- So sánh ID hoặc Name (nên dùng ID để chính xác tuyệt đối) --%>
                        <c:if test="${rp.id == p.id}">
                            <c:set var="isSelected" value="selected" />
                        </c:if>
                    </c:forEach>

                    <option value="${p.id}" ${isSelected}>
                        ${p.name}
                    </option>
                </c:forEach>
            </select>

            <div class="mb-3">
                <label class="form-label fw-bold">Description</label>
                <textarea name="description" class="form-control" rows="3">${role.description}</textarea>
            </div>

            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary">Update Role</button>
                <a href="roles" class="btn btn-outline-secondary">Back to List</a>
            </div>
        </form>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>

        <script>
            $(document).ready(function () {
                $('.selectpicker').selectpicker('refresh');
            });
        </script>
    </body>
</html>