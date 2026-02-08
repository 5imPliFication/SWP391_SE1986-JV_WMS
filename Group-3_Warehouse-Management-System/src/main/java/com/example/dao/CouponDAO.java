package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Coupon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CouponDAO {

    public Optional<Coupon> findById(Long id) {
        String sql = "SELECT * FROM coupons WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCoupon(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find coupon", e);
        }

        return Optional.empty();
    }

    public Optional<Coupon> findByCode(String code) {
        String sql = """
            SELECT * FROM coupons 
            WHERE code = ? 
            AND is_active = true 
            AND (valid_until IS NULL OR valid_until >= NOW())
        """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToCoupon(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find coupon by code", e);
        }

        return Optional.empty();
    }

    public List<Coupon> findAllActive() {
        String sql = """
            SELECT * FROM coupons 
            WHERE is_active = true 
            AND (valid_until IS NULL OR valid_until >= NOW())
            ORDER BY created_at DESC
        """;

        List<Coupon> coupons = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find active coupons", e);
        }

        return coupons;
    }

    public void incrementUsageCount(Long couponId) {
        String sql = "UPDATE coupons SET used_count = used_count + 1 WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, couponId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to increment coupon usage", e);
        }
    }

    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getLong("id"));
        coupon.setCode(rs.getString("code"));
        coupon.setDescription(rs.getString("description"));
        coupon.setDiscountType(rs.getString("discount_type"));
        coupon.setDiscountValue(rs.getDouble("discount_value"));
        coupon.setMinOrderAmount(rs.getDouble("min_order_amount"));
        coupon.setValidFrom(rs.getTimestamp("valid_from"));
        coupon.setValidUntil(rs.getTimestamp("valid_until"));
        coupon.setUsageLimit(rs.getInt("usage_limit"));
        coupon.setUsedCount(rs.getInt("used_count"));
        coupon.setIsActive(rs.getBoolean("is_active"));
        coupon.setCreatedAt(rs.getTimestamp("created_at"));
        coupon.setUpdatedAt(rs.getTimestamp("updated_at"));
        return coupon;
    }
}