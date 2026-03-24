package com.example.controller.order;

import com.example.model.User;
import com.example.service.ActivityLogService;
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
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        orderService = new OrderService();
        activityLogService = new ActivityLogService();

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
            String itemPage = req.getParameter("itemPage");
            String productPage = req.getParameter("productPage");
            String returnTo = req.getParameter("returnTo");

            int quantity;
            try {
                quantity = Integer.parseInt(quantityRaw);
            } catch (NumberFormatException e) {
                redirectWithQuantityError(req, resp, orderId, productId, quantityRaw,
                        "Quantity must be a valid number", itemPage, productPage, returnTo);
                return;
            }

            if (quantity <= 0) {
                redirectWithQuantityError(req, resp, orderId, productId, String.valueOf(quantity),
                        "Quantity must be greater than 0", itemPage, productPage, returnTo);
                return;
            }

            // Check if item already exists and update quantity instead
            String error = orderService.addOrUpdateOrderItem(orderId, productId, quantity);
            if (error != null && !error.isBlank()) {
                redirectWithQuantityError(req, resp, orderId, productId, String.valueOf(quantity), error, itemPage, productPage, returnTo);
                return;
            }
            activityLogService.log(user, "Add item into order");

            String redirectBase = "create".equalsIgnoreCase(returnTo)
                    ? "/salesman/order/create?id="
                    : "/salesman/order/detail?id=";
            String redirectUrl = req.getContextPath() + redirectBase + orderId;
            if (itemPage != null && !itemPage.isBlank()) {
                redirectUrl += "&itemPage=" + URLEncoder.encode(itemPage, StandardCharsets.UTF_8);
            }
            if (productPage != null && !productPage.isBlank()) {
                redirectUrl += "&productPage=" + URLEncoder.encode(productPage, StandardCharsets.UTF_8);
                redirectUrl += "&openProductModal=1";
            }
            resp.sendRedirect(redirectUrl);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to add item: " + e.getMessage());
        }
    }

    private void redirectWithQuantityError(HttpServletRequest req,
            HttpServletResponse resp,
            Long orderId,
            Long productId,
            String quantity,
            String message,
            String itemPage,
            String productPage,
            String returnTo) throws IOException {
        String redirectBase = "create".equalsIgnoreCase(returnTo)
                ? "/salesman/order/create?id="
                : "/salesman/order/detail?id=";
        String redirectUrl = req.getContextPath() + redirectBase + orderId
                + "&productItemId=" + productId
                + "&quantity=" + URLEncoder.encode(quantity == null ? "" : quantity, StandardCharsets.UTF_8)
                + "&quantityError=" + URLEncoder.encode(message, StandardCharsets.UTF_8);

        if (itemPage != null && !itemPage.isBlank()) {
            redirectUrl += "&itemPage=" + URLEncoder.encode(itemPage, StandardCharsets.UTF_8);
        }
        if (productPage != null && !productPage.isBlank()) {
            redirectUrl += "&productPage=" + URLEncoder.encode(productPage, StandardCharsets.UTF_8);
            redirectUrl += "&openProductModal=1";
        }

        resp.sendRedirect(redirectUrl);
    }
}
