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
    <title>User Reset Password List</title>
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
    <h2>List Reset Password Request</h2>
    <table>
        <thead>
        <tr>
            <th>Request ID</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Request Status</th>
            <th>Request Created Time</th>
            <th>Option</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${passwordResetList}" var="p">
            <tr>
                <td>${p.id}</td>
                <td>${p.user.fullName}</td>
                <td>${p.user.email}</td>
                <td>${p.status}</td>
                <td>${p.createdAt}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/password-reset" method="post" class="d-inline">
                        <input type="hidden" name="passwordResetId" value="${p.id}">
                        <input type="hidden" name="userEmail" value="${p.user.email}">
                        <c:if test="${p.status == 'PENDING'}">
                            <button type="submit" name="action" value="Approve" class="btn btn-sm btn-success">
                                Approve
                            </button>
                            <button type="submit" name="action" value="Reject" class="btn btn-sm btn-danger">
                                Reject
                            </button>
                        </c:if>

                    </form>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty passwordResetList}">
            <tr>
                <td colspan="7">No data</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

</body>
</html>

