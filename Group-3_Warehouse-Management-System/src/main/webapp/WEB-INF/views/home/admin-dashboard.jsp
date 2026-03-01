<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/dashboard.css">
    <title>JSP Page</title>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>


    <div class="dashboard-container">
        <h1 class="dashboard-header">System Overview</h1>

        <div class="stats-grid">
            <div class="stat-card total">
                <div class="stat-label">Total Users</div>
                <div class="stat-value">${totalUsers}</div>
            </div>
            <div class="stat-card active">
                <div class="stat-label">Active Users</div>
                <div class="stat-value">${activeUsers}</div>
            </div>
            <div class="stat-card inactive">
                <div class="stat-label">Inactive Users</div>
                <div class="stat-value">${inactiveUsers}</div>
            </div>
        </div>

        <h3 class="section-title">User Distribution by Role</h3>
        <div class="role-distribution">
            <div class="role-badge admins">
                <span>Admins:</span>
                <strong>${adminCount}</strong>
            </div>
            <!-- Uncomment when needed -->
            <div class="role-badge managers">
                <span>Managers:</span>
                <strong>${managerCount}</strong>
            </div>
            <div class="role-badge salesmen">
                <span>Salesmen:</span>
                <strong>${salesmanCount}</strong>
            </div>
            <div class="role-badge storekeepers">
                <span>Storekeepers:</span>
                <strong>${storekeeperCount}</strong>
            </div>
        </div>

        <h3 class="section-title">Recent Account Activity</h3>
        <div class="activity-table-container">
            <table class="activity-table">
                <thead>
                <tr>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Activity</th>
                    <th>Time</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty recentActivities}">
                    <tr>
                        <td colspan="7">No data</td>
                    </tr>
                </c:if>
                <c:forEach items="${recentActivities}" var="log">

                    <tr>
                        <td><strong>${log.user.email}</strong></td>
                        <td class="timestamp">${log.user.role.name}</td>
                        <td class="timestamp">${log.activity}</td>
                        <td class="timestamp">${log.time}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</main>
</body>
