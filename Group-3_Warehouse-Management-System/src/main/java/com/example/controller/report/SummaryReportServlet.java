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
import java.util.List;

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

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || user.getRole() == null
                || !"Manager".equalsIgnoreCase(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Only manager can manage report pages.");
            return;
        }

        // ===== Parse month =====
        String monthStr = req.getParameter("month");
        LocalDate fromDate = null;
        LocalDate toDate = null;

        try {
            if (monthStr != null && !monthStr.isEmpty()) {
                // monthStr format "yyyy-MM"
                fromDate = LocalDate.parse(monthStr + "-01"); // đầu tháng
                toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth()); // cuối tháng
            }
        } catch (Exception e) {
            req.setAttribute("error", "Invalid month format!");
        }

        // 🔥 GỌI DAO
        List<SummaryReportDTO> reportList = summaryReportDAO.getInventoryReport(fromDate, toDate);

        // ===== SET ATTRIBUTE =====
        req.setAttribute("reportList", reportList);
        req.setAttribute("month", monthStr); // giữ giá trị tháng đã chọn

        // ===== FORWARD =====
        req.getRequestDispatcher("/WEB-INF/manager/summary-report.jsp")
                .forward(req, resp);
    }
}
