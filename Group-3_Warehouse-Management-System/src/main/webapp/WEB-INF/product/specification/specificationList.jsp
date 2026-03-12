<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <title>Specification Management</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>
        <main class="main-content">
            <h2 class="mb-4">Laptop Specifications</h2>

            <!-- Tabs -->
            <ul class="nav nav-tabs" id="specTab" role="tablist">

                <li class="nav-item">
                    <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#model">Model</button>
                </li>

                <li class="nav-item">
                    <button class="nav-link" data-bs-toggle="tab" data-bs-target="#chip">Chip</button>
                </li>

                <li class="nav-item">
                    <button class="nav-link" data-bs-toggle="tab" data-bs-target="#ram">RAM</button>
                </li>

                <li class="nav-item">
                    <button class="nav-link" data-bs-toggle="tab" data-bs-target="#storage">Storage</button>
                </li>

                <li class="nav-item">
                    <button class="nav-link" data-bs-toggle="tab" data-bs-target="#size">Screen Size</button>
                </li>

            </ul>

            <div class="tab-content mt-3">

                <div class="tab-pane fade show active" id="model">
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
                                <td>${s.index+1}</td>
                                <td>${m.name}</td>
                                <td>${m.brandName}</td>
                                <td>${m.active}</td>
                            </tr>
                        </c:forEach>

                    </c:set>

                    <jsp:include page="/WEB-INF/common/table.jsp"/>


                </div>

                <!-- CHIP TAB -->
                <div class="tab-pane fade" id="chip">

                    <c:set var="tableHeader" scope="request">
                        <tr>
                            <th>#</th>
                            <th>Chip Name</th>
                            <th>Status</th>
                        </tr>
                    </c:set>

                    <c:set var="tableBody" scope="request">
                        <c:forEach var="c" items="${chips}" varStatus="s">
                            <tr>
                                <td>${s.index+1}</td>
                                <td>${c.name}</td>
                                <td>${c.active}</td>
                            </tr>
                        </c:forEach>
                    </c:set>

                    <jsp:include page="/WEB-INF/common/table.jsp"/>

                </div>

                <!-- RAM TAB -->
                <div class="tab-pane fade" id="ram">

                    <c:set var="tableHeader" scope="request">

                        <tr>
                            <th>#</th>
                            <th>RAM</th>
                            <th>Status</th>
                        </tr>
                    </c:set>

                    <c:set var="tableBody" scope="request">                  
                        <c:forEach var="r" items="${rams}" varStatus="s">
                            <tr>
                                <td>${s.index+1}</td>
                                <td>${r.size}</td>
                                <td>${r.active}</td>

                            </tr>
                        </c:forEach>
                    </c:set>

                    <jsp:include page="/WEB-INF/common/table.jsp"/>

                </div>

                <!-- STORAGE TAB -->
                <div class="tab-pane fade" id="storage">

                    <c:set var="tableHeader" scope="request">

                        <tr>
                            <th>#</th>
                            <th>Storage</th>
                            <th>Status</th>
                        </tr>
                    </c:set>

                    <c:set var="tableBody" scope="request">                  
                        <c:forEach var="s" items="${storages}" varStatus="c">
                            <tr>
                                <td>${c.index+1}</td>
                                <td>${s.size}</td>
                                <td>${s.active}</td>

                            </tr>
                        </c:forEach>
                    </c:set>

                    <jsp:include page="/WEB-INF/common/table.jsp"/>

                </div>

                <!-- SIZE TAB -->
                <div class="tab-pane fade" id="size">

                    <c:set var="tableHeader" scope="request">
                        <tr>
                            <th>#</th>
                            <th>Screen Size</th>
                            <th>Status</th>
                        </tr>
                    </c:set>

                    <c:set var="tableBody" scope="request">                  
                        <c:forEach var="s" items="${sizes}" varStatus="c">
                            <tr>
                                <td>${c.index+1}</td>
                                <td>${s.size}</td>
                                <td>${s.active}</td>

                            </tr>
                        </c:forEach>
                    </c:set>

                    <jsp:include page="/WEB-INF/common/table.jsp"/>

                </div>

            </div>
        </main>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
