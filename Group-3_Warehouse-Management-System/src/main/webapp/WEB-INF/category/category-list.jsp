<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

    <head>
        <title>Category List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home.css">
    </head>

    <body >
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <div class="main-content ">

            <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                <h4 class="mb-0 text-primary">Category Management</h4>
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'CREATE_PRODUCT')}">
                      <a href="create-category" class="btn btn-primary">
                          <i class="fas fa-plus"></i> Add New Category
                      </a>
                </c:if>

            </div>
            <c:if test="${not empty param.status}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    Status: ${param.status}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"
                            aria-label="Close"></button>
                </div>
            </c:if>

            <div class="card mb-3">
                <div class="card-body py-3">
                    <form method="get" action="${pageContext.request.contextPath}/categories">
                        <div class="row align-items-end g-3">
                            <div class="col-md-4">
                                <label for="searchName" class="form-label mb-1" style="font-size:13px;font-weight:600;color:#555;">
                                    <i class="fas fa-search me-1"></i>Search Name
                                </label>
                                <input type="text" class="form-control" id="searchName" name="searchName"
                                       value="${searchName}" placeholder="Search category name...">
                            </div>
                            <div class="col-md-3">
                                <label for="statusFilter" class="form-label mb-1" style="font-size:13px;font-weight:600;color:#555;">
                                    <i class="fas fa-filter me-1"></i>Status
                                </label>
                                <select class="form-select" id="statusFilter" name="statusFilter">
                                    <option value="">All Status</option>
                                    <option value="1" ${statusFilter == '1' ? 'selected' : ''}>Active</option>
                                    <option value="0" ${statusFilter == '0' ? 'selected' : ''}>Inactive</option>
                                </select>
                            </div>
                            <div class="col-md-3 d-flex gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search me-1"></i>Filter
                                </button>
                                <a href="${pageContext.request.contextPath}/categories" class="btn btn-outline-secondary">
                                    <i class="fas fa-redo"></i>
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <div class="table-responsive">

                <!-- HEADER -->
                <c:set var="tableHeader" scope="request">
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </c:set>

                <!-- BODY -->
                <c:set var="tableBody" scope="request">

                    <c:forEach var="category" items="${categories}">
                        <tr>

                            <td>${category.name}</td>

                            <td>${category.description}</td>

                            <td>
                                <jsp:include page="/WEB-INF/common/statusBadge.jsp">
                                    <jsp:param name="active" value="${category.isActive}" />
                                </jsp:include>
                            </td>

                            <td>

                                <c:if test="${sessionScope.user != null
                                              and sessionScope.user.role != null
                                              and sessionScope.user.role.active
                                              and fn:contains(sessionScope.userPermissions, 'UPDATE_PRODUCT')}">

                                      <a href="update-category?id=${category.id}"
                                         class="btn btn-sm btn-warning text-white me-2"
                                         title="Edit">

                                          <i class="fas fa-edit"></i> Edit

                                      </a>

                                </c:if>

                                <a href="category-detail?id=${category.id}"
                                   class="btn btn-sm btn-info text-white me-2"
                                   title="View Detail">

                                    <i class="fas fa-eye"></i> View

                                </a>

                            </td>

                        </tr>
                    </c:forEach>

                    <c:if test="${empty categories}">
                        <tr>
                            <td colspan="4"
                                class="text-center text-muted py-4">
                                No categories found.
                            </td>
                        </tr>
                    </c:if>

                </c:set>

                <!-- COMMON TABLE -->
                <jsp:include page="/WEB-INF/common/table.jsp"/>


            </div>

            <%-- Pagination --%>
            <c:if test="${totalPages > 1}">
                <div class="d-flex justify-content-between align-items-center mt-4 mb-5">
                    <div class="text-muted font-weight-bold" style="font-size: 14px;">
                        Page ${currentPage} of ${totalPages}
                    </div>
                    <nav aria-label="Page navigation">
                        <ul class="pagination mb-0">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/categories?page=${currentPage - 1}&searchName=${searchName != null ? searchName : ''}&statusFilter=${statusFilter != null ? statusFilter : ''}">
                                    <i class="fas fa-chevron-left mr-1"></i>Previous
                                </a>
                            </li>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link"
                                               href="${pageContext.request.contextPath}/categories?page=${i}&searchName=${searchName != null ? searchName : ''}&statusFilter=${statusFilter != null ? statusFilter : ''}">${i}</a>
                                        </li>
                                    </c:when>
                                    <c:when test="${i == currentPage - 3 || i == currentPage + 3}">
                                        <li class="page-item disabled"><span class="page-link">...</span></li>
                                    </c:when>
                                </c:choose>
                            </c:forEach>

                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/categories?page=${currentPage + 1}&searchName=${searchName != null ? searchName : ''}&statusFilter=${statusFilter != null ? statusFilter : ''}">
                                    Next<i class="fas fa-chevron-right ml-1"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </div>

        <style>
            .pagination {
                margin: 0;
            }
            .page-link {
                border-radius: 6px;
                margin: 0 2px;
                border: 1px solid #e2e8f0;
                color: #2563eb;
            }
            .page-item.active .page-link {
                background-color: #2563eb;
                border-color: #2563eb;
                color: #fff;
            }
        </style>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>