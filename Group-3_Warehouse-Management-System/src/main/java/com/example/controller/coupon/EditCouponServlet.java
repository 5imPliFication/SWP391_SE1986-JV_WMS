package com.example.controller.coupon;

import com.example.model.Coupon;
import com.example.model.User;
import com.example.service.CouponService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/coupons/edit")
public class EditCouponServlet extends HttpServlet {

    private CouponService couponService;

    @Override
    public void init() {
        couponService = new CouponService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !"Manager".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long couponId = Long.parseLong(req.getParameter("id"));
            Coupon coupon = couponService.getCouponById(couponId);

            if (coupon == null) {
                resp.sendRedirect(req.getContextPath() + "/coupons?error=" +
                        URLEncoder.encode("Coupon not found", "UTF-8"));
                return;
            }

            req.setAttribute("coupon", coupon);
            req.getRequestDispatcher("/WEB-INF/manager/coupon-edit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/coupons?error=" +
                    URLEncoder.encode("Invalid coupon ID", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/coupons?error=" +
                    URLEncoder.encode("Failed to load coupon", "UTF-8"));
        }
    }
}
