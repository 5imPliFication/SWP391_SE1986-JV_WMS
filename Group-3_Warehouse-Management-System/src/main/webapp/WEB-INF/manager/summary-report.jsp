<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
      <!DOCTYPE html>
      <html>

      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Summary Report - Warehouse Management</title>
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

          .filter-card .form-group {
            margin-bottom: 0;
          }

          .filter-card label {
            font-size: 13px;
            font-weight: 600;
            color: #555;
            margin-bottom: 4px;
          }

          .filter-card input[type="date"] {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 8px 12px;
            font-size: 14px;
          }

          .filter-card .btn-filter {
            background: #3b82f6;
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 8px 24px;
            font-weight: 600;
            font-size: 14px;
            transition: background 0.2s;
          }

          .filter-card .btn-filter:hover {
            background: #2563eb;
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
          }

          .stat-card {
            background: #fff;
            border-radius: 16px;
            padding: 28px 24px;
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
            transition: transform 0.2s, box-shadow 0.2s;
            border: 1px solid #f0f0f0;
          }

          .stat-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.10);
          }

          .stat-icon {
            width: 56px;
            height: 56px;
            border-radius: 14px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            margin-bottom: 16px;
          }

          .stat-icon.import-icon {
            background: #e8f5e9;
            color: #2e7d32;
          }

          .stat-icon.export-icon {
            background: #fff3e0;
            color: #e65100;
          }

          .stat-icon.stock-icon {
            background: #e3f2fd;
            color: #1565c0;
          }

          .stat-label {
            font-size: 14px;
            color: #888;
            font-weight: 500;
            margin-bottom: 8px;
          }

          .stat-value {
            font-size: 36px;
            font-weight: 700;
            color: #1a1a2e;
            line-height: 1;
          }
        </style>
      </head>

      <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp" />
        <main class="main-content">
          <jsp:include page="/WEB-INF/common/header.jsp" />

          <div class="container">
            <%-- Page Header --%>
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="font-weight-bold text-dark">
                  <i class="fas fa-chart-bar mr-2"></i>Summary Report
                </h2>
              </div>

              <%-- Date Filter --%>
                <div class="filter-card">
                  <form method="get" action="${pageContext.request.contextPath}/summary_report">
                    <div class="row align-items-end">
                      <div class="col-md-4">
                        <div class="form-group">
                          <label for="fromDate"><i class="fas fa-calendar-alt mr-1"></i>From Date</label>
                          <input type="date" class="form-control" id="fromDate" name="fromDate" value="${fromDate}">
                        </div>
                      </div>
                      <div class="col-md-4">
                        <div class="form-group">
                          <label for="toDate"><i class="fas fa-calendar-alt mr-1"></i>To Date</label>
                          <input type="date" class="form-control" id="toDate" name="toDate" value="${toDate}">
                        </div>
                      </div>
                      <div class="col-md-4">
                        <div class="d-flex" style="gap: 8px;">
                          <button type="submit" class="btn btn-filter">
                            <i class="fas fa-search mr-1"></i>Filter
                          </button>
                          <a href="${pageContext.request.contextPath}/summary_report" class="btn btn-reset">
                            <i class="fas fa-redo mr-1"></i>Reset
                          </a>
                        </div>
                      </div>
                    </div>
                  </form>
                </div>

                <%-- Stat Cards Row --%>
                  <div class="row">
                    <%-- Import Card --%>
                      <div class="col-md-4 mb-3">
                        <div class="stat-card">
                          <div class="stat-icon import-icon">
                            <i class="fas fa-arrow-circle-down"></i>
                          </div>
                          <div class="stat-label">Total Imports</div>
                          <div class="stat-value">
                            <fmt:formatNumber value="${report.totalImport}" pattern="#,##0" />
                          </div>
                        </div>
                      </div>

                      <%-- Export Card --%>
                        <div class="col-md-4 mb-3">
                          <div class="stat-card">
                            <div class="stat-icon export-icon">
                              <i class="fas fa-arrow-circle-up"></i>
                            </div>
                            <div class="stat-label">Total Exports</div>
                            <div class="stat-value">
                              <fmt:formatNumber value="${report.totalExport}" pattern="#,##0" />
                            </div>
                          </div>
                        </div>

                        <%-- In Stock Card --%>
                          <div class="col-md-4 mb-3">
                            <div class="stat-card">
                              <div class="stat-icon stock-icon">
                                <i class="fas fa-warehouse"></i>
                              </div>
                              <div class="stat-label">Current In Stock</div>
                              <div class="stat-value">
                                <fmt:formatNumber value="${report.totalInStock}" pattern="#,##0" />
                              </div>
                            </div>
                          </div>
                  </div>
          </div>

          <script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
        </main>
      </body>

      </html>