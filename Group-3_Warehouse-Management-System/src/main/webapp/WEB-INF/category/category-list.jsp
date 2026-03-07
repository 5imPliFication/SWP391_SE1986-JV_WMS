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
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>