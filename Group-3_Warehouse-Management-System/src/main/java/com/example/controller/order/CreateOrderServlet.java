package com.example.controller.order;

import com.example.model.Product;
import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.OrderService;
import com.example.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/salesman/order/create")
public class CreateOrderServlet extends HttpServlet {
    private OrderService orderService;
    private ProductService productService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        orderService = new OrderService();
        productService = new ProductService();
        activityLogService = new ActivityLogService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Salesman role required.");
            return;
        }

        List<Product> availableProducts = productService.findAvailableProductsForOrder(1, 1000);
        req.setAttribute("availableProducts", availableProducts);
        req.setAttribute("initialOrderItems", req.getParameter("orderItems"));
        req.getRequestDispatcher("/WEB-INF/salesman/create-order.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String customerName = normalize(req.getParameter("customerName"));
        String customerPhone = normalize(req.getParameter("customerPhone"));
        String note = normalize(req.getParameter("note"));
        String orderItemsRaw = normalize(req.getParameter("orderItems"));

        if (customerName.isEmpty()) {
            customerName = "Walk-in Customer";
        }

        Map<Long, Integer> selectedItems;
        try {
            selectedItems = parseOrderItems(orderItemsRaw);
        } catch (IllegalArgumentException e) {
            redirectBackWithError(req, resp, "Invalid order items data", customerName, customerPhone, note, orderItemsRaw);
            return;
        }

        if (selectedItems.isEmpty()) {
            redirectBackWithError(req, resp, "Please add at least one product before creating order", customerName, customerPhone, note, orderItemsRaw);
            return;
        }

        int orderId = orderService.createDraftOrder(customerName, customerPhone, note, user.getId());

        for (Map.Entry<Long, Integer> entry : selectedItems.entrySet()) {
            String addError = orderService.addOrUpdateOrderItem((long) orderId, entry.getKey(), entry.getValue());
            if (addError != null && !addError.isBlank()) {
                try {
                    orderService.cancelOrder((long) orderId, user.getId());
                } catch (Exception ignored) {
                    // Best effort cleanup of half-created draft.
                }
                redirectBackWithError(req, resp, addError, customerName, customerPhone, note, orderItemsRaw);
                return;
            }
        }

        activityLogService.log(user, "Create order");
        String success = URLEncoder.encode("Order created successfully", StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&success=" + success);
    }

    private void redirectBackWithError(HttpServletRequest req,
            HttpServletResponse resp,
            String error,
            String customerName,
            String customerPhone,
            String note,
            String orderItemsRaw) throws IOException {
        String url = req.getContextPath() + "/salesman/order/create"
                + "?error=" + encode(error)
                + "&customerName=" + encode(customerName)
                + "&customerPhone=" + encode(customerPhone)
                + "&note=" + encode(note)
                + "&orderItems=" + encode(orderItemsRaw);
        resp.sendRedirect(url);
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private Map<Long, Integer> parseOrderItems(String raw) {
        Map<Long, Integer> result = new LinkedHashMap<>();
        if (raw == null || raw.isBlank()) {
            return result;
        }

        String[] pairs = raw.split(";");
        for (String pair : pairs) {
            String trimmedPair = pair.trim();
            if (trimmedPair.isEmpty()) {
                continue;
            }

            String[] parts = trimmedPair.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid pair format");
            }

            long productId = Long.parseLong(parts[0].trim());
            int quantity = Integer.parseInt(parts[1].trim());
            if (productId <= 0 || quantity <= 0) {
                throw new IllegalArgumentException("Invalid values");
            }
            result.put(productId, quantity);
        }

        return result;
    }
}
