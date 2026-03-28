package com.example.controller.report;

import com.example.dto.InventoryMovementRowDTO;
import com.example.dto.ExportProductOrderDTO;
import com.example.enums.MovementType;
import com.example.model.User;
import com.example.dto.MonthSummaryDTO;
import com.example.dto.ReportItemDTO;
import com.example.service.ReportService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private ReportService reportService;
    private Gson gson;

    @Override
    public void init() {
        reportService = new ReportService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || user.getRole() == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only manager can manage report pages.");
            return;
        }

        String rawType = request.getParameter("type");
        boolean isDefaultLanding = rawType == null || rawType.isBlank();
        String type = normalizeType(rawType);
        String inventoryFallbackView = isDefaultLanding ? "in-out-stock" : "inventory";

        // Use for total compare chart
        int year = parsePositiveInt(request.getParameter("year"), LocalDate.now().getYear());
        List<MonthSummaryDTO> summary = reportService.getMonthSummary(year);
        String chartSummaryData = gson.toJson(summary);
        request.setAttribute("chartSummaryData", chartSummaryData);

        if ("import".equals(type)) {
            handleImportReport(request, response);
        } else if ("export".equals(type)) {
            handleExportReport(request, response);
        } else {
            handleInventoryReport(request, response, inventoryFallbackView);
        }
    }

    private void handleInventoryReport(HttpServletRequest request, HttpServletResponse response, String fallbackView) throws ServletException, IOException {
        String requestedView = request.getParameter("view");
        String view = (requestedView == null || requestedView.isBlank())
                ? normalizeInventoryView(fallbackView)
                : normalizeInventoryView(requestedView);

        int year = parsePositiveInt(request.getParameter("year"), LocalDate.now().getYear());
        int month = parseMonth(request.getParameter("month"));

        LocalDate[] dateRange = resolveDateRange(
                year,
                month,
                request.getParameter("fromDate"),
                request.getParameter("toDate")
        );
        LocalDate fromDate = dateRange[0];
        LocalDate toDate = dateRange[1];

        List<ReportItemDTO> reportItems;
        List<InventoryMovementRowDTO> movementRows;
        List<Long> chartData;

        try {
            reportItems = reportService.getInventoryItems(month, year);
            movementRows = reportService.getInventoryMovementRows(fromDate, toDate);
            chartData = reportService.getInventoryChartDataByYear(year);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("type", "inventory");
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.setAttribute("view", view);
            request.getRequestDispatcher(resolveInventoryView(view)).forward(request, response);
            return;
        }

        String jsonData = gson.toJson(chartData);

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("movementRows", movementRows);
        request.setAttribute("chartData", jsonData);
        request.setAttribute("type", "inventory");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.setAttribute("view", view);
        request.getRequestDispatcher(resolveInventoryView(view)).forward(request, response);
    }

    private String resolveInventoryView(String view) {
        if ("in-out-stock".equalsIgnoreCase(view)) {
            return "/WEB-INF/report/in-out-stock-report.jsp";
        }
        return "/WEB-INF/report/inventory-report.jsp";
    }

    private void handleExportReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int year = parsePositiveInt(request.getParameter("year"), LocalDate.now().getYear());
        int month = parseMonth(request.getParameter("month"));
        LocalDate[] dateRange = resolveDateRange(
                year,
                month,
                request.getParameter("fromDate"),
                request.getParameter("toDate")
        );
        LocalDate fromDate = dateRange[0];
        LocalDate toDate = dateRange[1];

        List<ReportItemDTO> reportItems;
        List<Long> chartData;
        try {
            int pageNo = parsePositiveInt(request.getParameter("pageNo"), 1);
            int pageSize = com.example.util.AppConstants.PAGE_SIZE;
            int totalItems = reportService.countItems(month, year, MovementType.EXPORT);
            int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
            pageNo = Math.min(pageNo, totalPages);

            reportItems = reportService.getItems(month, year, MovementType.EXPORT, pageNo, pageSize);
            chartData = reportService.getChartDataByYear(year, MovementType.EXPORT);

            request.setAttribute("pageNo", pageNo);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);

            Long productId = parsePositiveLong(request.getParameter("productId"));
            if (productId != null) {
                int productPageNo = parsePositiveInt(request.getParameter("productPage"), 1);
                int productOrderPageSize = 10;

                int totalProductOrders = reportService.countExportOrdersByProduct(productId, month, year);
                int totalProductOrderPages = Math.max(1, (int) Math.ceil((double) totalProductOrders / productOrderPageSize));
                productPageNo = Math.min(productPageNo, totalProductOrderPages);

                List<ExportProductOrderDTO> productOrders = reportService.getExportOrdersByProduct(
                        productId, month, year, productPageNo, productOrderPageSize
                );

                String selectedProductName = reportItems.stream()
                        .filter(item -> productId.equals(item.getProductId()))
                        .map(ReportItemDTO::getProductName)
                        .findFirst()
                        .orElse("Selected Product");

                request.setAttribute("selectedProductId", productId);
                request.setAttribute("selectedProductName", selectedProductName);
                request.setAttribute("productOrders", productOrders);
                request.setAttribute("productPageNo", productPageNo);
                request.setAttribute("productOrderPageSize", productOrderPageSize);
                request.setAttribute("totalProductOrderPages", totalProductOrderPages);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("type", "export");
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.getRequestDispatcher("/WEB-INF/report/export-report.jsp").forward(request, response);
            return;
        }

        String jsonData = gson.toJson(chartData);

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("chartData", jsonData);
        request.setAttribute("type", "export");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.getRequestDispatcher("/WEB-INF/report/export-report.jsp").forward(request, response);
    }

    private void handleImportReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int year = parsePositiveInt(request.getParameter("year"), LocalDate.now().getYear());
        int month = parseMonth(request.getParameter("month"));
        LocalDate[] dateRange = resolveDateRange(
                year,
                month,
                request.getParameter("fromDate"),
                request.getParameter("toDate")
        );
        LocalDate fromDate = dateRange[0];
        LocalDate toDate = dateRange[1];

        // get list items to display in table
        List<ReportItemDTO> reportItems;
        List<Long> chartData;
        try {
            int pageNo = parsePositiveInt(request.getParameter("pageNo"), 1);
            int pageSize = com.example.util.AppConstants.PAGE_SIZE;
            int totalItems = reportService.countItems(month, year, MovementType.IMPORT);
            int totalPages = Math.max(1, (int) Math.ceil((double) totalItems / pageSize));
            pageNo = Math.min(pageNo, totalPages);

            reportItems = reportService.getItems(month, year, MovementType.IMPORT, pageNo, pageSize);
            chartData = reportService.getChartDataByYear(year, MovementType.IMPORT);

            request.setAttribute("pageNo", pageNo);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.setAttribute("type", "import");
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.getRequestDispatcher("/WEB-INF/report/import-report.jsp").forward(request, response);
            return;
        }

        // convert sang JSON
        String jsonData = gson.toJson(chartData);

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("chartData", jsonData);
        request.setAttribute("type", "import");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        request.getRequestDispatcher("/WEB-INF/report/import-report.jsp").forward(request, response);
    }

    private String normalizeType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return "inventory";
        }

        String type = rawType.trim().toLowerCase();
        if ("in-out-stock".equals(type)) {
            return "inventory";
        }

        if ("inventory".equals(type) || "import".equals(type) || "export".equals(type)) {
            return type;
        }
        return "inventory";
    }

    private String normalizeInventoryView(String rawView) {
        if ("in-out-stock".equalsIgnoreCase(rawView)) {
            return "in-out-stock";
        }
        return "inventory";
    }

    private int parseMonth(String rawMonth) {
        int parsedMonth = parsePositiveInt(rawMonth, LocalDate.now().getMonthValue());
        return Math.min(Math.max(parsedMonth, 1), 12);
    }

    private LocalDate parseDate(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(rawValue);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDate[] resolveDateRange(int year, int month, String fromDateRaw, String toDateRaw) {
        LocalDate fromDate = parseDate(fromDateRaw);
        LocalDate toDate = parseDate(toDateRaw);

        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            fromDate = LocalDate.of(year, month, 1);
            toDate = fromDate.plusMonths(1).minusDays(1);
        }

        return new LocalDate[]{fromDate, toDate};
    }

    private int parsePositiveInt(String rawValue, int defaultValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return defaultValue;
        }
        try {
            return Math.max(Integer.parseInt(rawValue), 1);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Long parsePositiveLong(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }
        try {
            long value = Long.parseLong(rawValue);
            return value > 0 ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
