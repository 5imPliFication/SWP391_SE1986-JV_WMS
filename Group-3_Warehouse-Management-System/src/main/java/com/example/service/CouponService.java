package com.example.service;

import com.example.dao.CouponDAO;
import com.example.model.Coupon;

import java.util.List;
import java.util.Optional;

public class CouponService {

    private final CouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = new CouponDAO();
    }

    public Optional<Coupon> validateCoupon(String code, Double orderTotal) {
        Optional<Coupon> couponOpt = couponDAO.findByCode(code);

        if (couponOpt.isEmpty()) {
            return Optional.empty();
        }

        Coupon coupon = couponOpt.get();

        // Check minimum order amount
        if (coupon.getMinOrderAmount() != null && orderTotal < coupon.getMinOrderAmount()) {
            return Optional.empty();
        }

        // Check usage limit
        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            return Optional.empty();
        }

        return Optional.of(coupon);
    }

    public Double calculateDiscount(Coupon coupon, Double orderTotal) {
        if (coupon == null) {
            return 0.0;
        }

        if ("PERCENTAGE".equals(coupon.getDiscountType())) {
            return orderTotal * (coupon.getDiscountValue() / 100);
        } else if ("FIXED".equals(coupon.getDiscountType())) {
            return Math.min(coupon.getDiscountValue(), orderTotal);
        }

        return 0.0;
    }

    public List<Coupon> getAllActiveCoupons() {
        return couponDAO.findAllActive();
    }
}