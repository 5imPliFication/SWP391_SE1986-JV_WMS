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
            <ul class="nav nav-tabs ">
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/specification/model">
                        Model
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active"
                       href="${pageContext.request.contextPath}/specification/chip">
                        Chip
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/specification/ram">
                        Ram
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link "
                       href="${pageContext.request.contextPath}/specification/size">
                        Size
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link"
                       href="${pageContext.request.contextPath}/specification/storage">
                        Storage
                    </a>
                </li>
            </ul>
            <!-- ================= Chip ================= -->
            <div class="card-body shadow-lg rounded">

                <h4>Chips</h4>

                <c:set var="tableHeader" scope="request">
                    <tr>
                        <th style="width: 100px">#</th>
                        <th>Chip Name</th>
                        <th style="width: 400px">Status</th>
                        <th style="width: 200px">Action</th>
                    </tr>
                </c:set>

                <c:set var="tableBody" scope="request">
                    <c:forEach var="c" items="${chips}" varStatus="s">
                        <tr>
                            <td>${(pageNo - 1) * 2 + s.index + 1}</td>
                            <td>${c.name}</td>
                            <td >
                                <jsp:include page="/WEB-INF/common/statusBadge.jsp">
                                    <jsp:param name="active" value="${c.active}" />
                                </jsp:include>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/active-chip" method="post">

                                    <input type="hidden" name="id" value="${c.id}" />
                                    <input type="hidden" name="active" value="${!c.active}" />

                                    <button class="btn btn-sm ${!c.active ? 'btn-success' : 'btn-danger'}">
                                        ${!c.active ? 'Active' : 'Inactive'}
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
                    <jsp:param name="baseUrl" value="${pageContext.request.contextPath}/chip" />
                </jsp:include>
            </div>

        </main>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
