package com.example.controller.dashboard;

import com.example.dao.UserDAO;
import com.example.model.Order;
import com.example.model.User;
import com.example.service.OrderService;
import com.example.service.ProductService;
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
import java.util.Map;

@WebServlet("/manager-dashboard")
public class ManagerDashboardServlet extends HttpServlet {

    private OrderService orderService;
    private ProductService productService;
    private UserDAO userDAO;

    @Override
    public void init() {
        orderService = new OrderService();
        productService = new ProductService();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"Manager".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Calculate dates
            LocalDate today = LocalDate.now();
            LocalDate weekStart = today.minusDays(7);

            Timestamp now = Timestamp.from(java.time.Instant.now());
            Timestamp todayDate = Timestamp.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Timestamp weekStartDate = Timestamp.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Order statistics
            int ordersToday = orderService.countOrdersByDateRange(todayDate, now);
            int ordersThisWeek = orderService.countOrdersByDateRange(weekStartDate, now);

            // Order status counts
            Map<String, Integer> statusCounts = orderService.getOrderStatistics();

            // Product statistics
            int totalProducts = productService.countAllProducts();
            int lowStockCount = productService.countLowStockProducts(10); // threshold: 10 units
            int salesmenCount = userDAO.countUsersByRole("Salesman");
            int warehouseCount = userDAO.countUsersByRole("Warehouse");

            // Recent orders (last 10)
            List<Order> recentOrders = orderService.getRecentOrders(10);

            // Set attributes
            req.setAttribute("ordersToday", ordersToday);
            req.setAttribute("ordersThisWeek", ordersThisWeek);
            req.setAttribute("totalProducts", totalProducts);
            req.setAttribute("lowStockCount", lowStockCount);
            req.setAttribute("salesmenCount", salesmenCount);
            req.setAttribute("warehouseCount", warehouseCount);

            req.setAttribute("submittedOrders", statusCounts.getOrDefault("SUBMITTED", 0));
            req.setAttribute("processingOrders", statusCounts.getOrDefault("PROCESSING", 0));
            req.setAttribute("completedOrders", statusCounts.getOrDefault("COMPLETED", 0));
            req.setAttribute("cancelledOrders", statusCounts.getOrDefault("CANCELLED", 0));

            req.setAttribute("recentOrders", recentOrders);

            req.getRequestDispatcher("/WEB-INF/views/home/manager-dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}