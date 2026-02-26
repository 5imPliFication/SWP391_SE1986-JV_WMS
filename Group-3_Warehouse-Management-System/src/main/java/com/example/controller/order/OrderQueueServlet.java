package com.example.controller.order;

import com.example.model.Order;
import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/warehouse/orders")
public class OrderQueueServlet extends HttpServlet {

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

        if (!"Warehouse".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Get page number (default to 1)
            int pageNo = 1;
            String pageParam = req.getParameter("pageNo");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    pageNo = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    pageNo = 1;
                }
            }

            // Items per page
            int pageSize = 10;

            // Get filters
            String status = req.getParameter("status");
            String searchCode = req.getParameter("searchCode");

            // Get total count
            OrderService orderService = new OrderService();
            int totalOrders = orderService.countOrders(status, searchCode);

            // Calculate total pages
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

            // Get paginated data
            int offset = (pageNo - 1) * pageSize;
            List<Order> orders = orderService.getOrders(status, searchCode, offset, pageSize);

            // Set attributes
            req.setAttribute("orders", orders);
            req.setAttribute("pageNo", pageNo);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalOrders", totalOrders);

            // Get statistics
            Map<String, Integer> stats = orderService.getOrderStatistics();

            // Set attributes
            req.setAttribute("submittedCount", stats.getOrDefault("SUBMITTED", 0));
            req.setAttribute("processingCount", stats.getOrDefault("PROCESSING", 0));
            req.setAttribute("completedCount", stats.getOrDefault("COMPLETED", 0));
            req.setAttribute("cancelledCount", stats.getOrDefault("CANCELLED", 0));

            req.getRequestDispatcher("/WEB-INF/warehouse/order-queue.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load orders: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}

