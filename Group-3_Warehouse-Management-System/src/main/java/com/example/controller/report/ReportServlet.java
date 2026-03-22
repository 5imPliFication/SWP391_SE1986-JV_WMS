package com.example.controller.report;

import com.example.model.User;
import com.example.dto.ExportProductOrderDTO;
import com.example.dto.InventoryMovementRowDTO;
import com.example.dto.MonthSummaryDTO;
import com.example.dto.ReportItemDTO;
import com.example.service.ReportService;
import com.example.util.AppConstants;
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
        if (user == null || user.getRole() == null || !"Manager".equalsIgnoreCase(user.getRole().getName())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only manager can manage report pages.");
            return;
        }

        // get data from request parameters
        String type = request.getParameter("type");

        // default: inventory report because this is the primary manager view
        if (type == null || type.isEmpty()) {
            type = "inventory";
        }

        // Use for total compare chart
        String yearStr = request.getParameter("year");
        int year = (yearStr != null && !yearStr.isEmpty())
                ? Integer.parseInt(yearStr)
                : LocalDate.now().getYear();
        List<MonthSummaryDTO> summary = reportService.getMonthSummary(year);
        String chartSummaryData = gson.toJson(summary);
        request.setAttribute("chartSummaryData", chartSummaryData);

        // handle different report types
        if (type.equals("import")) {
            handleImportReport(request, response);
        } else if (type.equals("export")) {
            handleExportReport(request, response);
        } else if (type.equals("inventory")) {
            handleInventoryReport(request, response);
        } else {
            handleInventoryReport(request, response);
        }
    }

    private void handleInventoryReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");

        int year = (yearStr != null && !yearStr.isEmpty()) ? Integer.parseInt(yearStr) : LocalDate.now().getYear();
        int month = (monthStr != null && !monthStr.isEmpty()) ? Integer.parseInt(monthStr) : LocalDate.now().getMonthValue();

        List<ReportItemDTO> reportItems;
        List<InventoryMovementRowDTO> movementRows;
        List<Long> chartData;

        LocalDate fromDate;
        LocalDate toDate;
        if (fromDateStr != null && !fromDateStr.isBlank() && toDateStr != null && !toDateStr.isBlank()) {
            fromDate = LocalDate.parse(fromDateStr);
            toDate = LocalDate.parse(toDateStr);
        } else {
            fromDate = LocalDate.of(year, month, 1);
            toDate = fromDate.plusMonths(1).minusDays(1);
        }

        try {
            reportItems = reportService.getInventoryItems(month, year);
            movementRows = reportService.getInventoryMovementRows(fromDate, toDate);
            chartData = reportService.getInventoryChartDataByYear(year);
        } catch (Exception e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
            return;
        }

        String jsonData = gson.toJson(chartData);

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("movementRows", movementRows);
        request.setAttribute("chartData", jsonData);
        request.setAttribute("type", "inventory");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("fromDate", fromDate.toString());
        request.setAttribute("toDate", toDate.toString());
        request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
    }

    private void handleExportReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");
        String productIdStr = request.getParameter("productId");
        String productPageStr = request.getParameter("productPage");

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        int productPageNo = AppConstants.DEFAULT_PAGE_NO;

        if (yearStr != null && !yearStr.isEmpty()) {
            year = Integer.parseInt(yearStr);
        }
        if (monthStr != null && !monthStr.isEmpty()) {
            month = Integer.parseInt(monthStr);
        }
        if (productPageStr != null && !productPageStr.isEmpty()) {
            productPageNo = Integer.parseInt(productPageStr);
            if (productPageNo < AppConstants.DEFAULT_PAGE_NO) {
                productPageNo = AppConstants.DEFAULT_PAGE_NO;
            }
        }

        List<ReportItemDTO> reportItems;
        List<Long> chartData;
        List<ExportProductOrderDTO> productOrders = null;

        try {
            reportItems = reportService.getExportItems(month, year);
            chartData = reportService.getExportChartDataByYear(year);

            if (productIdStr != null && !productIdStr.isBlank()) {
                long productId = Long.parseLong(productIdStr);
                int totalProductOrders = reportService.countExportOrdersByProduct(productId, month, year);
                int totalProductOrderPages = (int) Math.ceil((double) totalProductOrders / AppConstants.PAGE_SIZE);
                if (totalProductOrderPages == 0) {
                    totalProductOrderPages = 1;
                }
                if (productPageNo > totalProductOrderPages) {
                    productPageNo = totalProductOrderPages;
                }

                productOrders = reportService.getExportOrdersByProduct(productId, month, year, productPageNo, AppConstants.PAGE_SIZE);
                request.setAttribute("selectedProductId", productId);
                request.setAttribute("productPageNo", productPageNo);
                request.setAttribute("totalProductOrderPages", totalProductOrderPages);
                request.setAttribute("productOrderPageSize", AppConstants.PAGE_SIZE);

                for (ReportItemDTO item : reportItems) {
                    if (item.getProductId() != null && item.getProductId().equals(productId)) {
                        request.setAttribute("selectedProductName", item.getProductName());
                        break;
                    }
                }

                if (request.getAttribute("selectedProductName") == null) {
                    request.setAttribute("selectedProductName", "Selected Product");
                }
            }
        } catch (Exception e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
            return;
        }

        String chartDataJson = gson.toJson(chartData);

        request.setAttribute("type", "export");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("reportItems", reportItems);
        request.setAttribute("chartData", chartDataJson);
        request.setAttribute("productOrders", productOrders);

        request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
    }

    private void handleImportReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");

        int year = (yearStr != null && !yearStr.isEmpty()) ? Integer.parseInt(yearStr) : LocalDate.now().getYear();
        int month = (monthStr != null && !monthStr.isEmpty()) ? Integer.parseInt(monthStr) : LocalDate.now().getMonthValue();

        // get list items to display in table
        List<ReportItemDTO> reportItems;
        List<Long> chartData;
        try {
            reportItems = reportService.getItems(month, year);
            chartData = reportService.getImportChartDataByYear(year);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
            return;
        }

        // convert sang JSON
        String jsonData = gson.toJson(chartData);

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("chartData", jsonData);
        request.setAttribute("type", "import");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
    }
}

