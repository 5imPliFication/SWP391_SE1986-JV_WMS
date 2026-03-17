package com.example.controller.report;

import com.example.dao.ReportDAO;
import com.example.dto.ReportItemDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@WebServlet("/report")
public class ReportServlet extends HttpServlet {

    private ReportDAO reportDAO;

    @Override
    public void init() throws ServletException {
        reportDAO = new ReportDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");

        LocalDate now = LocalDate.now();

        if (type == null || type.isEmpty()) {
            type = "import";
        }
        
        int year = now.getYear();
        if (yearStr != null && !yearStr.isEmpty()) {
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        
        String yearMonth = "";
        if (monthStr != null && !monthStr.isEmpty()) {
            yearMonth = monthStr;
            try {
                YearMonth ym = YearMonth.parse(monthStr);
                year = ym.getYear();
            } catch (Exception e) {
                // ignore
            }
        } else {
            // Default to current month if no month is selected
            yearMonth = String.format("%04d-%02d", year, now.getMonthValue());
        }

        // To keep form values consistent
        request.setAttribute("type", type);
        request.setAttribute("year", year);
        request.setAttribute("month", yearMonth);

        // We only process "import" currently per requirements
        if ("import".equals(type)) {
            List<Long> chartData = reportDAO.getImportChartDataByYear(year);
            List<ReportItemDTO> reportItems = reportDAO.getImportReportByMonth(yearMonth);
            
            // simple JSON array serialization for chartData
            StringBuilder chartDataJSON = new StringBuilder("[");
            for (int i = 0; i < chartData.size(); i++) {
                chartDataJSON.append(chartData.get(i));
                if (i < chartData.size() - 1) {
                    chartDataJSON.append(", ");
                }
            }
            chartDataJSON.append("]");
            
            request.setAttribute("chartData", chartDataJSON.toString());
            request.setAttribute("reportItems", reportItems);
        }

        request.getRequestDispatcher("/WEB-INF/report/report.jsp").forward(request, response);
    }
}
