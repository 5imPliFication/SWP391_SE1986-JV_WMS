<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Update Role</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/design-system.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    </head>

    <body>

        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <main class="main-content">

            <div class="d-flex justify-content-center align-items-center min-vh-100">
                <div class="border border-secondary rounded shadow bg-white w-50 p-4">

                    <form action="edit-role" method="post">

                        <h2 class="text-center mb-4">Update Role</h2>

                        <!-- hidden -->
                        <input type="hidden" name="roleID" value="${role.id}">
                        <input type="hidden" id="isActive" name="currentStatus" value="${role.active}">

                        <!-- Role Name -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Role Name</label>
                            <input name="roleName"
                                   class="form-control"
                                   value="${role.name}"
                                   required>
                        </div>

                        <!-- STATUS -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Status</label><br/>

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

                        <!-- Description -->
                        <div class="mb-3">
                            <label class="form-label fw-bold">Description</label>
                            <textarea name="description"
                                      class="form-control"
                                      rows="3">${role.description}</textarea>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex justify-content-between">

                            <button type="submit" class="btn btn-primary">
                                Update Role
                            </button>

                            <a href="roles" class="btn btn-outline-secondary">
                                Back to List
                            </a>

                        </div>

                    </form>

                </div>
            </div>

        </main>

        <!-- JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>

        <script>
                                        // Sử dụng noconflict nếu nghi ngờ có thư viện khác tranh chấp ký hiệu $
                                        var $j = jQuery.noConflict();

                                        $j(document).ready(function () {
                                            // Khởi tạo thủ công để đảm bảo nó chạy
                                            $j('.selectpicker').selectpicker({
                                                container: 'body',
                                                liveSearch: true,
                                                noneSelectedText: 'Chưa chọn quyền'
                                            });
                                        });

                                        function toggleStatus() {
                                            const hidden = document.getElementById("isActive");
                                            const btn = document.getElementById("btnStatus");
                                            let current = hidden.value === "true";
                                            let newStatus = !current;

                                            hidden.value = newStatus;
                                            if (newStatus) {
                                                btn.className = "btn btn-sm btn-danger";
                                                btn.innerText = "Deactivate this role";
                                            } else {
                                                btn.className = "btn btn-sm btn-success";
                                                btn.innerText = "Activate this role";
                                            }
                                        }


        </script>

    </body>
</html>
