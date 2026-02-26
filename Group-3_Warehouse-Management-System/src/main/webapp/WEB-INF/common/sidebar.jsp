<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% String uri = request.getRequestURI(); %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home.css">
        <style>
            .sidebar {
                height: 100vh;
                overflow-y: auto;
                overflow-x: hidden;
            }
        </style>
        <title>Error Page</title>
    </head>
    <aside class="sidebar">
        <div class="sidebar-header">
            <h1>Warehouse</h1>
            <p>Management System</p>
        </div>

        <ul class="sidebar-menu">

            <!-- Dashboard luôn hiển thị -->
            <li>
                <a href="${pageContext.request.contextPath}/home"
                   class="<%= (uri.endsWith("home") || uri.contains("home")) ? "active" : "" %>">
                    <span>Dashboard</span>
                </a>
            </li>

            <!-- USER -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_USER')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/user/list"
                         class="<%= (uri.contains("user")) ? "active" : "" %>">
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
                      <a href="${pageContext.request.contextPath}/roles"
                         class="<%= (uri.contains("roles")) ? "active" : "" %>">
                          <span>Role</span>
                      </a>
                  </li>
            </c:if>

            <!-- Order (salesman) -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role.name eq 'Salesman'
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_ORDER')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/salesman/orders"
                         class="<%= (uri.contains("order")) ? "active" : "" %>">
                          <span>Orders</span>
                      </a>
                  </li>
            </c:if>
            <!-- Order (warehouse) -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role.name eq 'Warehouse'
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_ORDER')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/warehouse/orders"
                         class="<%= (uri.contains("order")) ? "active" : "" %>">
                          <span>Orders</span>
                      </a>
                  </li>
            </c:if>

            <!-- Coupons (manager) -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role.name eq 'Manager'
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_COUPON')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/coupons"
                         class="<%= (uri.contains("coupons")) ? "active" : "" %>">
                          <span>Coupons</span>
                      </a>
                  </li>
            </c:if>

            <!-- Request Password Reset -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_USER')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/admin/password-reset"
                         class="<%= (uri.contains("admin/password-reset")) ? "active" : "" %>">
                          <span>Password Reset Request</span>
                      </a>
                  </li>
            </c:if>


            <!-- Products -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_USER')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/products"
                         class="<%= (uri.contains("products")) ? "active" : "" %>">
                          <span>Products</span>
                      </a>
                  </li>
            </c:if>


            <%--import product--%>
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'IMPORT_PRODUCT')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/inventory/import"
                         class="<%= (uri.contains("inventory/import")) ? "active" : "" %>">
                          <span>Import Product</span>
                      </a>
                  </li>
            </c:if>
            <%--            Export product--%>
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'EXPORT_PRODUCT')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/inventory/export"
                         class="<%= (uri.contains("inventory/export")) ? "active" : "" %>">
                          <span>Export Product</span>
                      </a>
                  </li>
            </c:if>
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_PURCHASE_REQUEST')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/purchase-request/list"
                         class="<%= (uri.contains("roles")) ? "active" : "" %>">
                          <span>Purchase Request</span>
                      </a>
                  </li>
            </c:if>

            <%--            import history--%>
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'IMPORT_PRODUCT')}">
                <li>
                    <a href="${pageContext.request.contextPath}/inventory/import/history"
                       class="<%= (uri.contains("inventory/import/history")) ? "active" : "" %>">
                        <span>Import History</span>
                    </a>
                </li>
            </c:if>

            <%--  out of stock--%>
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'IMPORT_PRODUCT')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/inventory/alert"
                         class="<%= (uri.contains("out-of-stock-alert")) ? "active" : "" %>">
                          <span>Out of stock alert</span>
                      </a>
                  </li>
            </c:if>

            <!-- Audit -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'READ_USER')}">
                  <li>
                      <a href="${pageContext.request.contextPath}/inventory-audits"
                         class="<%= (uri.contains("inventory-audits")) ? "active" : "" %>">
                          <span>Inventory Audit</span>
                      </a>
                  </li>
            </c:if>
        </ul>
    </aside>
</html>