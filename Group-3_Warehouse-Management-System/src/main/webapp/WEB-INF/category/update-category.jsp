<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Update Category</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        </head>

        <body class="d-flex justify-content-center align-items-center vh-100 bg-light">
            <jsp:include page="/WEB-INF/common/sidebar.jsp" />

            <form action="update-category" method="post"
                class="border border-secondary rounded shadow bg-white w-50 p-4 main-content">
                <h2 class="text-center mb-4 text-primary">Update Category</h2>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger">
                        ${error}
                    </div>
                </c:if>

                <input type="hidden" name="id" value="${category.id}">

                <div class="mb-3">
                    <label class="form-label fw-bold">Category Name</label>
                    <input name="name" class="form-control" placeholder="Enter category name..." required
                        maxlength="100" value="${category.name}">
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Description</label>
                    <textarea name="description" class="form-control" rows="3"
                        placeholder="Enter category description...">${category.description}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold d-block">Status</label>
                    <div class="btn-group w-100" role="group">
                        <input type="radio" class="btn-check" name="status" id="active" value="true" ${category.active
                            ? 'checked' : '' }>
                        <label class="btn btn-outline-success" for="active"><i class="fas fa-check"></i> Active</label>

                        <input type="radio" class="btn-check" name="status" id="inactive" value="false"
                            ${!category.active ? 'checked' : '' }>
                        <label class="btn btn-outline-danger" for="inactive"><i class="fas fa-times"></i>
                            Inactive</label>
                    </div>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary py-2 fw-bold">
                        <i class="fas fa-save"></i> Update Category
                    </button>
                    <a href="categories" class="btn btn-outline-secondary">
                        <i class="fas fa-arrow-left"></i> Cancel
                    </a>
                </div>
            </form>

            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>