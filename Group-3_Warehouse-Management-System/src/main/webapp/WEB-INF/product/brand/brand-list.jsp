<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">   
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    </head>
    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp" />

        <main class=" main-content">
            <div class="container-fluid py-4">

                <h2>Brand</h2>

                <!-- message -->
                <c:if test="${param.status == 'success'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        Thêm thành công
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.status == 'name_existed'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        Brand đã tồn tại
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.status == 'error'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        Đã xảy ra lỗi
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>


                <div class="card mb-4 shadow-sm">
                    <!-- ROLE HEADER -->
                    <div class="card-header">
                        <div class="card-header">

                            <div class="row mb-3 align-items-end w-100">

                                <!-- ACTION -->
                                <div class="col-md-3">
                                    <input
                                        type="text"
                                        id="searchName"
                                        class="form-control"
                                        placeholder="Search brand name..."
                                        >
                                </div>

                                <!-- OBJECT -->
                                <div class="col-md-2">
                                    <select id="a" class="form-select">
                                        <option value="">-- All --</option>
                                        <option value="1">Active</option>
                                        <option value="0">Deactive</option>
                                    </select>
                                </div>

                                <div class="col-md-7 d-flex justify-content-end">
                                    <button class="btn btn-primary"
                                            data-bs-toggle="modal"
                                            data-bs-target="#addBrandModal">
                                        Add new brand
                                    </button>
                                </div>

                            </div>

                        </div>

                        <!-- PERMISSION TABLE -->
                        <div class="card-body p-2">
                            <div style="overflow-x: auto;">   <!-- Cho phép scroll ngang nếu nhiều role -->

                                <table class="table table-bordered mb-0">
                                    <thead class="table-primary text-center">
                                        <tr>
                                            <th>Stt</th>
                                            <th>Brand name</th>
                                            <th>Description</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach items="${brandList}" var="b" varStatus="s">
                                            <tr data-status="${b.active ? 1 : 0}" 
                                                data-name="${fn:toLowerCase(b.name)}"
                                                >
                                                <td>${s.index + 1}</td>
                                                <td>${b.name}</td>
                                                <td>${b.description}</td>
                                                <td>
                                                    <form action="/brand-update" method="get" class="d-inline">
                                                        <input type="hidden" name="brandId" value="${b.id}">
                                                        <input type="hidden" name="status" value="${b.active}">
                                                        <c:if test="${b.active == true}">
                                                            <button type="submit" class="btn btn-sm btn-danger">
                                                                Active 
                                                            </button>
                                                        </c:if>
                                                        <c:if test="${b.active == false}">
                                                            <button type="submit" class="btn btn-sm btn-success">
                                                                Inactive 
                                                            </button>
                                                        </c:if>
                                                    </form>
                                                </td>
                                                <td>
                                                    <button>edit</button>
                                                    <button>delete</button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>

                </div>

        </main>
        <jsp:include page="add-brand.jsp" />
    </body>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        const statusSelect = document.getElementById("a");
        const searchInput = document.getElementById("searchName");

        function filterTable() {
            const selectedStatus = statusSelect.value;   // "", "1", "0"
            const searchText = searchInput.value.toLowerCase().trim();

            document.querySelectorAll("tbody tr").forEach(row => {
                const rowStatus = row.dataset.status;
                const rowName = row.dataset.name;

                let show = true;

                if (selectedStatus !== "") {
                    show = rowStatus === selectedStatus;
                }

                if (show && searchText !== "") {
                    show = rowName.includes(searchText);
                }

                row.style.display = show ? "" : "none";
            });
        }

        statusSelect.addEventListener("change", filterTable);
        searchInput.addEventListener("keyup", filterTable);
    </script>

</html>
