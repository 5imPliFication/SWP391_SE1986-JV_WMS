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

            .sidebar{
                width:300px;
                height:100vh;
                position:fixed;
                left:0;
                top:0;

                overflow-y:auto;
                overflow-x:hidden;

                scrollbar-width:none;
            }

            a{
                text-decoration: none
            }
            .sidebar::-webkit-scrollbar{
                display:none;
            }

            .main-header{
                margin-left:300px;
            }

            .main-content{
                margin-left:300px;
                padding:20px;
            }
            .bootstrap-select .dropdown-menu{
                z-index:9999 !important;
            }

            /* Sidebar Container */
            .sidebar {
                width: 260px;
                background-color: #0f172a; /* Nền tối sang trọng */
                color: #94a3b8;
                min-height: 100vh;
                box-shadow: 2px 0 8px rgba(0,0,0,0.1);
                display: flex;
                flex-direction: column;
            }

            /* Sidebar Header */
            .sidebar-header {
                padding: 24px 20px;
                border-bottom: 1px solid #1e293b;
                color: #f8fafc;
            }
            .sidebar-header h1 {
                font-size: 1.5rem;
                margin-bottom: 4px;
            }
            .sidebar-header p {
                font-size: 0.85rem;
                color: #64748b;
                margin: 0;
            }

            /* Sidebar Menu */
            .sidebar-menu {
                list-style: none;
                padding: 16px 0;
                margin: 0;
                flex-grow: 1;
                overflow-y: auto;
            }

            .sidebar-menu li {
                margin-bottom: 4px;
            }

            .sidebar-menu li a {
                display: flex;
                align-items: center;
                padding: 12px 24px;
                color: #cbd5e1;
                font-size: 0.95rem;
                font-weight: 500;
                transition: all 0.2s ease;
                border-left: 4px solid transparent; /* Dành cho trạng thái active */
            }

            /* UX: Hiệu ứng Hover và Active */
            .sidebar-menu li a:hover {
                background-color: #1e293b;
                color: #ffffff;
                text-decoration: none;
            }

            .sidebar-menu li a.active {
                background-color: #1e293b;
                color: #38bdf8; /* Xanh ngọc nổi bật */
                border-left: 4px solid #38bdf8;
                font-weight: 600;
            }

            /* Khoảng cách cho icon */
            .sidebar-icon {
                margin-right: 12px;
                font-size: 1.1rem;
                width: 24px;
                text-align: center;
            }

            /* Submenu styles */
            .sidebar-submenu {
                list-style: none;
                padding: 0;
                background-color: #0b1120; /* Tối hơn một chút để phân biệt */
            }
            .sidebar-submenu li a {
                padding: 10px 24px 10px 48px;
                font-size: 0.9rem;
                border-left: none;
                color: #94a3b8;
            }
            .sidebar-submenu li a:hover {
                color: #ffffff;
                background-color: #1e293b;
            }
            .sidebar-submenu li a.active {
                color: #38bdf8;
                border-left: none;
                background-color: transparent;
            }
            /* Chevron rotation */
            .menu-toggle[aria-expanded="true"] .fa-chevron-down {
                transform: rotate(180deg);
                transition: transform 0.2s ease;
            }
            .menu-toggle .fa-chevron-down {
                transform: rotate(0deg);
                transition: transform 0.2s ease;
            }
        </style>

        <title>Warehouse</title>

    </head>

    <body>

        <aside class="sidebar">

            <div class="sidebar-header pt-4">
                <h1 style="font-weight: 800; letter-spacing: -1px;">📦 WMS</h1>
                <p>Laptop Warehouse</p>
            </div>

            <ul class="sidebar-menu">

                <c:set var="roleName" value="${sessionScope.user != null and sessionScope.user.role != null ? sessionScope.user.role.name : ''}"/>
                <c:set var="canReadProduct" value="false"/>
                <c:set var="canReadOrder" value="false"/>
                <c:set var="canReadPurchaseRequest" value="false"/>
                <c:set var="canImportProduct" value="false"/>
                <c:set var="canExportProduct" value="false"/>
                <c:set var="canReadAudit" value="false"/>
                <c:set var="canReadUser" value="false"/>
                <c:set var="canReadRole" value="false"/>
                <c:set var="canReadPasswordReset" value="false"/>
                <c:set var="canReadReport" value="false"/>

                <c:forEach items="${sessionScope.userPermissions}" var="perm">
                    <c:if test="${perm eq 'READ_PRODUCT'}"><c:set var="canReadProduct" value="true"/></c:if>
                    <c:if test="${perm eq 'READ_ORDER'}"><c:set var="canReadOrder" value="true"/></c:if>
                    <c:if test="${perm eq 'READ_PURCHASE_REQUEST'}"><c:set var="canReadPurchaseRequest" value="true"/></c:if>
                    <c:if test="${perm eq 'IMPORT_PRODUCT'}"><c:set var="canImportProduct" value="true"/></c:if>
                    <c:if test="${perm eq 'EXPORT_PRODUCT'}"><c:set var="canExportProduct" value="true"/></c:if>
                    <c:if test="${perm eq 'READ_AUDIT'}"><c:set var="canReadAudit" value="true"/></c:if>
                    <c:if test="${perm eq 'READ_USER'}"><c:set var="canReadUser" value="true"/></c:if>
                    <c:if test="${perm eq 'READ_ROLE'}"><c:set var="canReadRole" value="true"/></c:if>
                    <c:if test="${perm eq 'READ_PASSWORD_RESET_REQUEST'}"><c:set var="canReadPasswordReset" value="true"/></c:if>
                    <c:if test="${perm eq 'READ_REPORT'}"><c:set var="canReadReport" value="true"/></c:if>
                </c:forEach>

                <!-- Dashboard -->
                <li>
                    <a href="${pageContext.request.contextPath}/home" class="nav-link">
                        <span>Dashboard</span>
                    </a>
                </li>

                <!-- Products Group -->
                <c:if test="${sessionScope.user != null and canReadProduct}">
                    <li>
                        <a href="#productSubmenu" data-toggle="collapse" data-bs-toggle="collapse" aria-expanded="false" class="menu-toggle d-flex justify-content-between align-items-center">
                            <span>Products Setup</span>
                            <i class="fas fa-chevron-down" style="font-size: 0.8rem;"></i>
                        </a>
                        <ul class="collapse sidebar-submenu" id="productSubmenu" data-parent=".sidebar-menu">
                            <li><a href="${pageContext.request.contextPath}/products">Products</a></li>
                            <li><a href="${pageContext.request.contextPath}/brands">Brands</a></li>
                            <li><a href="${pageContext.request.contextPath}/categories">Categories</a></li>
                            <li><a href="${pageContext.request.contextPath}/specification/model">Specification</a></li>
                        </ul>
                    </li>
                </c:if>

                <!-- Inventory & Orders Group -->
                <c:if test="${sessionScope.user != null and (canReadOrder or canReadPurchaseRequest or canImportProduct or canExportProduct or canReadAudit)}">
                    <li>
                        <a href="#inventorySubmenu" data-toggle="collapse" data-bs-toggle="collapse" aria-expanded="false" class="menu-toggle d-flex justify-content-between align-items-center">
                            <span>Inventory & Orders</span>
                            <i class="fas fa-chevron-down" style="font-size: 0.8rem;"></i>
                        </a>
                        <ul class="collapse sidebar-submenu" id="inventorySubmenu" data-parent=".sidebar-menu">
                            <!-- Orders (Salesman) -->
                            <c:if test="${roleName eq 'Salesman' and canReadOrder}">
                                <li><a href="${pageContext.request.contextPath}/salesman/orders">Salesman Orders</a></li>
                            </c:if>
                            <!-- Orders (Warehouse) -->
                            <c:if test="${roleName eq 'Warehouse' and canReadOrder}">
                                <li><a href="${pageContext.request.contextPath}/warehouse/orders">Warehouse Orders</a></li>
                            </c:if>
                            <!-- Purchase Request -->
                            <c:if test="${canReadPurchaseRequest}">
                                <li><a href="${pageContext.request.contextPath}/purchase-request/list">Purchase Request</a></li>
                            </c:if>
                            <!-- Import History -->
                            <c:if test="${canImportProduct}">
                                <li><a href="${pageContext.request.contextPath}/inventory/import-history">Import History</a></li>
                            </c:if>
                            <!-- Export History -->
                            <c:if test="${canExportProduct}">
                                <li><a href="${pageContext.request.contextPath}/inventory/export-history">Export History</a></li>
                            </c:if>
                            <!-- Out Of Stock -->
                            <c:if test="${canImportProduct}">
                                <li><a href="${pageContext.request.contextPath}/inventory/alert">Out of stock alert</a></li>
                            </c:if>
                            <!-- Inventory Audit -->
                            <c:if test="${canReadAudit}">
                                <li><a href="${pageContext.request.contextPath}/inventory-audits">Inventory Audit</a></li>
                            </c:if>
                        </ul>
                    </li>
                </c:if>

                <!-- Access Control Group -->
                <c:if test="${sessionScope.user != null and (canReadUser or canReadRole or canReadPasswordReset)}">
                    <li>
                        <a href="#accessSubmenu" data-toggle="collapse" data-bs-toggle="collapse" aria-expanded="false" class="menu-toggle d-flex justify-content-between align-items-center">
                            <span>Access Control</span>
                            <i class="fas fa-chevron-down" style="font-size: 0.8rem;"></i>
                        </a>
                        <ul class="collapse sidebar-submenu" id="accessSubmenu" data-parent=".sidebar-menu">
                            <c:if test="${canReadUser}">
                                <li><a href="${pageContext.request.contextPath}/user/list">User</a></li>
                            </c:if>
                            <c:if test="${canReadRole}">
                                <li><a href="${pageContext.request.contextPath}/roles">Role</a></li>
                            </c:if>
                            <c:if test="${canReadPasswordReset}">
                                <li><a href="${pageContext.request.contextPath}/admin/password-reset">Password Reset</a></li>
                            </c:if>
                        </ul>
                    </li>
                </c:if>

                <!-- Reports Group -->
                <c:if test="${sessionScope.user != null and canReadReport}">
                    <li>
                        <a href="#reportSubmenu" data-toggle="collapse" data-bs-toggle="collapse" aria-expanded="false" class="menu-toggle d-flex justify-content-between align-items-center">
                            <span>Reports</span>
                            <i class="fas fa-chevron-down" style="font-size: 0.8rem;"></i>
                        </a>
                        <ul class="collapse sidebar-submenu" id="reportSubmenu" data-parent=".sidebar-menu">
                            <li><a href="${pageContext.request.contextPath}/summary_report">Summary Report</a></li>
                            <li><a href="${pageContext.request.contextPath}/report">Detailed Report</a></li>
                        </ul>
                    </li>
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

                    // Logic cũ
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
