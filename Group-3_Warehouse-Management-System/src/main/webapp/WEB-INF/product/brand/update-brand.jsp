<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">   
    </head>
    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp" />
        <main class=" main-content">

            <div>
                <h2 >Update Brand</h2>
            </div>

            <!-- message -->

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    Brand đã tồn tại
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>




            <div class="card mb-4 shadow-sm">
                <form action="brand-update" method="post" class="p-4">
                    <input type="hidden" name="id" value="${brand.id}">
                    <input type="hidden" id="isActive" name="status" value="${brand.active}">
                    <div >
                        <div class="mb-3">
                            <label class="form-label fw-bold">Brand names</label>
                            <input type="text" name="name" class="form-control" value="${brand.name}" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Description</label>
                            <textarea name="description" class="form-control" rows="8">${brand.description}</textarea>
                        </div>
                        <div class="d-grid mb-3">
                            <label class="form-label fw-bold">Status</label>
                            <button type="button"
                                    id="btnStatus"
                                    data-active="${brand.active}"
                                    class="btn ${brand.active ? 'btn-danger' : 'btn-success'}"
                                    onclick="toggleStatus()">
                                ${brand.active ? "Inactive" : "Active"}
                            </button>
                        </div>

                    </div>
                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary">Save Change</button>
                        <div >
                            <a href="brand" class="btn btn-outline-secondary ">Back to List</a>
                        </div>
                    </div>

                </form>
            </div>

        </main>
    </body>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
    <script>
                                        function toggleStatus() {
                                            const hidden = document.getElementById("isActive");
                                            const btn = document.getElementById("btnStatus");

                                            let current = hidden.value === "true";
                                            let newStatus = !current;

                                            hidden.value = newStatus;

                                            if (newStatus) {
                                                btn.classList.remove("btn-success");
                                                btn.classList.add("btn-danger");
                                                btn.innerText = "Inactive";
                                            } else {
                                                btn.classList.remove("btn-danger");
                                                btn.classList.add("btn-success");
                                                btn.innerText = "Active";
                                            }
                                        }
    </script>

</html>
