package com.example.controller.dashboard;

import com.example.model.Order;
import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@WebServlet("/salesman/dashboard")
public class SalesmanDashboardServlet extends HttpServlet {

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
            Long salesmanId = user.getId();

            // Calculate dates
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(7);

            Timestamp now = Timestamp.from(java.time.Instant.now());
            Timestamp todayDate = Timestamp.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Order counts for this salesman
            int ordersToday = orderService.countOrdersBySalesmanAndDate(salesmanId, todayDate, now);
            int draftOrders = orderService.countOrdersBySalesmanAndStatus(salesmanId, "DRAFT");
            int submittedOrders = orderService.countOrdersBySalesmanAndStatus(salesmanId, "SUBMITTED");
            int completedOrders = orderService.countOrdersBySalesmanAndStatus(salesmanId, "COMPLETED");

            // Today's orders (full list)
            List<Order> todayOrders = orderService.getOrdersBySalesmanAndDate(salesmanId, todayDate, now);

            // Recent orders (last 7 days, limit 10)
            Timestamp weekAgoDate = Timestamp.from(weekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
            List<Order> recentOrders = orderService.getRecentOrdersBySalesman(salesmanId, weekAgoDate, 10);

            // Set attributes
            req.setAttribute("ordersToday", ordersToday);
            req.setAttribute("draftOrders", draftOrders);
            req.setAttribute("submittedOrders", submittedOrders);
            req.setAttribute("completedOrders", completedOrders);
            req.setAttribute("todayOrders", todayOrders);
            req.setAttribute("recentOrders", recentOrders);

            req.getRequestDispatcher("/WEB-INF/views/home/salesman-dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}