package com.example.controller.order;

import com.example.model.User;
import com.example.service.OrderService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/salesman/order/customer/update")
public class UpdateDraftOrderCustomerServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!"Salesman".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String itemPage = req.getParameter("itemPage");
        String productPage = req.getParameter("productPage");

        try {
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            String customerName = req.getParameter("customerName");
            String customerPhone = req.getParameter("customerPhone");
            String note = req.getParameter("note");

            orderService.updateDraftCustomerInfo(orderId, user.getId(), customerName, customerPhone, note);

            String success = URLEncoder.encode("Order information updated", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId
                    + "&itemPage=" + safePage(itemPage)
                    + "&productPage=" + safePage(productPage)
                    + "&success=" + success);
        } catch (Exception e) {
            String error = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            String orderId = req.getParameter("orderId");
            resp.sendRedirect(req.getContextPath() + "/salesman/order/detail?id=" + orderId
                    + "&itemPage=" + safePage(itemPage)
                    + "&productPage=" + safePage(productPage)
                    + "&error=" + error);
        }
    }

    private String safePage(String rawPage) {
        if (rawPage == null || rawPage.isBlank()) {
            return "1";
        }

        try {
            int page = Integer.parseInt(rawPage);
            return String.valueOf(Math.max(page, 1));
        } catch (NumberFormatException e) {
            return "1";
        }
    }
}
