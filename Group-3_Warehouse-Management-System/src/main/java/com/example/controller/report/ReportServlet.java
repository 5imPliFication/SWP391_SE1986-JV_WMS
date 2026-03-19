package com.example.controller.report;

import com.example.model.Order;
import com.example.model.User;
import com.example.service.OrderService;
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

    private void handleInventoryReport(HttpServletRequest request, HttpServletResponse response) {
    }

    private void handleExportReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String yearStr = request.getParameter("year");
        String quarterStr = request.getParameter("quarter");
        String searchCode = request.getParameter("searchCode");
        String pageStr = request.getParameter("pageNo");

        int year = LocalDate.now().getYear();
        int quarter = getCurrentQuarter();
        int pageNo = AppConstants.DEFAULT_PAGE_NO;

        if (yearStr != null && !yearStr.isEmpty()) {
            year = Integer.parseInt(yearStr);
        }
        if (quarterStr != null && !quarterStr.isEmpty()) {
            quarter = Integer.parseInt(quarterStr);
        }
        if (pageStr != null && !pageStr.isEmpty()) {
            pageNo = Integer.parseInt(pageStr);
        }

        try {
            reportService.validateQuarter(quarter);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            quarter = getCurrentQuarter();
        }

        int startMonth = (quarter - 1) * 3 + 1;
        LocalDate fromDate = LocalDate.of(year, startMonth, 1);
        LocalDate toDate = fromDate.plusMonths(3).minusDays(1);

        int offset = (pageNo - 1) * AppConstants.PAGE_SIZE;
        List<Order> orders = orderService.getExportHistoryOrders(searchCode, fromDate, toDate, offset);
        int totalOrders = orderService.countExportHistory(searchCode, fromDate, toDate);
        int totalPages = (int) Math.ceil((double) totalOrders / AppConstants.PAGE_SIZE);

        Map<String, List<Long>> annualOverview = reportService.getExportAnnualOverviewByYear(year);
        Map<String, Object> annualChartPayload = new LinkedHashMap<>();
        annualChartPayload.put("labels", List.of(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        ));
        annualChartPayload.put("inStock", annualOverview.get("inStock"));
        annualChartPayload.put("exported", annualOverview.get("exported"));
        annualChartPayload.put("imported", annualOverview.get("imported"));

        String annualChartJson = gson.toJson(annualChartPayload);
        String quarterTrendJson = gson.toJson(reportService.getExportQuarterTrend(year, quarter));

        request.setAttribute("type", "export");
        request.setAttribute("year", year);
        request.setAttribute("quarter", quarter);
        request.setAttribute("searchCode", searchCode);

        request.setAttribute("annualChartData", annualChartJson);
        request.setAttribute("quarterTrendData", quarterTrendJson);

        request.setAttribute("orders", orders);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageNo", pageNo);

        request.setAttribute("fromDate", fromDate.toString());
        request.setAttribute("toDate", toDate.toString());

        request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
    }

    private void handleImportReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");

        int month = -1;
        int year = -1;
        if(yearStr != null && !yearStr.isEmpty()) {
            year = Integer.parseInt(yearStr);
        } else {
            year = Integer.parseInt(String.valueOf(LocalDate.now().getYear()));
        }

        if(monthStr != null && !monthStr.isEmpty()) {
            month = Integer.parseInt(monthStr);
        } else {
            month = Integer.parseInt(String.valueOf(LocalDate.now().getMonthValue()));
        }

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
        request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
    }

    private int getCurrentQuarter() {
        int month = LocalDate.now().getMonthValue();
        return ((month - 1) / 3) + 1;
    }
}
