<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home.css">

        <style>
            :root {
                /* Bảng màu Dark Theme mới */
                --sidebar-bg: #111827;           /* Đen xanh sâu (Rất chuyên nghiệp) */
                --sidebar-text: #9ca3af;         /* Chữ xám nhạt để không bị chói */
                --sidebar-hover-bg: #1f2937;      /* Màu nền khi di chuột qua */
                --sidebar-hover-text: #ffffff;    /* Chữ trắng sáng khi di chuột */
                --sidebar-active-bg: #3b82f6;     /* Màu xanh làm điểm nhấn cho mục đang chọn */
                --sidebar-active-text: #ffffff;   /* Chữ trắng cho mục đang chọn */
                --sidebar-border: #1f2937;       /* Đường kẻ chia tách tối màu */
            }

            .sidebar {
                width: 250px;
                height: 100vh;
                position: fixed;
                left: 0;
                top: 0;
                background: var(--sidebar-bg);
                color: var(--sidebar-text);
                overflow-y: auto;
                overflow-x: hidden;
                scrollbar-width: none;
                border-right: 1px solid var(--sidebar-border);
                transition: all 0.3s ease;
                font-family: 'Inter', 'Segoe UI', Roboto, sans-serif;
                z-index: 1000;
            }

            .sidebar::-webkit-scrollbar {
                display: none;
            }

            .sidebar-header {
                padding: 24px 20px 16px;
                border-bottom: 1px solid var(--sidebar-border);
                margin-bottom: 16px;
            }

            .sidebar-header h1 {
                font-size: 1.4rem;
                font-weight: 700;
                color: #ffffff; /* Tiêu đề chính trắng hoàn toàn */
                margin: 0;
            }

            .sidebar-header p {
                font-size: 0.75rem;
                color: #6366f1; /* Màu tím xanh làm điểm nhấn nhẹ */
                margin: 4px 0 0 0;
                text-transform: uppercase;
                letter-spacing: 1px;
                font-weight: 600;
            }

            .sidebar-category {
                font-size: 0.7rem;
                text-transform: uppercase;
                font-weight: 700;
                color: #4b5563; /* Màu phân loại tối hơn một chút */
                margin: 24px 20px 8px 20px;
                letter-spacing: 1px;
            }

            .sidebar-menu {
                list-style: none;
                padding: 0;
                margin: 0;
                padding-bottom: 40px;
            }

            .sidebar a {
                text-decoration: none;
                color: var(--sidebar-text);
                display: flex;
                align-items: center;
                padding: 12px 20px;
                margin: 4px 12px;
                border-radius: 8px;
                transition: all 0.2s ease;
                font-size: 0.9rem;
                font-weight: 500;
            }

            .sidebar a:hover {
                background: var(--sidebar-hover-bg);
                color: var(--sidebar-hover-text);
            }

            .sidebar a.active {
                background: var(--sidebar-active-bg);
                color: var(--sidebar-active-text);
                box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.2);
                font-weight: 600;
            }

            /* Đảm bảo nội dung chính không bị đè lên */
            .main-header, .main-content {
                margin-left: 250px;
            }
        </style>

        <title>Warehouse</title>

    </head>

    <body>

        <c:set var="userRole" value="${sessionScope.user.role}" />
        <c:set var="permissions" value="${sessionScope.userPermissions}" />
        <c:set var="hasAccess" value="${sessionScope.user != null and userRole != null and userRole.active}" />

        <c:set var="showAccountAccess" value="${hasAccess and (fn:contains(permissions, 'READ_USER') or fn:contains(permissions, 'READ_ROLE') or fn:contains(permissions, 'READ_PASSWORD_RESET_REQUEST'))}" />
        <c:set var="showCatalog" value="${hasAccess and fn:contains(permissions, 'READ_PRODUCT')}" />
        <c:set var="showTransactions" value="${hasAccess and ((userRole.name eq 'Salesman' and fn:contains(permissions, 'READ_ORDER')) or (userRole.name eq 'Warehouse' and fn:contains(permissions, 'READ_ORDER')) or fn:contains(permissions, 'READ_PURCHASE_REQUEST'))}" />
        <c:set var="showInventory" value="${hasAccess and (fn:contains(permissions, 'IMPORT_PRODUCT') or fn:contains(permissions, 'EXPORT_PRODUCT') or fn:contains(permissions, 'READ_AUDIT'))}" />
        <c:set var="showReports" value="${hasAccess and userRole.name eq 'Manager' and fn:contains(permissions, 'READ_REPORT')}" />

        <aside class="sidebar">

            <div class="sidebar-header">
                <h1>Warehouse</h1>
                <p>Management System</p>
            </div>

            <ul class="sidebar-menu">

                <!-- MAIN -->
                <div class="sidebar-category">Main</div>
                <li>
                    <a href="${pageContext.request.contextPath}/home" class="nav-link">
                        <span>Dashboard</span>
                    </a>
                </li>

                <!-- CATALOG -->
                <c:if test="${showCatalog}">
                    <div class="sidebar-category">Catalog</div>
                    <li>
                        <a href="${pageContext.request.contextPath}/products">
                            <span>Products</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/brands">
                            <span>Brands</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/categories">
                            <span>Categories</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/specification/model">
                            <span>Specification</span>
                        </a>
                    </li>
                </c:if>

                <!-- TRANSACTIONS -->
                <c:if test="${showTransactions}">
                    <div class="sidebar-category">Transactions</div>
                    
                    <c:if test="${userRole.name eq 'Salesman' and fn:contains(permissions, 'READ_ORDER')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/salesman/orders">
                                <span>Orders</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${userRole.name eq 'Warehouse' and fn:contains(permissions, 'READ_ORDER')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/warehouse/orders">
                                <span>Orders</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${fn:contains(permissions, 'READ_PURCHASE_REQUEST')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/purchase-request/list">
                                <span>Purchase Request</span>
                            </a>
                        </li>
                    </c:if>
                </c:if>

                <!-- INVENTORY -->
                <c:if test="${showInventory}">
                    <div class="sidebar-category">Inventory Management</div>

                    <c:if test="${fn:contains(permissions, 'IMPORT_PRODUCT')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/inventory/import-history">
                                <span>Import History</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${fn:contains(permissions, 'EXPORT_PRODUCT')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/inventory/export-history">
                                <span>Export History</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${fn:contains(permissions, 'IMPORT_PRODUCT')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/inventory">
                                <span>Inventory</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${fn:contains(permissions, 'READ_AUDIT')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/inventory-audits">
                                <span>Inventory Audit</span>
                            </a>
                        </li>
                    </c:if>
                </c:if>

                <!-- REPORTS -->
                <c:if test="${showReports}">
                    <div class="sidebar-category">Reports</div>

                    <li>
                        <a href="${pageContext.request.contextPath}/stock-history">
                            <span>Stock Movement History</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/report">
                            <span>Detailed Report</span>
                        </a>
                    </li>
                </c:if>

                <!-- ACCOUNT & ACCESS -->
                <c:if test="${showAccountAccess}">
                    <div class="sidebar-category">Account & Access</div>

                    <c:if test="${fn:contains(permissions, 'READ_USER')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/user/list">
                                <span>Users</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${fn:contains(permissions, 'READ_ROLE')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/roles">
                                <span>Roles</span>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${fn:contains(permissions, 'READ_PASSWORD_RESET_REQUEST')}">
                        <li>
                            <a href="${pageContext.request.contextPath}/admin/password-reset">
                                <span>Password Resets</span>
                            </a>
                        </li>
                    </c:if>
                </c:if>

            </ul>

        </aside>

        <jsp:include page="/WEB-INF/common/header.jsp"/>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const currentPath = window.location.pathname;
                let bestMatch = null;
                let bestLength = 0;

                document.querySelectorAll(".sidebar-menu a").forEach(function (link) {
                    const linkPath = new URL(link.href).pathname;

                    // Dashboard
                    if (linkPath === "/home" && currentPath.includes("dashboard")) {
                        link.classList.add("active");
                        return;
                    }

                    // Specification group
                    if (linkPath.startsWith("/specification") && currentPath.startsWith("/specification")) {
                        link.classList.add("active");
                        return;
                    }

                    // Match logic
                    if (currentPath.startsWith(linkPath) && linkPath.length > bestLength) {
                        bestMatch = link;
                        bestLength = linkPath.length;
                    }
                });

                if (bestMatch) {
                    bestMatch.classList.add("active");
                }
            });
        </script>

    </body>
</html>
