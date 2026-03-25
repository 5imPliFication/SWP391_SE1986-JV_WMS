package com.example.controller.report;

import com.example.dto.InventoryMovementRowDTO;
import com.example.enums.MovementType;
import com.example.model.Order;
import com.example.model.User;
import com.example.service.OrderService;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private ReportService reportService;
    private OrderService orderService;
    private Gson gson;

    @Override
    public void init() {
        reportService = new ReportService();
        orderService = new OrderService();
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

        // default: import report for current month
        if (type == null || type.isEmpty()) {
            type = "import";
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
            handleImportReport(request, response);
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
            request.getRequestDispatcher("/WEB-INF/report/inventory-report.jsp").forward(request, response);
            return;
        }

        String jsonData = gson.toJson(chartData);

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("movementRows", movementRows);
        request.setAttribute("chartData", jsonData);
        request.setAttribute("type", "inventory");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.getRequestDispatcher("/WEB-INF/report/inventory-report.jsp").forward(request, response);
    }

    private void handleExportReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");

        // if year and month are not provided, default to current year and month
        int year = (yearStr != null && !yearStr.isEmpty()) ? Integer.parseInt(yearStr) : LocalDate.now().getYear();
        int month = (monthStr != null && !monthStr.isEmpty()) ? Integer.parseInt(monthStr) : LocalDate.now().getMonthValue();

        List<ReportItemDTO> reportItems;
        List<Long> chartData;
        try {
            reportItems = reportService.getItems(month, year, MovementType.EXPORT);
            chartData = reportService.getChartDataByYear(year, MovementType.EXPORT);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/WEB-INF/report/export-report.jsp").forward(request, response);
            return;
        }

        String jsonData = gson.toJson(chartData);

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("chartData", jsonData);
        request.setAttribute("type", "export");
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.getRequestDispatcher("/WEB-INF/report/export-report.jsp").forward(request, response);
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
            reportItems = reportService.getItems(month, year, MovementType.IMPORT);
            chartData = reportService.getChartDataByYear(year, MovementType.IMPORT);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
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
        request.getRequestDispatcher("/WEB-INF/report/import-report.jsp").forward(request, response);
    }

    private int getCurrentQuarter() {
        int month = LocalDate.now().getMonthValue();
        return ((month - 1) / 3) + 1;
    }
}

