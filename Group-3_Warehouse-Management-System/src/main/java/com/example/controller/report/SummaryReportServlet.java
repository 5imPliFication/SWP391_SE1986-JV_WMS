package com.example.controller.report;

import com.example.dao.SummaryReportDAO;
import com.example.dto.SummaryReportDTO;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/summary_report")
public class SummaryReportServlet extends HttpServlet {

    private SummaryReportDAO summaryReportDAO;

    @Override
    public void init() {
        summaryReportDAO = new SummaryReportDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Role check: Manager only
        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !"Admin".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Parse optional date range filters
        String fromDateStr = req.getParameter("fromDate");
        String toDateStr = req.getParameter("toDate");

        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (fromDateStr != null && !fromDateStr.isEmpty()) {
            fromDate = LocalDate.parse(fromDateStr);
        }
        if (toDateStr != null && !toDateStr.isEmpty()) {
            toDate = LocalDate.parse(toDateStr);
        }

        SummaryReportDTO report = summaryReportDAO.getSummaryReport(fromDate, toDate);

        req.setAttribute("report", report);
        req.setAttribute("fromDate", fromDateStr);
        req.setAttribute("toDate", toDateStr);

        req.getRequestDispatcher("/WEB-INF/manager/summary-report.jsp").forward(req, resp);
    }
}
