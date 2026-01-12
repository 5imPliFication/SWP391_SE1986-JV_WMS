<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Create New Role</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>

    <body class="d-flex justify-content-center align-items-center vh-100 bg-light">
        <jsp:include page="/common/sidebar.jsp" />

        <form action="create-role" method="post" class="border border-secondary rounded shadow bg-white w-50 p-4 main-content">
            <h2 class="text-center mb-4 text-primary">Create New Role</h2>

            <div class="mb-3">
                <label class="form-label fw-bold">Role Name</label>
                <input name="roleName" class="form-control" placeholder="Enter role name..." required>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold d-block">Status</label>
                <div class="btn-group w-100" role="group">
                    <input type="radio" class="btn-check" name="status" id="active" value="true" checked>
                    <label class="btn btn-outline-success" for="active"><i class="fas fa-check"></i> Active</label>

                    <input type="radio" class="btn-check" name="status" id="inactive" value="false">
                    <label class="btn btn-outline-danger" for="inactive"><i class="fas fa-times"></i> Inactive</label>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Assign Permissions</label>
                <select name="permissionIds" class="selectpicker w-100" 
                        multiple 
                        data-live-search="true" 
                        title="Select permissions for this role..."
                        data-style="btn-outline-secondary">
                    <c:forEach items="${listRolePermission}" var="p">
                        <option value="${p.id}">${p.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Description</label>
                <textarea name="description" class="form-control" rows="3" placeholder="Enter role description..."></textarea>
            </div>

            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-success py-2 fw-bold">
                    <i class="fas fa-plus-circle"></i> Create Role
                </button>
                <a href="roles" class="btn btn-outline-secondary">
                    <i class="fas fa-arrow-left"></i> Back to List
                </a>
            </div>
        </form>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>

        <script>
            $(document).ready(function () {
                $('.selectpicker').selectpicker('render');
            });
        </script>
    </body>
</html>