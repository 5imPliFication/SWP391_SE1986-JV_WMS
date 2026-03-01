package com.example.controller.coupon;

import com.example.model.Coupon;
import com.example.model.User;
import com.example.service.CouponService;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/salesman/order/apply-coupon")
public class ApplyCouponServlet extends HttpServlet {

    private CouponService couponService;
    private OrderService orderService;

    @Override
    public void init() {
        couponService = new CouponService();
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
            String couponCode = req.getParameter("couponCode");

            if (couponCode == null || couponCode.trim().isEmpty()) {
                String errorMsg = URLEncoder.encode("Please enter a coupon code", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);
                return;
            }

            // Calculate order total
            BigDecimal orderTotal = orderService.calculateOrderTotal(orderId);
            System.out.println(orderTotal);

            if (orderTotal == null || orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
                String errorMsg = URLEncoder.encode("Cannot apply coupon to empty order", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);
                return;
            }

            // Validate coupon (with per-user check)
            Optional<Coupon> couponOpt = couponService.validateCoupon(couponCode, orderTotal, user.getId());

            if (couponOpt.isEmpty()) {
                String errorMsg = URLEncoder.encode("Invalid, expired, inapplicable, or already used coupon code", StandardCharsets.UTF_8);
                resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);
                return;
            }

            Coupon coupon = couponOpt.get();

            // Apply coupon to order (with user ID)
            orderService.applyCouponToOrder(orderId, coupon.getId(), user.getId());

            String successMsg = URLEncoder.encode("Coupon applied successfully!", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&success=" + successMsg);

        } catch (IllegalArgumentException e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);

        } catch (IllegalStateException e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to apply coupon: " + e.getMessage());
            req.setAttribute("code", "500");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
        }
    }
}