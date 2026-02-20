package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Coupon;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CouponDAO {

    public Coupon findById(Long id) {
        String sql = "SELECT * FROM coupons WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToCoupon(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find coupon by ID", e);
        }
        return null;
    }

    public Optional<Coupon> findByCode(String code) {
        String sql = """
                    SELECT * FROM coupons 
                    WHERE code = ? 
                    AND is_active = true 
                    AND (valid_from IS NULL OR valid_from <= NOW())
                    AND (valid_until IS NULL OR valid_until >= NOW())
                """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, code.toUpperCase());
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

    public List<Coupon> findAll() {
        String sql = "SELECT * FROM coupons ORDER BY created_at DESC";

        List<Coupon> coupons = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                coupons.add(mapResultSetToCoupon(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all coupons", e);
        }

        return coupons;
    }

    public Long create(Coupon coupon) {
        String sql = """
                    INSERT INTO coupons (code, description, discount_type, discount_value, 
                                        min_order_amount, valid_from, valid_until, usage_limit, is_active)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, coupon.getCode().toUpperCase());
            ps.setString(2, coupon.getDescription());
            ps.setString(3, coupon.getDiscountType());
            ps.setBigDecimal(4, coupon.getDiscountValue());
            ps.setBigDecimal(5, coupon.getMinOrderAmount() != null ?
                    coupon.getMinOrderAmount() : BigDecimal.ZERO);
            ps.setTimestamp(6, coupon.getValidFrom() != null ?
                    new Timestamp(coupon.getValidFrom().getTime()) : new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(7, coupon.getValidUntil() != null ?
                    new Timestamp(coupon.getValidUntil().getTime()) : null);

            if (coupon.getUsageLimit() != null) {
                ps.setInt(8, coupon.getUsageLimit());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setBoolean(9, coupon.getIsActive() != null ? coupon.getIsActive() : true);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create coupon", e);
        }

        return null;
    }

    public void update(Coupon coupon) {
        String sql = "UPDATE coupons SET " +
                "code = ?, " +
                "description = ?, " +
                "discount_type = ?, " +
                "discount_value = ?, " +
                "min_order_amount = ?, " +
                "valid_until = ?, " +
                "usage_limit = ? " +
                "WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, coupon.getCode());
            ps.setString(2, coupon.getDescription());
            ps.setString(3, coupon.getDiscountType());
            ps.setBigDecimal(4, coupon.getDiscountValue());
            ps.setBigDecimal(5, coupon.getMinOrderAmount());
            ps.setTimestamp(6, coupon.getValidUntil());

            if (coupon.getUsageLimit() != null) {
                ps.setInt(7, coupon.getUsageLimit());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            ps.setLong(8, coupon.getId());

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Coupon not found with ID: " + coupon.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update coupon", e);
        }
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

    public void toggleActive(Long couponId, boolean isActive) {
        String sql = "UPDATE coupons SET is_active = ? WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setLong(2, couponId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to toggle coupon status", e);
        }
    }

    private Coupon mapResultSetToCoupon(ResultSet rs) throws SQLException {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getLong("id"));
        coupon.setCode(rs.getString("code"));
        coupon.setDescription(rs.getString("description"));
        coupon.setDiscountType(rs.getString("discount_type"));
        coupon.setDiscountValue(rs.getBigDecimal("discount_value"));
        coupon.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
        coupon.setValidFrom(rs.getTimestamp("valid_from"));
        coupon.setValidUntil(rs.getTimestamp("valid_until"));

        int usageLimit = rs.getInt("usage_limit");
        coupon.setUsageLimit(rs.wasNull() ? null : usageLimit);

        coupon.setUsedCount(rs.getInt("used_count"));
        coupon.setIsActive(rs.getBoolean("is_active"));
        coupon.setCreatedAt(rs.getTimestamp("created_at"));
        coupon.setUpdatedAt(rs.getTimestamp("updated_at"));

        return coupon;
    }
}