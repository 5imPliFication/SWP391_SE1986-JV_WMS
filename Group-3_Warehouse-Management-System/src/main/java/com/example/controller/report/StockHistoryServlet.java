package com.example.controller.report;

import com.example.dao.StockMovementDAO;
import com.example.enums.MovementType;
import com.example.enums.ReferenceType;
import com.example.model.StockMovement;

import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/stock-history")
public class StockHistoryServlet extends HttpServlet {

    private final StockMovementDAO dao = new StockMovementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");
        String typeParam = request.getParameter("type");
        String staffName = request.getParameter("staffName");
        String productName = request.getParameter("productName");
        String pageParam = request.getParameter("page");

        // First load or reset: no parameters at all
        boolean isInitialLoad = (yearParam == null && monthParam == null && typeParam == null && staffName == null && productName == null);
        if (isInitialLoad) {
            LocalDate now = LocalDate.now();
            yearParam = String.valueOf(now.getYear());
            monthParam = String.valueOf(now.getMonthValue());
        }

        // Normalize parameters
        if (yearParam == null) yearParam = "";
        if (monthParam == null) monthParam = "";

        LocalDate fromDate = null;
        LocalDate toDate = null;

        if (!yearParam.isEmpty() && !yearParam.equals("all")) {
            int year = Integer.parseInt(yearParam);
            if (!monthParam.isEmpty() && !monthParam.equals("all")) {
                int month = Integer.parseInt(monthParam);
                fromDate = LocalDate.of(year, month, 1);
                toDate = fromDate.withDayOfMonth(fromDate.lengthOfMonth());
            } else {
                // All months for this year
                fromDate = LocalDate.of(year, 1, 1);
                toDate = LocalDate.of(year, 12, 31);
                monthParam = "all"; // Ensure consistency for UI
            }
        } else {
            // All time
            yearParam = "all";
            monthParam = "all";
        }

        MovementType type = (typeParam != null && !typeParam.isEmpty())
                ? MovementType.valueOf(typeParam)
                : null;

        int page = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int limit = AppConstants.PAGE_SIZE;
        int offset = (page - 1) * limit;
        int totalCount = dao.getTotalCount(fromDate, toDate, type, null, staffName, productName);
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        List<StockMovement> list =
                dao.getStockHistory(fromDate, toDate, type, null, staffName, productName, limit, offset);

        request.setAttribute("list", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        // Pass current year for the year dropdown
        request.setAttribute("currentYear", LocalDate.now().getYear());
        request.setAttribute("year", yearParam);
        request.setAttribute("month", monthParam);
        request.setAttribute("type", typeParam);
        request.setAttribute("staffName", staffName);
        request.setAttribute("productName", productName);

        request.getRequestDispatcher("/WEB-INF/manager/stock-history.jsp")
                .forward(request, response);
    }
}