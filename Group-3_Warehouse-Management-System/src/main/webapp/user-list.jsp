<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/8/2026
  Time: 9:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
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
    <body >
        <jsp:include page="/common/sidebar.jsp" />
        <div class="main-content">
            <h2>List user</h2>
            <c:if test="${sessionScope.user.hasPermission('CREATE_USER')}">
                <a href="/user-create">
                    <button class="btn btn-primary mb-3">Create new user</button>
                </a>
            </c:if>
            <br>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Change password</th>
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
                                <c:choose>

                                    <c:when test="${sessionScope.user.hasPermission('UPDATE_ROLE')}">
                                        <form action="/change-user-status" method="post" class="d-inline">
                                            <input type="hidden" name="userId" value="${u.id}">
                                            <c:if test="${u.active == false}">
                                                <button type="submit" class="btn btn-sm btn-danger">
                                                    Inactive 
                                                </button>
                                            </c:if>
                                            <c:if test="${u.active == true}">
                                                <button type="submit" class="btn btn-sm btn-success">
                                                    Active 
                                                </button>
                                            </c:if>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${u.active}">
                                                <span class="badge bg-success p-2 w-75">ACTIVE</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger p-2 w-75">DEACTIVE</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${sessionScope.user.hasPermission('UPDATE_USER')}">
                                        <a href="/change-password">Change password</a>

                                    </c:when>
                                    <c:otherwise>
                                        <p>Bạn không được cấp quyền</p>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${sessionScope.user.hasPermission('UPDATE_USER')}">
                                        <a href="/user?id=${u.id}" >Detail</a>
                                    </c:when>
                                    <c:otherwise>
                                        <p>Bạn không được cấp quyền</p>
                                    </c:otherwise>
                                </c:choose>


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

