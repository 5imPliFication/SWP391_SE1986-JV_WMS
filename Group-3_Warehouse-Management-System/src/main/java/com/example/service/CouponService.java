package com.example.service;

import com.example.dao.CouponDAO;
import com.example.model.Coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class CouponService {

    private final CouponDAO couponDAO;

    public CouponService() {
        this.couponDAO = new CouponDAO();
    }

    /**
     * Validate coupon for an order
     */
    public Optional<Coupon> validateCoupon(String code, BigDecimal orderTotal) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }

        if (orderTotal == null || orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.empty();
        }

        Optional<Coupon> couponOpt = couponDAO.findByCode(code.toUpperCase());

        if (couponOpt.isEmpty()) {
            return Optional.empty();
        }

        Coupon coupon = couponOpt.get();

        // Check if coupon is valid
        if (!coupon.isValid()) {
            return Optional.empty();
        }

        // Check minimum order amount
        if (coupon.getMinOrderAmount() != null &&
                orderTotal.compareTo(coupon.getMinOrderAmount()) < 0) {
            return Optional.empty();
        }

        return Optional.of(coupon);
    }


    /**
     * Apply coupon to order (increment usage count)
     */
    public void applyCoupon(Long couponId) {
        couponDAO.incrementUsageCount(couponId);
    }

    /**
     * Get all active coupons
     */
    public List<Coupon> getAllActiveCoupons() {
        return couponDAO.findAllActive();
    }

    /**
     * Get all coupons (for manager)
     */
    public List<Coupon> getAllCoupons() {
        return couponDAO.findAll();
    }

    /**
     * Create new coupon
     */
    public Long createCoupon(Coupon coupon) {
        // Validate coupon data
        if (coupon.getCode() == null || coupon.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code is required");
        }

        if (coupon.getDiscountValue() == null ||
                coupon.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount value must be greater than 0");
        }

        if ("PERCENTAGE".equals(coupon.getDiscountType()) &&
                coupon.getDiscountValue().compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Percentage discount cannot exceed 100%");
        }

        return couponDAO.create(coupon);
    }

    /**
     * Update coupon
     */
    public void updateCoupon(Coupon coupon) {
        if (coupon.getId() == null) {
            throw new IllegalArgumentException("Coupon ID is required for update");
        }

        couponDAO.update(coupon);
    }

    /**
     * Toggle coupon active status
     */
    public void toggleCouponStatus(Long couponId, boolean isActive) {
        couponDAO.toggleActive(couponId, isActive);
    }

    /**
     * Get coupon by ID
     */
    public Coupon getCouponById(Long id) {
        return couponDAO.findById(id);
    }

}