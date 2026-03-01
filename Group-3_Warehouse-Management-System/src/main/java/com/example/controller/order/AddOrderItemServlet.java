package com.example.controller.order;

import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/salesman/order/item/add")
public class AddOrderItemServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            Long productId = Long.parseLong(req.getParameter("productId"));
            String quantityRaw = req.getParameter("quantity");

            int quantity;
            try {
                quantity = Integer.parseInt(quantityRaw);
            } catch (NumberFormatException e) {
                redirectWithQuantityError(req, resp, orderId, productId, quantityRaw,
                        "Quantity must be a valid number");
                return;
            }

            if (quantity <= 0) {
                redirectWithQuantityError(req, resp, orderId, productId, String.valueOf(quantity),
                        "Quantity must be greater than 0");
                return;
            }

            // Check if item already exists and update quantity instead
            String error = orderService.addOrUpdateOrderItem(orderId, productId, quantity);
            if (error != null && !error.isBlank()) {
                redirectWithQuantityError(req, resp, orderId, productId, String.valueOf(quantity), error);
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to add item: " + e.getMessage());
        }
    }

    private void redirectWithQuantityError(HttpServletRequest req,
                                           HttpServletResponse resp,
                                           Long orderId,
                                           Long productId,
                                           String quantity,
                                           String message) throws IOException {
        String redirectUrl = req.getContextPath() + "/salesman/order/detail?id=" + orderId
                + "&productItemId=" + productId
                + "&quantity=" + URLEncoder.encode(quantity == null ? "" : quantity, StandardCharsets.UTF_8)
                + "&quantityError=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(redirectUrl);
    }
}