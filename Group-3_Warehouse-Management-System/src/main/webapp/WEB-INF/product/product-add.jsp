<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add new product</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="d-flex justify-content-center align-items-center vh-100 bg-light">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class="container mt-5 main-content ">
    <div class="row justify-content-center">
        <div class="col-md-6">

            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Add new product</h4>
                </div>

                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/products/add" method="post">

                        <!-- Product Name -->
                        <div class="mb-3">
                            <label class="form-label">Product Name</label>
                            <input type="text"
                                   name="productName"
                                   class="form-control"
                                   placeholder="Enter product name"
                                   required>
                        </div>

                        <!-- Description -->
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <input type="text"
                                   name="productDescription"
                                   class="form-control"
                                   placeholder="Enter description"
                                   required>
                        </div>

                        <!-- Image URL -->
                        <div class="mb-3">
                            <label class="form-label">Image URL</label>
                            <input type="text"
                                   name="imgUrl"
                                   class="form-control"
                                   placeholder="Enter image url"
                                   required>
                        </div>

                        <!-- Brand -->
                        <div class="mb-3">
                            <label class="form-label">Brand</label>
                            <select name="brandId" class="form-select">
                                <c:forEach items="${brands}" var="b">
                                    <option value="${b.id}">${b.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Category -->
                        <div class="mb-3">
                            <label class="form-label">Category</label>
                            <select name="categoryId" class="form-select">
                                <c:forEach items="${categories}" var="c">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/products"
                               class="btn btn-secondary">
                                Back to products list
                            </a>
                            <button type="submit" class="btn btn-primary">
                                Add product
                            </button>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>