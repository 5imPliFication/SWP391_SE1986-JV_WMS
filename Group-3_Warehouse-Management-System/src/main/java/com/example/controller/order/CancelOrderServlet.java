package com.example.controller.order;

import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Multiple URL mappings for different roles
@WebServlet({"/salesman/order/cancel", "/warehouse/order/cancel"})
public class CancelOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Authentication
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            String note = req.getParameter("note"); // Can be null for salesman

            String role = user.getRole().getName();

            // Role-based authorization
            if (!"Salesman".equalsIgnoreCase(role) && !"Warehouse".equalsIgnoreCase(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to cancel orders");
                return;
            }

            // Cancel the order
            if (note != null && !note.trim().isEmpty()) {
                // Warehouse cancellation with note
                orderService.cancelOrder(orderId, user.getId(), note);
            } else {
                // Salesman cancellation without note
                orderService.cancelOrder(orderId, user.getId());
            }

            // Redirect based on role
            if ("Salesman".equalsIgnoreCase(role)) {
                resp.sendRedirect(req.getContextPath() + "/salesman/orders?cancelled=true");
            } else if ("Warehouse".equalsIgnoreCase(role)) {
                resp.sendRedirect(req.getContextPath() + "/warehouse/orders?cancelled=true");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid order ID format");
            req.setAttribute("code", "400");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);

        } catch (SecurityException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("code", "403");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);

        } catch (IllegalStateException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("code", "409");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to cancel order: " + e.getMessage());
            req.setAttribute("code", "500");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}