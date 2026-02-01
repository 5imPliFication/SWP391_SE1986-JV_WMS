<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Category List</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        </head>

        <body class="d-flex bg-light">
            <jsp:include page="/WEB-INF/common/sidebar.jsp" />

            <div class="container-fluid p-4">
                <div class="card shadow-sm">
                    <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                        <h4 class="mb-0 text-primary">Category Management</h4>
                        <a href="create-category" class="btn btn-primary">
                            <i class="fas fa-plus"></i> Add New Category
                        </a>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty param.status}">
                            <div class="alert alert-info alert-dismissible fade show" role="alert">
                                Status: ${param.status}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"
                                    aria-label="Close"></button>
                            </div>
                        </c:if>

                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr>
                                        th>ID</th>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="category" items="${categories}">
                                        <tr>
                                            <td>${category.id}</td>
                                            <td>${category.name}</td>
                                            <td>${category.description}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${category.active}">
                                                        <span class="badge bg-success">Active</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger">Inactive</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="category-detail?id=${category.id}"
                                                    class="btn btn-sm btn-info text-white me-2" title="View Detail">
                                                    <i class="fas fa-eye"></i> View
                                                </a>
                                                <!-- Add Edit/Delete buttons if needed later -->
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty categories}">
                                        <tr>
                                            <td colspan="5" class="text-center text-muted py-4">
                                                No categories found.
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>