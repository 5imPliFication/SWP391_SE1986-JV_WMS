package com.example.controller.inventory;

import com.example.model.Order;
import com.example.service.OrderService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/inventory/export-history")
public class ExportHistoryServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchCode = request.getParameter("searchCode");
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");
        String pageStr = request.getParameter("pageNo");

        int pageNo = AppConstants.DEFAULT_PAGE_NO;
        try {
            if (pageStr != null && !pageStr.isEmpty()) {
                pageNo = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            pageNo = AppConstants.DEFAULT_PAGE_NO;
        }

        LocalDate fromDate = null;
        if (fromDateStr != null && !fromDateStr.isEmpty()) {
            fromDate = LocalDate.parse(fromDateStr);
        }

        LocalDate toDate = null;
        if (toDateStr != null && !toDateStr.isEmpty()) {
            toDate = LocalDate.parse(toDateStr);
        }

        int offset = (pageNo - 1) * AppConstants.PAGE_SIZE;
        List<Order> orders = orderService.getExportHistoryOrders(searchCode, fromDate, toDate, offset);
        int totalOrders = orderService.countExportHistory(searchCode, fromDate, toDate);
        int totalPages = (int) Math.ceil((double) totalOrders / AppConstants.PAGE_SIZE);

        request.setAttribute("orders", orders);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("searchCode", searchCode);
        request.setAttribute("fromDate", fromDateStr);
        request.setAttribute("toDate", toDateStr);

        request.getRequestDispatcher("/WEB-INF/inventory/export-history.jsp").forward(request, response);
    }
}
