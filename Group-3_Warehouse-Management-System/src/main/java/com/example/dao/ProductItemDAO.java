package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.ProductItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
