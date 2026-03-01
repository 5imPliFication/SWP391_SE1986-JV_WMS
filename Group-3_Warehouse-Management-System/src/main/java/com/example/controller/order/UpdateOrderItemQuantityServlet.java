package com.example.controller.order;

import com.example.model.User;
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

    @Override
    public void init() {
        orderItemService = new OrderItemService();
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

            // Validate quantity
            if (quantity < 1) {
                String errorMsg = URLEncoder.encode("Quantity must be at least 1", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);
                return;
            }

            // Update quantity
            orderItemService.updateQuantity(itemId, quantity);

            String successMsg = URLEncoder.encode("Quantity updated successfully", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&success=" + successMsg);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
        } catch (Exception e) {
            e.printStackTrace();
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            String errorMsg = URLEncoder.encode("Failed to update quantity: " + e.getMessage(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);
        }
    }
}