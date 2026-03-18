package com.example.controller.report;

import com.example.dao.ReportDAO;
import com.example.dto.ReportItemDTO;
import com.example.service.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private ReportService reportService;

    @Override
    public void init() {
        reportService = new ReportService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get data from request parameters
        String type = request.getParameter("type");

        // default: import report for current month
        if (type == null || type.isEmpty()) {
            type = "import";
        }

        if (type.equals("import")) {
            handleImportReport(request, response);
        } else if (type.equals("export")) {
            handleExportReport(request, response);
        } else if (type.equals("inventory")) {
            handleInventoryReport(request, response);
        }
    }

    private void handleInventoryReport(HttpServletRequest request, HttpServletResponse response) {
    }

    private void handleExportReport(HttpServletRequest request, HttpServletResponse response) {
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
        try {
            reportItems = reportService.getItems(month, year);
        } catch (IllegalArgumentException e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("messageType", "danger");
            request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
            return;
        }

        request.setAttribute("reportItems", reportItems);
        request.setAttribute("type", "import");
        request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
    }
}
