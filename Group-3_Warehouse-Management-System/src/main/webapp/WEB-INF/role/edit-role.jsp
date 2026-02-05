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
        <jsp:include page="/WEB-INF/common/sidebar.jsp" />

        <div class="border border-secondary rounded shadow bg-white w-50 p-4 main-content">

            <!-- FORM UPDATE ROLE -->
            <form action="edit-role" method="post">

                <h2 class="text-center mb-4">Update Role</h2>

                <!-- hidden fields -->
                <input type="hidden" name="roleID" value="${role.id}">
                <input type="hidden" id="isActive" name="currentStatus" value="${role.active}">

                <!-- Role name -->
                <div class="mb-3">
                    <label class="form-label fw-bold">Role Name</label>
                    <input name="roleName" class="form-control" value="${role.name}" required>
                </div>

                <div class="d-grid mb-3">
                    <label class="form-label fw-bold">Status</label>
                    <button type="button"
                            id="btnStatus"
                            class="btn btn-sm ${role.active ? 'btn-danger' : 'btn-success'}"
                            onclick="toggleStatus()">

                        <c:choose>
                            <c:when test="${role.active}">
                                Deactivate this role
                            </c:when>
                            <c:otherwise>
                                Activate this role
                            </c:otherwise>
                        </c:choose>

                    </button>
                </div>


                <!-- PERMISSIONS -->
                <label class="form-label fw-bold">Permission</label>

                <select name="permissionIds" class="selectpicker w-100" multiple data-live-search="true">
                    <c:forEach items="${listRolePermission}" var="p">
                        <c:set var="isSelected" value="" />

                        <c:forEach items="${role.permissions}" var="rp">
                            <c:if test="${rp.id == p.id}">
                                <c:set var="isSelected" value="selected" />
                            </c:if>
                        </c:forEach>

                        <option value="${p.id}" ${isSelected}>
                            ${p.name}
                        </option>
                    </c:forEach>
                </select>

                <!-- Description -->
                <div class="mb-3 mt-3">
                    <label class="form-label fw-bold">Description</label>
                    <textarea name="description" class="form-control" rows="3">${role.description}</textarea>
                </div>

                <!-- UPDATE BUTTON -->
                <div class="d-grid m-1">
                    <button type="submit" class="btn btn-primary">Update Role</button>
                </div>
            </form>

            <!-- BACK -->
            <div class="d-grid m-1">
                <a href="roles" class="btn btn-outline-secondary">Back to List</a>
            </div>

        </div>

        <!-- JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
        <script>
                      function toggleStatus() {
                          const hidden = document.getElementById("isActive");
                          const btn = document.getElementById("btnStatus");

                          // đảo trạng thái
                          let current = hidden.value === "true";
                          let newStatus = !current;

                          // cập nhật hidden field
                          hidden.value = newStatus;

                          // đổi giao diện nút
                          if (newStatus) {
                              btn.classList.remove("btn-success");
                              btn.classList.add("btn-danger");
                              btn.innerText = "Deactivate this role";
                          } else {
                              btn.classList.remove("btn-danger");
                              btn.classList.add("btn-success");
                              btn.innerText = "Activate this role";
                          }
                      }
        </script>



    </body>
</html>
