package com.example.controller.dashboard;

import com.example.model.Order;
import com.example.model.Product;
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

@WebServlet("/warehouse/dashboard")
public class WarehouseDashboardServlet extends HttpServlet {

    private OrderService orderService;
    private ProductService productService;

    @Override
    public void init() {
        orderService = new OrderService();
        productService = new ProductService();
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
            // Order statistics
            Map<String, Integer> statusCounts = orderService.getOrderStatistics();

            int submittedCount = statusCounts.getOrDefault("SUBMITTED", 0);
            int processingCount = statusCounts.getOrDefault("PROCESSING", 0);

            // Today's completed orders
            LocalDate today = LocalDate.now();
            Timestamp now = Timestamp.from(java.time.Instant.now());
            Timestamp todayDate = Timestamp.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            int completedToday = orderService.countOrdersByStatusAndDate("COMPLETED", todayDate, now);

            // Pending orders (SUBMITTED status) - limit to 10 for dashboard
            List<Order> allPendingOrders = orderService.getOrdersByStatus("SUBMITTED");
            int maxDashboardOrders = 10;
            boolean hasMorePending = allPendingOrders.size() > maxDashboardOrders;
            List<Order> pendingOrders = allPendingOrders.size() > maxDashboardOrders 
                    ? allPendingOrders.subList(0, maxDashboardOrders) 
                    : allPendingOrders;

            // Orders currently processing - limit to 10 for dashboard
            List<Order> allProcessingOrders = orderService.getOrdersByStatus("PROCESSING");
            boolean hasMoreProcessing = allProcessingOrders.size() > maxDashboardOrders;
            List<Order> processingOrders = allProcessingOrders.size() > maxDashboardOrders
                    ? allProcessingOrders.subList(0, maxDashboardOrders)
                    : allProcessingOrders;

            // Low stock products (threshold: 10 units)
            List<Product> lowStockProducts = productService.getLowStockProducts(10);
            int lowStockCount = lowStockProducts.size();

            // Set attributes
            req.setAttribute("submittedCount", submittedCount);
            req.setAttribute("processingCount", processingCount);
            req.setAttribute("completedToday", completedToday);
            req.setAttribute("lowStockCount", lowStockCount);

            req.setAttribute("pendingOrders", pendingOrders);
            req.setAttribute("hasMorePending", hasMorePending);
            req.setAttribute("processingOrders", processingOrders);
            req.setAttribute("hasMoreProcessing", hasMoreProcessing);
            req.setAttribute("lowStockProducts", lowStockProducts);

            req.getRequestDispatcher("/WEB-INF/views/home/warehouse-dashboard.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}