package com.example.controller.order;

import com.example.model.User;
import com.example.service.ActivityLogService;
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
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        orderService = new OrderService();
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
            String itemPage = req.getParameter("itemPage");
            String productPage = req.getParameter("productPage");

            // Now get orderItemId instead of productItemId
            Long orderItemId = Long.parseLong(req.getParameter("orderItemId"));

            // Remove item
            orderService.removeItem(orderId, orderItemId);
            activityLogService.log(user, "Remove item from order");

            String successMsg = URLEncoder.encode("Item removed successfully", StandardCharsets.UTF_8);
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
        } catch (IllegalStateException | IllegalArgumentException e) {
            String errorMsg = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            Long orderId = Long.parseLong(req.getParameter("orderId"));
            String itemPage = req.getParameter("itemPage");
            String productPage = req.getParameter("productPage");
            String redirectUrl = req.getContextPath() + "/salesman/order/detail?id=" + orderId + "&error=" + errorMsg;
            if (itemPage != null && !itemPage.isBlank()) {
                redirectUrl += "&itemPage=" + URLEncoder.encode(itemPage, StandardCharsets.UTF_8);
            }
            if (productPage != null && !productPage.isBlank()) {
                redirectUrl += "&productPage=" + URLEncoder.encode(productPage, StandardCharsets.UTF_8);
            }
            resp.sendRedirect(redirectUrl);
        } catch (Exception e) {
            req.setAttribute("error", "Failed to remove item: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }
}
