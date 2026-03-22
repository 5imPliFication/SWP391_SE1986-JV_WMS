package com.example.controller.order;

import com.example.model.User;
import com.example.service.ActivityLogService;
import com.example.service.OrderItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/salesman/order/item/update-quantity")
public class UpdateOrderItemQuantityServlet extends HttpServlet {

    private OrderItemService orderItemService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        orderItemService = new OrderItemService();
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

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            Long itemId = Long.parseLong(req.getParameter("itemId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            String itemPage = req.getParameter("itemPage");
            String productPage = req.getParameter("productPage");

            // Validate quantity
            if (quantity < 1) {
                String errorMsg = URLEncoder.encode("Quantity must be at least 1", StandardCharsets.UTF_8);
                String redirectUrl = req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg;
                if (itemPage != null && !itemPage.isBlank()) {
                    redirectUrl += "&itemPage=" + URLEncoder.encode(itemPage, StandardCharsets.UTF_8);
                }
                if (productPage != null && !productPage.isBlank()) {
                    redirectUrl += "&productPage=" + URLEncoder.encode(productPage, StandardCharsets.UTF_8);
                }
                resp.sendRedirect(redirectUrl);
                return;
            }
            activityLogService.log(user, "Update order");

            // Update quantity and refresh order totals
            orderItemService.updateQuantity(orderId, itemId, quantity);

            String successMsg = URLEncoder.encode("Quantity updated successfully", StandardCharsets.UTF_8);
            String redirectUrl = req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&success=" + successMsg;
            if (itemPage != null && !itemPage.isBlank()) {
                redirectUrl += "&itemPage=" + URLEncoder.encode(itemPage, StandardCharsets.UTF_8);
            }
            if (productPage != null && !productPage.isBlank()) {
                redirectUrl += "&productPage=" + URLEncoder.encode(productPage, StandardCharsets.UTF_8);
            }
            resp.sendRedirect(redirectUrl);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
        } catch (Exception e) {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            String itemPage = req.getParameter("itemPage");
            String productPage = req.getParameter("productPage");
            String errorMsg = URLEncoder.encode("Failed to update quantity: " + e.getMessage(), StandardCharsets.UTF_8);
            String redirectUrl = req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg;
            if (itemPage != null && !itemPage.isBlank()) {
                redirectUrl += "&itemPage=" + URLEncoder.encode(itemPage, StandardCharsets.UTF_8);
            }
            if (productPage != null && !productPage.isBlank()) {
                redirectUrl += "&productPage=" + URLEncoder.encode(productPage, StandardCharsets.UTF_8);
            }
            resp.sendRedirect(redirectUrl);
        }
    }
}
