package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.*;
import com.example.util.UserConstant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    protected LocalDateTime getLocalDateTime(ResultSet rs, String column)
            throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts == null ? null : ts.toLocalDateTime();
    }

    public List<Product> getAll(String searchName, String brandName,
            String categoryName, Boolean isActive, int pageNo) {

        List<Product> products = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT p.id, p.name, p.description, p.img_url, p.is_active,
                   b.name AS brand_name,
                   c.name AS category_name,
                   p.created_at, p.updated_at
            FROM products p
            JOIN brands b ON p.brand_id = b.id
            JOIN categories c ON p.category_id = c.id
            WHERE 1=1
        """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND p.name LIKE ? ");
        }
        if (brandName != null && !brandName.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append(" AND c.name = ? ");
        }
        if (isActive != null) {
            sql.append(" AND p.is_active = ? ");
        }

        sql.append(" ORDER BY p.created_at DESC ");
        sql.append(" LIMIT ? OFFSET ? ");

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (brandName != null && !brandName.trim().isEmpty()) {
                ps.setString(index++, brandName);
            }
            if (categoryName != null && !categoryName.trim().isEmpty()) {
                ps.setString(index++, categoryName);
            }
            if (isActive != null) {
                ps.setBoolean(index++, isActive);
            }

            ps.setInt(index++, UserConstant.PAGE_SIZE);
            ps.setInt(index++, (pageNo - 1) * UserConstant.PAGE_SIZE);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setImgUrl(rs.getString("img_url"));
                product.setIsActive(rs.getBoolean("is_active"));

                // ✅ SAFE timestamp
                product.setCreatedAt(getLocalDateTime(rs, "created_at"));
                product.setUpdatedAt(getLocalDateTime(rs, "updated_at"));

                Brand brand = new Brand();
                brand.setName(rs.getString("brand_name"));

                Category category = new Category();
                category.setName(rs.getString("category_name"));

                product.setBrand(brand);
                product.setCategory(category);

                products.add(product);
            }

            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countProducts(String searchName, String brandName,
            String categoryName, Boolean isActive) {

        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM products p
            JOIN brands b ON p.brand_id = b.id
            JOIN categories c ON p.category_id = c.id
            WHERE 1=1
        """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND p.name LIKE ? ");
        }
        if (brandName != null && !brandName.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append(" AND c.name = ? ");
        }
        if (isActive != null) {
            sql.append(" AND p.is_active = ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (brandName != null && !brandName.trim().isEmpty()) {
                ps.setString(index++, brandName);
            }
            if (categoryName != null && !categoryName.trim().isEmpty()) {
                ps.setString(index++, categoryName);
            }
            if (isActive != null) {
                ps.setBoolean(index++, isActive);
            }

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Create new product
    public boolean create(Product product) {
        String sql = """
                INSERT INTO products (name, description, img_url, brand_id, category_id, is_active, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, 1, NOW(), NOW())
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImgUrl());
            ps.setLong(4, product.getBrand().getId());
            ps.setLong(5, product.getCategory().getId());

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product findById(long productId) {
        String sql = """
                SELECT p.id as product_id, p.name as product_name, p.description, p.img_url, p.is_active ,
                       b.id as brand_id, b.name as brand_name,
                       c.id as category_id, c.name as category_name
                FROM products p
                JOIN brands b on p.brand_id = b.id
                JOIN categories c on p.category_id = c.id
                WHERE p.id = ?;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            Product product = null;

            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = new Product();

                product.setId(rs.getLong("product_id"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setImgUrl(rs.getString("img_url"));
                product.setIsActive(rs.getBoolean("is_active"));

                Brand brand = new Brand();
                brand.setId(rs.getLong("brand_id"));
                brand.setName(rs.getString("brand_name"));
                product.setBrand(brand);

                Category category = new Category();
                category.setId(rs.getLong("category_id"));
                category.setName(rs.getString("category_name"));
                product.setCategory(category);
            }

            return product;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Product product) {
        String sql = """
                UPDATE products
                SET name = ?,
                    description = ?,
                    img_url = ?,
                    brand_id = ?,
                    category_id = ?,
                    is_active = ?,
                    updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImgUrl());
            ps.setLong(4, product.getBrand().getId());
            ps.setLong(5, product.getCategory().getId());
            ps.setBoolean(6, product.getIsActive());
            ps.setLong(7, product.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
