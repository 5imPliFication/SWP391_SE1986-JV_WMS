<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/8/2026
  Time: 9:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User List</title>
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
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<div class="main-content">
    <h2>List user</h2>
    <%-- create new user--%>
    <c:if test="${fn:contains(sessionScope.userPermissions, 'CREATE_USER')}">
        <a href="${pageContext.request.contextPath}/user/create">
            <button class="btn btn-primary mb-3">Create new user</button>
        </a>
    </c:if>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/user/list" method="get"
          class="row g-2 align-items-center mb-3">
        <%--search user by name--%>
        <div class="col-auto">
            <label>
                <input type="text" class="form-control" name="searchName"
                       placeholder="Search by name" value="${searchName}">
            </label>
        </div>

        <%-- sort by name--%>
        <div class="col-auto">
            <label>
                <select name="sortName" class="form-select">
                    <option value="">Sort</option>
                    <option value="asc" ${typeSort=='asc' ? 'selected' : '' }>
                        Name ASC
                    </option>
                    <option value="desc" ${typeSort=='desc' ? 'selected' : '' }>
                        Name DESC
                    </option>
                </select>
            </label>
        </div>

        <%-- filter by role --%>
        <div class="col-auto">
            <label>
                <select name="roleId" class="form-select">

                    <%-- default --%>
                    <option value="" ${empty selectedRoleId ? 'selected' : '' }>
                        All Roles
                    </option>

                    <c:forEach items="${roleList}" var="r">
                        <option value="${r.id}"
                            ${selectedRoleId == r.id.toString() ? 'selected' : '' }>
                                ${r.name}
                        </option>
                    </c:forEach>

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
        <thead>
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Details</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${userList}" var="u">
            <tr>
                <td>${u.id}</td>
                <td>${u.fullName}</td>
                <td>${u.email}</td>
                <td>${u.role.name}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/user?id=${u.id}"
                       class="btn btn-info btn-sm">
                        Detail
                    </a>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty userList}">
            <tr>
                <td colspan="7">No data</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <%-- pagination--%>
    <%--    when total page > 1 -> display--%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">
                    <%-- previous page --%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/user/list?pageNo=${pageNo - 1}&searchName=${param.searchName}&sortName=${param.sortName}&roleId=${selectedRoleId}">
                        Previous
                    </a>
                </li>

                    <%-- current page --%>
                    <%-- display 2 page left--%>
                <c:set var="left" value="${pageNo - 2}"/>
                    <%-- display 2 page right--%>
                <c:set var="right" value="${pageNo + 2}"/>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <%-- alway display first page --%>
                        <c:when test="${i == 1}">
                            <li
                                    class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/user/list?pageNo=${i}&searchName=${param.searchName}&sortName=${param.sortName}&roleId=${selectedRoleId}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- alway display last page --%>
                        <c:when test="${i == totalPages}">
                            <li
                                    class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/user/list?pageNo=${i}&searchName=${param.searchName}&sortName=${param.sortName}&roleId=${selectedRoleId}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display page between --%>
                        <c:when test="${i >= left && i <= right}">
                            <li
                                    class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/user/list?pageNo=${i}&searchName=${param.searchName}&sortName=${param.sortName}&roleId=${selectedRoleId}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display hidden page by ...--%>
                        <c:when
                                test="${i == left - 1 || i == right + 1}">
                            <li class="page-item disabled">
                                <span class="page-link">...</span>
                            </li>
                        </c:when>
                    </c:choose>
                </c:forEach>

                    <%-- next page--%>
                <li
                        class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/user/list?pageNo=${pageNo + 1}&searchName=${param.searchName}&sortName=${param.sortName}&roleId=${selectedRoleId}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>
    <%--message about change status of user--%>
    <div>
        <c:choose>
            <c:when test="${messageStatus}">
                ${messageSuccess}
            </c:when>
            <c:otherwise>
                ${messageFail}
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>