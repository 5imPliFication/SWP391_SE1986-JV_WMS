package com.example.controller.order;

import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet("/order/cancel")
public class CancelOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        // Prefer DI / context lookup if you already use it
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Authentication (filter may already handle this)
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            String note = req.getParameter("note");

            orderService.cancelOrder(
                    orderId,
                    user.getId(),
                    note
            );

            // Redirect based on role (UX concern, not permission)
            String role = user.getRole().getName();
            if ("Salesman".equalsIgnoreCase(role)) {
                resp.sendRedirect(req.getContextPath()
                        + "/salesman/orders?cancelled=true");
            } else if ("Warehouse".equalsIgnoreCase(role)) {
                resp.sendRedirect(req.getContextPath()
                        + "/warehouse/orders?cancelled=true");
            } else {
                resp.sendRedirect(req.getContextPath()
                        + "/orders?cancelled=true");
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        } catch (SecurityException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (IllegalStateException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to cancel order: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}