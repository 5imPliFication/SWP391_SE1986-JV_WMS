package com.example.controller.order;

import com.example.model.Order;
import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import  jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/salesman/orders")
public class MyOrdersServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // ===== PAGINATION SETUP =====

            // 1. Get page number (default to 1)
            int pageNo = 1;
            String pageParam = req.getParameter("pageNo");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    pageNo = Integer.parseInt(pageParam);
                    if (pageNo < 1) pageNo = 1;
                } catch (NumberFormatException e) {
                    pageNo = 1;
                }
            }

            // 2. Items per page
            int pageSize = 10;

            // 3. Get filters (optional)
            String status = req.getParameter("status");
            String searchCode = req.getParameter("searchCode");

            // Get sorting parameters
            String sortBy = req.getParameter("sortBy");
            String sortDir = req.getParameter("sortDir");
            
            // Default sorting: newest first
            if (sortBy == null || sortBy.isEmpty()) {
                sortBy = "createdAt";
            }
            if (sortDir == null || sortDir.isEmpty()) {
                sortDir = "desc";
            }

            // 4. Get total count for this salesman
            int totalOrders = orderService.countOrdersBySalesman(user.getId(), status, searchCode);

            // 5. Calculate total pages
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
            if (totalPages == 0) totalPages = 1; // At least 1 page

            // 6. Calculate offset
            int offset = (pageNo - 1) * pageSize;

            // 7. Get paginated orders for this salesman
            List<Order> orders = orderService.getOrdersBySalesman(user.getId(), status, searchCode, sortBy, sortDir, offset, pageSize);

            // ===== SET ATTRIBUTES =====
            req.setAttribute("orders", orders);
            req.setAttribute("pageNo", pageNo);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalOrders", totalOrders);
            req.setAttribute("pageSize", pageSize);
            req.setAttribute("sortBy", sortBy);
            req.setAttribute("sortDir", sortDir);

            // Forward to JSP
            req.getRequestDispatcher("/WEB-INF/salesman/orders.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load orders: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}
