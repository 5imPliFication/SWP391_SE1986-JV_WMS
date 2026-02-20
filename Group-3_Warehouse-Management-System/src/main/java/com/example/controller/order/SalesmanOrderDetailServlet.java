package com.example.controller.order;

import com.example.model.Coupon;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.User;
import com.example.service.CouponService;
import com.example.service.OrderItemService;
import com.example.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/salesman/order/detail")
public class SalesmanOrderDetailServlet extends HttpServlet {

    private OrderService orderService;
    private OrderItemService orderItemService;

    @Override
    public void init() {
        orderService = new OrderService();
        orderItemService = new OrderItemService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
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
            Long orderId = Long.parseLong(req.getParameter("id"));

            Order order = orderService.getOrderDetail(orderId, user.getId(), "Salesman");
            List<OrderItem> items = orderItemService.getItemsByOrder(orderId, user.getId(), "Salesman");

            // If order has a coupon, load it
            if (order.getCoupon() != null) {
                CouponService couponService = new CouponService();
                Coupon coupon = couponService.getCouponById(order.getCoupon().getId());
                order.setCoupon(coupon);
            }

            req.setAttribute("order", order);
            req.setAttribute("items", items);

            req.getRequestDispatcher("/WEB-INF/salesman/order-detail.jsp")
                    .forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to load order: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}



