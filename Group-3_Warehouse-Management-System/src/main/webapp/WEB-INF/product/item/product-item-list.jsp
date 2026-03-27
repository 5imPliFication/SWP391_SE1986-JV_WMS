<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Item List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            padding: 8px;
            border: 1px solid #ccc;
            text-align: left;
        }

        th {
            background-color: #f4f4f4;
        }

        .product-img {
            width: 40px;
            height: 40px;
            object-fit: cover;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<div class="main-content">

    <h2>Product Item List</h2>

    <div class="fs-5 fw-semibold text-dark">
        ${product.name}
    </div>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/products/items" method="get"
          class="row g-2 align-items-center mb-3">
        <%--Hidden productId--%>
        <input type="hidden" name="productId" value="${product.id}"/>

        <%--search by serial number--%>
        <div class="col-auto">
            <label>
                <input type="text" class="form-control" name="searchSerial" placeholder="Search by serial number"
                       value="${param.searchSerial}">
            </label>
        </div>

        <%--sort by status--%>
        <div class="col-auto">
            <label>
                <select name="isActive" class="form-select">
                    <option value="">All Status</option>
                    <option value="true"  ${param.isActive == 'true' ? 'selected' : ''}>Available</option>
                    <option value="false" ${param.isActive == 'false' ? 'selected' : ''}>Unavailable</option>
                </select>
            </label>
        </div>

        <div class="col-auto">
            <button type="submit" class="btn btn-outline-primary">
                Search
            </button>
        </div>
    </form>
    <table>
        <tbody>
        <c:set var="tableHeader" scope="request">
            <tr>
                <th>Serial Number</th>
                <th>Imported Price</th>
                <th>Selling Price</th>
                <th>Unit Price</th>
                <th>Imported At</th>
                <th>Updated At</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </c:set>

        <c:set var="tableBody" scope="request">
            <c:choose>
                <c:when test="${empty productItems}">
                    <tr>
                        <td colspan="8" class="text-center">No data</td>
                    </tr>
                </c:when>
                <c:otherwise>

                    <c:forEach items="${productItems}" var="pi">
                        <tr>
                            <td>${pi.serial}</td>
                            <td>
                                <fmt:formatNumber value="${pi.importedPrice}" type="number" groupingUsed="true"/>
                            </td>
                            <td>
                                <fmt:formatNumber value="${pi.currentPrice}" type="number" groupingUsed="true"/>
                            </td>
                            <td>VND</td>
                            <td>
                                    <%--Convert LocalDateTime to Date for JSTL formatting--%>
                                <fmt:parseDate value="${pi.importedAt}"
                                               pattern="yyyy-MM-dd'T'HH:mm"
                                               var="parsedImportedDate"
                                               type="both"/>
                                <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                                value="${parsedImportedDate}"/>
                            </td>
                            <td>
                                    <%--Convert LocalDateTime to Date for JSTL formatting--%>
                                <fmt:parseDate value="${pi.updatedAt}"
                                               pattern="yyyy-MM-dd'T'HH:mm"
                                               var="parsedUpdatedDate"
                                               type="both"/>
                                <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                                value="${parsedUpdatedDate}"/>
                            </td>
                            <td>${(pi.isActive == true) ? 'Available' : 'Unavailable'}</td>
                            <td>
                                <c:if test="${sessionScope.user != null
                                              and sessionScope.user.role != null
                                              and sessionScope.user.role.active
                                              and fn:contains(sessionScope.userPermissions, 'UPDATE_PRODUCT_ITEM')}">

                                    <form method="post"
                                          action="${pageContext.request.contextPath}/products/items/update">
                                        <input type="hidden" name="productItemId" value="${pi.id}">
                                            <%--Use for redirect exactly--%>
                                        <input type="hidden" name="productId" value="${pi.productId}">
                                        <input type="hidden" name="pageNo" value="${param.pageNo}">
                                        <input type="hidden" name="searchSerial" value="${param.searchSerial}">
                                        <input type="hidden" name="isActive" value="${param.isActive}">
                                        <c:if test="${pi.isActive == true }">
                                            <button class="btn-warning" type="submit" name="btnChangeStatus"
                                                    value="unavailable"
                                                    onclick="return confirm('Are you sure you want to mark this item as unavailable?');">
                                                Mark Unavailable
                                            </button>
                                        </c:if>
<%--                                        <c:if test="${pi.isActive == false }">--%>
<%--                                            <button class="btn-success" type="submit" name="btnChangeStatus"--%>
<%--                                                    value="available">--%>
<%--                                                Mark Available--%>
<%--                                            </button>--%>
<%--                                        </c:if>--%>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:set>

        <jsp:include page="/WEB-INF/common/table.jsp"/>

        <c:if test="${empty productItems}">
            <tr>
                <td colspan="8">No data</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <br>
    <a href="${pageContext.request.contextPath}/products">Back to product list</a>

    <%-- pagination--%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">

                    <%-- previous page --%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/products/items?pageNo=${pageNo - 1}&productId=${param.productId}&searchSerial=${param.searchSerial}&isActive=${param.isActive}">
                        Previous
                    </a>
                </li>

                    <%-- current page  --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/products/items?pageNo=${i}&productId=${param.productId}&searchSerial=${param.searchSerial}&isActive=${param.isActive}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/products/items?pageNo=${pageNo + 1}&productId=${param.productId}&searchSerial=${param.searchSerial}&isActive=${param.isActive}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>