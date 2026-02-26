<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <!DOCTYPE html>
      <html>

      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Stock History - Warehouse Management</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <style>
          .filter-card {
            background: #fff;
            border-radius: 16px;
            padding: 20px 24px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
            border: 1px solid #f0f0f0;
            margin-bottom: 24px;
          }

          .filter-card label {
            font-size: 13px;
            font-weight: 600;
            color: #555;
            margin-bottom: 4px;
          }

          .filter-card input,
          .filter-card select {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 8px 12px;
            font-size: 14px;
          }

          .filter-card .btn-filter {
            background: #2563eb;
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 8px 24px;
            font-weight: 600;
            font-size: 14px;
            transition: background 0.2s;
          }

          .filter-card .btn-filter:hover {
            background: #1d4ed8;
            color: #fff;
          }

          .filter-card .btn-reset {
            background: #f1f5f9;
            color: #64748b;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            padding: 8px 20px;
            font-weight: 600;
            font-size: 14px;
            transition: background 0.2s;
          }

          .filter-card .btn-reset:hover {
            background: #e2e8f0;
            color: #64748b;
          }

          .table-card {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
            border: 1px solid #f0f0f0;
            overflow: hidden;
            margin-bottom: 24px;
          }

          .table th {
            border-top: none;
            border-bottom: 2px solid #f0f0f0;
            font-weight: 600;
            color: #444;
            background-color: #f8fafc;
            padding: 16px 20px;
          }

          .table td {
            padding: 16px 20px;
            vertical-align: middle;
            border-bottom: 1px solid #f0f0f0;
            color: #555;
          }

          .table tbody tr:hover {
            background-color: #f8fafc;
          }

          .badge-type {
            padding: 6px 12px;
            border-radius: 6px;
            font-weight: 600;
            font-size: 12px;
          }

          .type-import {
            background: #e8f5e9;
            color: #2e7d32;
          }

          .type-export {
            background: #fff3e0;
            color: #e65100;
          }

          .type-adjustment {
            background: #e3f2fd;
            color: #1565c0;
          }

          .type-return {
            background: #f3e5f5;
            color: #6a1b9a;
          }

          .pagination {
            margin: 0;
          }

          .page-link {
            border-radius: 6px;
            margin: 0 2px;
            border: 1px solid #e2e8f0;
            color: #2563eb;
          }

          .page-item.active .page-link {
            background-color: #2563eb;
            border-color: #2563eb;
          }

          .main-content {
            margin-left: 250px;
          }

          .header {
            height: 60px;
            background: #fff;
            border-bottom: 1px solid #e5e7eb;
            display: flex;
            align-items: center;
            padding: 0 24px;
            box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
          }
        </style>
      </head>

      <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp" />
        <main class="main-content">
          <jsp:include page="/WEB-INF/common/header.jsp" />

          <div class="container-fluid" style="padding: 24px;">
            <div class="d-flex justify-content-between align-items-center mb-4">
              <h2 class="font-weight-bold text-dark m-0">
                <i class="fas fa-history mr-2"></i>Stock History
              </h2>
            </div>

            <div class="filter-card">
              <form method="get" action="${pageContext.request.contextPath}/stock-history">
                <div class="row align-items-end">
                  <div class="col-md-2 mb-3 mb-md-0">
                    <div class="form-group mb-0">
                      <label for="fromDate"><i class="fas fa-calendar-alt mr-1"></i>From Date</label>
                      <input type="date" class="form-control" id="fromDate" name="fromDate" value="${fromDate}">
                    </div>
                  </div>
                  <div class="col-md-2 mb-3 mb-md-0">
                    <div class="form-group mb-0">
                      <label for="toDate"><i class="fas fa-calendar-alt mr-1"></i>To Date</label>
                      <input type="date" class="form-control" id="toDate" name="toDate" value="${toDate}">
                    </div>
                  </div>
                  <div class="col-md-3 mb-3 mb-md-0">
                    <div class="form-group mb-0">
                      <label for="type"><i class="fas fa-exchange-alt mr-1"></i>Movement Type</label>
                      <select class="form-control" id="type" name="type">
                        <option value="">All Types</option>
                        <option value="IMPORT" ${type=='IMPORT' ? 'selected' : '' }>IMPORT</option>
                        <option value="EXPORT" ${type=='EXPORT' ? 'selected' : '' }>EXPORT</option>
                        <option value="ADJUSTMENT" ${type=='ADJUSTMENT' ? 'selected' : '' }>ADJUSTMENT</option>
                        <option value="RETURN" ${type=='RETURN' ? 'selected' : '' }>RETURN</option>
                      </select>
                    </div>
                  </div>
                  <div class="col-md-3 mb-3 mb-md-0">
                    <div class="form-group mb-0">
                      <label for="referenceType"><i class="fas fa-file-alt mr-1"></i>Reference Type</label>
                      <select class="form-control" id="referenceType" name="referenceType">
                        <option value="">All References</option>
                        <option value="ORDER" ${referenceType=='ORDER' ? 'selected' : '' }>ORDER</option>
                        <option value="GOODS_RECEIPT" ${referenceType=='GOODS_RECEIPT' ? 'selected' : '' }>GOODS RECEIPT
                        </option>
                        <option value="AUDIT_ADJUSTMENT" ${referenceType=='AUDIT_ADJUSTMENT' ? 'selected' : '' }>AUDIT
                          ADJUSTMENT</option>
                      </select>
                    </div>
                  </div>
                  <div class="col-md-2">
                    <div class="d-flex" style="gap: 8px;">
                      <button type="submit" class="btn btn-filter flex-grow-1">
                        <i class="fas fa-search mr-1"></i>Filter
                      </button>
                      <a href="${pageContext.request.contextPath}/stock-history" class="btn btn-reset">
                        <i class="fas fa-redo"></i>
                      </a>
                    </div>
                  </div>
                </div>
              </form>
            </div>

            <div class="table-card">
              <div class="table-responsive">
                <table class="table mb-0">
                  <thead>
                    <tr>
                      <th>Movement ID</th>
                      <th>Product ID</th>
                      <th>Quantity</th>
                      <th>Type</th>
                      <th>Reference Type</th>
                      <th>Date & Time</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="item" items="${list}">
                      <tr>
                        <td><strong>#${item.id}</strong></td>
                        <td>PID-${item.productId}</td>
                        <td>
                          <c:choose>
                            <c:when test="${item.type == 'IMPORT' || item.type == 'RETURN'}">
                              <span class="text-success font-weight-bold"
                                style="font-size: 15px;">+${item.quantity}</span>
                            </c:when>
                            <c:when test="${item.type == 'EXPORT'}">
                              <span class="text-danger font-weight-bold"
                                style="font-size: 15px;">-${item.quantity}</span>
                            </c:when>
                            <c:otherwise>
                              <span class="text-primary font-weight-bold"
                                style="font-size: 15px;">${item.quantity}</span>
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${item.type == 'IMPORT'}"><span class="badge-type type-import"><i
                                  class="fas fa-arrow-down mr-1"></i>IMPORT</span></c:when>
                            <c:when test="${item.type == 'EXPORT'}"><span class="badge-type type-export"><i
                                  class="fas fa-arrow-up mr-1"></i>EXPORT</span></c:when>
                            <c:when test="${item.type == 'ADJUSTMENT'}"><span class="badge-type type-adjustment"><i
                                  class="fas fa-sliders-h mr-1"></i>ADJUSTMENT</span></c:when>
                            <c:when test="${item.type == 'RETURN'}"><span class="badge-type type-return"><i
                                  class="fas fa-undo mr-1"></i>RETURN</span></c:when>
                          </c:choose>
                        </td>
                        <td><span class="badge badge-light"
                            style="font-size: 13px; border: 1px solid #ddd; color: #555;">${item.referenceType}</span>
                        </td>
                        <td>${item.createdAt}</td>
                      </tr>
                    </c:forEach>
                    <c:if test="${empty list}">
                      <tr>
                        <td colspan="6" class="text-center py-5">
                          <div class="text-muted mb-2"><i class="fas fa-inbox fa-3x"></i></div>
                          <h5 class="text-secondary mt-3">No Stock History Found</h5>
                          <p class="text-muted">There are no movements matching your current filters.</p>
                        </td>
                      </tr>
                    </c:if>
                  </tbody>
                </table>
              </div>
            </div>

            <%-- Pagination --%>
              <c:if test="${totalPages > 1}">
                <div class="d-flex justify-content-between align-items-center mt-4 mb-5">
                  <div class="text-muted font-weight-bold" style="font-size: 14px;">
                    Page ${currentPage} of ${totalPages}
                  </div>
                  <nav aria-label="Page navigation">
                    <ul class="pagination mb-0">
                      <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link"
                          href="${pageContext.request.contextPath}/stock-history?page=${currentPage - 1}&fromDate=${fromDate != null ? fromDate : ''}&toDate=${toDate != null ? toDate : ''}&type=${type != null ? type : ''}&referenceType=${referenceType != null ? referenceType : ''}">
                          <i class="fas fa-chevron-left mr-1"></i>Previous
                        </a>
                      </li>

                      <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                          <c:when test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                              <a class="page-link"
                                href="${pageContext.request.contextPath}/stock-history?page=${i}&fromDate=${fromDate != null ? fromDate : ''}&toDate=${toDate != null ? toDate : ''}&type=${type != null ? type : ''}&referenceType=${referenceType != null ? referenceType : ''}">${i}</a>
                            </li>
                          </c:when>
                          <c:when test="${i == currentPage - 3 || i == currentPage + 3}">
                            <li class="page-item disabled"><span class="page-link">...</span></li>
                          </c:when>
                        </c:choose>
                      </c:forEach>

                      <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link"
                          href="${pageContext.request.contextPath}/stock-history?page=${currentPage + 1}&fromDate=${fromDate != null ? fromDate : ''}&toDate=${toDate != null ? toDate : ''}&type=${type != null ? type : ''}&referenceType=${referenceType != null ? referenceType : ''}">
                          Next<i class="fas fa-chevron-right ml-1"></i>
                        </a>
                      </li>
                    </ul>
                  </nav>
                </div>
              </c:if>

          </div>
          <script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
        </main>
      </body>

      </html>