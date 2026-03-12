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
            <!-- ================= MODEL ================= -->
            <ul class="nav nav-tabs ">
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
                    <a class="nav-link ${tab == 'ram' ? 'active' : ''}"
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

            <div class="card-body shadow-lg rounded">
                <h4>Models</h4>

                <c:set var="tableHeader" scope="request">
                    <tr>
                        <th>#</th>
                        <th>Model Name</th>
                        <th>Brand</th>
                        <th>Status</th>
                    </tr>
                </c:set>

                <c:set var="tableBody" scope="request">
                    <c:forEach var="m" items="${models}" varStatus="s">
                        <tr>
                            <td>${(pageNo - 1) * 2 + s.index + 1}</td>
                            <td>${m.name}</td>
                            <td>${m.brandName}</td>
                            <td>${m.active}</td>
                        </tr>
                    </c:forEach>
                </c:set>

                <jsp:include page="/WEB-INF/common/table.jsp"/>

                <!-- PAGINATION -->

                <jsp:include page="/WEB-INF/common/pagination.jsp">
                    <jsp:param name="pageNo" value="${pageNo}" />
                    <jsp:param name="totalPages" value="${totalPages}" />
                    <jsp:param name="baseUrl" value="${pageContext.request.contextPath}/model" />
                </jsp:include>
            </div>
        </main>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
