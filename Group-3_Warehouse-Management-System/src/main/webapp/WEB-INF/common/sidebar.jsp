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
                width:290px;
                height:100vh;
                position:fixed;
                left:0;
                top:0;

                overflow-y:auto;
                overflow-x:hidden;

                scrollbar-width:none;
            }

            .sidebar::-webkit-scrollbar{
                display:none;
            }

            .main-header{
                margin-left:290px;
            }

            .main-content{
                margin-left:300px;
                padding:20px;
            }

        </style>

        <title>Warehouse</title>

    </head>

    <body>

        <aside class="sidebar">

            <div class="sidebar-header">
                <h1>Warehouse</h1>
                <p>Management System</p>
            </div>

            <ul class="sidebar-menu">

                <!-- Dashboard -->
                <li>
                    <a href="${pageContext.request.contextPath}/home">
                        <span>Dashboard</span>
                    </a>
                </li>

                <!-- USER -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_USER')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/user/list">
                              <span>User</span>
                          </a>
                      </li>

                </c:if>


                <!-- ROLE -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_ROLE')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/roles">
                              <span>Role</span>
                          </a>
                      </li>

                </c:if>


                <!-- Orders (Salesman) -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role.name eq 'Salesman'
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_ORDER')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/salesman/orders">
                              <span>Orders</span>
                          </a>
                      </li>

                </c:if>


                <!-- Orders (Warehouse) -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role.name eq 'Warehouse'
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_ORDER')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/warehouse/orders">
                              <span>Orders</span>
                          </a>
                      </li>

                </c:if>


                <!-- Coupons -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role.name eq 'Manager'
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_COUPON')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/coupons">
                              <span>Coupons</span>
                          </a>
                      </li>

                </c:if>


                <!-- Password Reset -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_PASSWORD_RESET_REQUEST')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/admin/password-reset">
                              <span>Password Reset Request</span>
                          </a>
                      </li>

                </c:if>


                <!-- Products -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_PRODUCT')}">

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

                </c:if>


                <!-- Import Product -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'IMPORT_PRODUCT')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/inventory/import">
                              <span>Import Product</span>
                          </a>
                      </li>

                </c:if>


                <!-- Purchase Request -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_PURCHASE_REQUEST')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/purchase-request/list">
                              <span>Purchase Request</span>
                          </a>
                      </li>

                </c:if>


                <!-- Import History -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'IMPORT_PRODUCT')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/inventory/import/history">
                              <span>Import History</span>
                          </a>
                      </li>

                </c:if>


                <!-- Out Of Stock -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'IMPORT_PRODUCT')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/inventory/alert">
                              <span>Out of stock alert</span>
                          </a>
                      </li>

                </c:if>


                <!-- Inventory Audit -->
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'READ_AUDIT')}">

                      <li>
                          <a href="${pageContext.request.contextPath}/inventory-audits">
                              <span>Inventory Audit</span>
                          </a>
                      </li>

                </c:if>

            </ul>

        </aside>


        <div class="main-header sticky-top h-100">
            <jsp:include page="/WEB-INF/common/header.jsp"/>
        </div>


        <script>

            document.addEventListener("DOMContentLoaded", function () {

                const currentPath = window.location.pathname;
                let bestMatch = null;
                let bestLength = 0;

                document.querySelectorAll(".sidebar-menu a").forEach(function (link) {

                    const linkPath = new URL(link.href).pathname;

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
