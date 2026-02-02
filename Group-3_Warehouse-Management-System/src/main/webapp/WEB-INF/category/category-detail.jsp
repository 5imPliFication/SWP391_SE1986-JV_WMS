<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Category Detail</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        </head>

        <body class="d-flex justify-content-center align-items-center vh-100 bg-light">
            <jsp:include page="/WEB-INF/common/sidebar.jsp" />

            <div class="card shadow-lg p-4" style="width: 500px;">
                <div class="card-body">
                    <h3 class="card-title text-center text-primary mb-4">Category Detail</h3>

                    <div class="mb-3">
                        <label class="fw-bold text-secondary">ID:</label>
                        <span class="fs-5 ms-2">${category.id}</span>
                    </div>

                    <div class="mb-3">
                        <label class="fw-bold text-secondary">Name:</label>
                        <div class="p-2 bg-light rounded border">${category.name}</div>
                    </div>

                    <div class="mb-3">
                        <label class="fw-bold text-secondary">Description:</label>
                        <div class="p-2 bg-light rounded border" style="min-height: 50px;">
                            ${empty category.description ? 'No description' : category.description}
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="fw-bold text-secondary">Status:</label>
                        <div class="ms-2 d-inline-block">
                            <c:choose>
                                <c:when test="${category.active}">
                                    <span class="badge bg-success fs-6"><i class="fas fa-check-circle"></i>
                                        Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger fs-6"><i class="fas fa-times-circle"></i>
                                        Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="d-grid gap-2 mt-4">
                        <a href="categories" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>