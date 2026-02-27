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

@WebServlet("/salesman/order/item/remove")
public class RemoveOrderItemServlet extends HttpServlet {
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

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));

            // ✓ CORRECT: Get productItemId (not productId)
            Long productItemId = Long.parseLong(req.getParameter("productItemId"));

            // Remove item
            orderService.removeItem(orderId, productItemId);

            String successMsg = URLEncoder.encode("Item removed successfully", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&success=" + successMsg);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
        } catch (IllegalStateException | IllegalArgumentException e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to remove item: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
