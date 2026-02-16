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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/coupons")
public class CouponManagementServlet extends HttpServlet {

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

        List<Coupon> coupons = couponService.getAllCoupons();
        req.setAttribute("coupons", coupons);

        req.getRequestDispatcher("/WEB-INF/manager/coupons-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !"Manager".equals(user.getRole().getName())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");

        try {
            if ("create".equals(action)) {
                createCoupon(req);
                resp.sendRedirect(req.getContextPath() + "/coupons?success=true");
            } else if ("update".equals(action)) {
                updateCoupon(req);
                resp.sendRedirect(req.getContextPath() + "/coupons?success=true");
            } else if ("toggle".equals(action)) {
                toggleCoupon(req);
                resp.sendRedirect(req.getContextPath() + "/coupons?success=true");
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", e.getMessage());

            // Redirect back to appropriate page based on action
            if ("create".equals(action)) {
                resp.sendRedirect(req.getContextPath() + "/coupons/create?error=" +
                        URLEncoder.encode(e.getMessage(), "UTF-8"));
            } else if ("update".equals(action)) {
                Long couponId = Long.parseLong(req.getParameter("couponId"));
                resp.sendRedirect(req.getContextPath() + "/coupons/edit?id=" + couponId +
                        "&error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
            } else {
                resp.sendRedirect(req.getContextPath() + "/coupons?error=" +
                        URLEncoder.encode(e.getMessage(), "UTF-8"));
            }
        }
    }

    private void createCoupon(HttpServletRequest req) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Coupon coupon = Coupon.builder()
                .code(req.getParameter("code"))
                .description(req.getParameter("description"))
                .discountType(req.getParameter("discountType"))
                .discountValue(new BigDecimal(req.getParameter("discountValue")))
                .minOrderAmount(new BigDecimal(req.getParameter("minOrderAmount")))
                .validFrom(new Timestamp(System.currentTimeMillis()))
                .validUntil(req.getParameter("validUntil") != null && !req.getParameter("validUntil").isEmpty()
                        ? new Timestamp(sdf.parse(req.getParameter("validUntil")).getTime()) : null)
                .usageLimit(req.getParameter("usageLimit") != null && !req.getParameter("usageLimit").isEmpty()
                        ? Integer.parseInt(req.getParameter("usageLimit")) : null)
                .isActive(true)
                .build();

        couponService.createCoupon(coupon);
    }

    private void updateCoupon(HttpServletRequest req) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long couponId = Long.parseLong(req.getParameter("couponId"));

        // First, get the existing coupon to preserve fields not in the form
        Coupon existingCoupon = couponService.getCouponById(couponId);

        if (existingCoupon == null) {
            throw new IllegalArgumentException("Coupon not found");
        }

        // Update only the fields from the form, preserve the rest
        existingCoupon.setCode(req.getParameter("code"));
        existingCoupon.setDescription(req.getParameter("description"));
        existingCoupon.setDiscountType(req.getParameter("discountType"));
        existingCoupon.setDiscountValue(new BigDecimal(req.getParameter("discountValue")));
        existingCoupon.setMinOrderAmount(new BigDecimal(req.getParameter("minOrderAmount")));
        existingCoupon.setValidUntil(req.getParameter("validUntil") != null && !req.getParameter("validUntil").isEmpty()
                ? new Timestamp(sdf.parse(req.getParameter("validUntil")).getTime()) : null);
        existingCoupon.setUsageLimit(req.getParameter("usageLimit") != null && !req.getParameter("usageLimit").isEmpty()
                ? Integer.parseInt(req.getParameter("usageLimit")) : null);

        // Keep existing: validFrom, isActive, usedCount, createdAt, etc.

        couponService.updateCoupon(existingCoupon);
    }

    private void toggleCoupon(HttpServletRequest req) {
        Long couponId = Long.parseLong(req.getParameter("couponId"));
        boolean isActive = Boolean.parseBoolean(req.getParameter("isActive"));
        couponService.toggleCouponStatus(couponId, isActive);
    }
}