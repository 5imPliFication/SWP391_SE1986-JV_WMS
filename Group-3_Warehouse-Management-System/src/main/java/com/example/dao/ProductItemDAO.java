package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.ProductItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductItemDAO {

    public ProductItem findById(Long id) {
        String sql = "SELECT * FROM product_items WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductItem item = new ProductItem();
                item.setId(rs.getLong("id"));
                item.setProductId(rs.getLong("product_id"));
                item.setCurrentPrice(rs.getDouble("current_price"));
                item.setIsActive(rs.getBoolean("is_active"));
                return item;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find product item", e);
        }
    }
    /**
     * Find all product items for a given product
     */
    public List<ProductItem> findByProductId(Long productId) {
        String sql = "SELECT * FROM product_items WHERE product_id = ? ORDER BY id DESC";
        List<ProductItem> items = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductItem item = new ProductItem();
                item.setId(rs.getLong("id"));
                item.setSerial(rs.getString("serial"));
                item.setImportedPrice(rs.getDouble("imported_price"));
                item.setProductId(rs.getLong("product_id"));
                item.setCurrentPrice(rs.getDouble("current_price"));
                item.setIsActive(rs.getBoolean("is_active"));
                if (rs.getTimestamp("imported_at") != null) {
                    item.setImportedAt(rs.getTimestamp("imported_at").toLocalDateTime());
                }
                items.add(item);
            }

            return items;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find product items", e);
        }
    }

    public int countActiveByProductId(Long productId) {
        String sql = "SELECT COUNT(*) FROM product_items WHERE product_id = ? AND is_active = 1";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count active product items", e);
        }
    }

    public int deactivateAvailableItems(Long productId, int quantity) {
        if (quantity <= 0) {
            return 0;
        }

        String selectSql = "SELECT id FROM product_items WHERE product_id = ? AND is_active = 1 ORDER BY id LIMIT ?";
        String updateSql = "UPDATE product_items SET is_active = 0, updated_at = NOW() WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);
            try {
                List<Long> ids = new ArrayList<>();
                try (PreparedStatement selectPs = con.prepareStatement(selectSql)) {
                    selectPs.setLong(1, productId);
                    selectPs.setInt(2, quantity);
                    ResultSet rs = selectPs.executeQuery();
                    while (rs.next()) {
                        ids.add(rs.getLong("id"));
                    }
                }

                if (ids.size() < quantity) {
                    con.rollback();
                    return 0;
                }

                int updated = 0;
                try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                    for (Long id : ids) {
                        updatePs.setLong(1, id);
                        updated += updatePs.executeUpdate();
                    }
                }

                con.commit();
                return updated;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to deactivate product items", e);
        }
    }

    public int activateInactiveItems(Long productId, int quantity) {
        if (quantity <= 0) {
            return 0;
        }

        String selectSql = "SELECT id FROM product_items WHERE product_id = ? AND is_active = 0 ORDER BY id DESC LIMIT ?";
        String updateSql = "UPDATE product_items SET is_active = 1, updated_at = NOW() WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);
            try {
                List<Long> ids = new ArrayList<>();
                try (PreparedStatement selectPs = con.prepareStatement(selectSql)) {
                    selectPs.setLong(1, productId);
                    selectPs.setInt(2, quantity);
                    ResultSet rs = selectPs.executeQuery();
                    while (rs.next()) {
                        ids.add(rs.getLong("id"));
                    }
                }

                if (ids.size() < quantity) {
                    con.rollback();
                    return 0;
                }

                int updated = 0;
                try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                    for (Long id : ids) {
                        updatePs.setLong(1, id);
                        updated += updatePs.executeUpdate();
                    }
                }

                con.commit();
                return updated;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to activate product items", e);
        }
    }

}
