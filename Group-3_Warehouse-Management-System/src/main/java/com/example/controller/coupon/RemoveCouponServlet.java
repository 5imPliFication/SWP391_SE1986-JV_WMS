package com.example.controller.coupon;

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

@WebServlet("/salesman/order/remove-coupon")
public class RemoveCouponServlet extends HttpServlet {

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

            // Remove coupon from order (with user ID for usage tracking cleanup)
            orderService.removeCouponFromOrder(orderId, user.getId());

            String successMsg = URLEncoder.encode("Coupon removed successfully!", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&success=" + successMsg);

        } catch (IllegalArgumentException e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);

        } catch (Exception e) {
            req.setAttribute("error", "An error occurred while removing the coupon");
            req.setAttribute("code", "500");
            try {
                req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            } catch (Exception ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}
