package com.example.controller.order;

import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/salesman/order/submit")
public class SubmitOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

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

            // This will increment coupon usage if coupon is applied
            orderService.submitOrder(orderId, user.getId());

            resp.sendRedirect(req.getContextPath() + "/salesman/orders?submitted=true");

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
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
            req.setAttribute("error", "Failed to submit order: " + e.getMessage());
            req.setAttribute("code", "500");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}

