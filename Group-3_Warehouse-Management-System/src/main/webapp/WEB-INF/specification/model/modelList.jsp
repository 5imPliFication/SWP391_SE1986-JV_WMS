<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Specification Management</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <main class="main-content">

            <h2>Specification</h2>

            <!-- ================= TAB ================= -->
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link active"
                       href="${pageContext.request.contextPath}/specification/model">
                        Model
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link ${tab == 'chip' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/specification/chip">
                        Chip
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link ${tab == 'ram' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/specification/ram">
                        Ram
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link ${tab == 'size' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/specification/size">
                        Size
                    </a>
                </li>

                <li class="nav-item">
                    <a class="nav-link ${tab == 'storage' ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/specification/storage">
                        Storage
                    </a>
                </li>
            </ul>

            <!-- ================= CONTENT ================= -->

            <div class="row">

                <!-- ================= TABLE ================= -->

                <div class="col-md-8">

                    <div class="card-body shadow-lg rounded">

                        <h4>Models</h4>

                        <c:set var="tableHeader" scope="request">
                            <tr>
                                <th style="width: 100px">#</th>
                                <th>Model Name</th>
                                <th>Brand</th>
                                <th style="width: 200px">Status</th>
                                <th style="width: 200px">Action</th>
                            </tr>
                        </c:set>

                        <c:set var="tableBody" scope="request">
                            <c:forEach var="m" items="${models}" varStatus="s">
                                <tr>

                                    <td>${(pageNo - 1) * 2 + s.index + 1}</td>

                                    <td>${m.name}</td>

                                    <td>${m.brand.name}</td>

                                    <td>

                                        <jsp:include page="/WEB-INF/common/statusBadge.jsp">
                                            <jsp:param name="active" value="${m.active}" />
                                        </jsp:include>

                                    </td>

                                    <td>

                                        <form action="${pageContext.request.contextPath}/active-model" method="post">

                                            <input type="hidden" name="id" value="${m.id}" />
                                            <input type="hidden" name="active" value="${!m.active}" />

                                            <button class="btn btn-sm ${!m.active ? 'btn-success' : 'btn-danger'}">
                                                ${!m.active ? 'Active' : 'Inactive'}
                                            </button>

                                        </form>

                                    </td>

                                </tr>
                            </c:forEach>
                        </c:set>

                        <jsp:include page="/WEB-INF/common/table.jsp"/>

                        <!-- PAGINATION -->

                        <jsp:include page="/WEB-INF/common/pagination.jsp">
                            <jsp:param name="pageNo" value="${pageNo}" />
                            <jsp:param name="totalPages" value="${totalPages}" />
                            <jsp:param name="baseUrl" value="${pageContext.request.contextPath}/specification/model" />
                        </jsp:include>

                    </div>

                </div>


                <!-- ================= CREATE FORM ================= -->

                <div class="col-md-4">

                    <div class="card shadow-lg">

                        <div class="card-header">
                            <h5>Create Model</h5>
                        </div>

                        <div class="card-body">

                            <form action="${pageContext.request.contextPath}/create-model" method="post">

                                <!-- MODEL NAME -->

                                <div class="mb-3">

                                    <label class="form-label">Model Name</label>

                                    <input
                                        type="text"
                                        id="modelName"
                                        name="name"
                                        class="form-control">

                                </div>


                                <!-- BRAND -->

                                <div class="mb-3">

                                    <label class="form-label">Brand</label>

                                    <select
                                        id="brandSelect"
                                        name="brandId"
                                        class="form-select">

                                        <option value="">Select Brand</option>

                                        <c:forEach var="b" items="${brands}">
                                            <option value="${b.id}">
                                                ${b.name}
                                            </option>
                                        </c:forEach>

                                    </select>

                                </div>


                                <!-- STATUS -->

                                <div class="mb-3">

                                    <label class="form-label">Status</label>

                                    <select name="active" class="form-select">
                                        <option value="true">Active</option>
                                        <option value="false">Inactive</option>
                                    </select>

                                </div>


                                <!-- BUTTON -->

                                <button
                                    id="createBtn"
                                    class="btn btn-success w-100"
                                    disabled>

                                    Create

                                </button>

                            </form>

                        </div>

                    </div>

                </div>

            </div>

        </main>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <script>

            document.addEventListener("DOMContentLoaded", function () {

                const name = document.getElementById("modelName");
                const brand = document.getElementById("brandSelect");
                const btn = document.getElementById("createBtn");

                function validateForm() {

                    if (
                            name.value.trim() !== "" &&
                            brand.value !== ""
                            ) {

                        btn.disabled = false;

                    } else {

                        btn.disabled = true;

                    }

                }

                name.addEventListener("input", validateForm);
                brand.addEventListener("change", validateForm);

            });

        </script>

    </body>
</html>
