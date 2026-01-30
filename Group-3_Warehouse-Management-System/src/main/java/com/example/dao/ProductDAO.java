package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.*;
import com.example.util.UserConstant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAll(String searchName, String brandName, String categoryName, int pageNo) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                    SELECT p.id, p.name, p.description, p.img_url, p.is_active, b.name as brand_name , c.name as category_name , p.created_at, p.updated_at
                    FROM products p
                    JOIN brands b on p.brand_id = b.id
                    JOIN categories c on p.category_id = c.id
                    WHERE 1=1
                """);

        // search by name
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND p.name LIKE ? ");
        }
        // filter by brand name
        if (brandName != null && !brandName.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }
        // filter by brand name
        if (brandName != null && !brandName.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }
        // filter by category name
        if (brandName != null && !brandName.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }
        // filter by date created
        sql.append(" ORDER BY p.created_at DESC ");

        // handle pagination
        sql.append(" limit ? offset ? ");

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

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
            // set value for pagination of SQL
            ps.setInt(index++, UserConstant.PAGE_SIZE);

            int offset = (pageNo - 1)* UserConstant.PAGE_SIZE;
            ps.setInt(index++, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setImgUrl(rs.getString("img_url"));
                product.setIsActive(rs.getBoolean("is_active"));
                product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

                Brand brand = new Brand();
                brand.setName(rs.getString("brand_name"));
                Category category = new Category();
                category.setName(rs.getString("category_name"));

                product.setBrand(brand);
                product.setCategory(category);

                // Add to list
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countProducts(String searchName, String brandName, String categoryName) {
        int totalProducts = 0;
        StringBuilder sql = new StringBuilder("""
                    SELECT count(*)
                    FROM products p
                    JOIN brands b on p.brand_id = b.id
                    JOIN categories c on p.category_id = c.id
                    WHERE 1=1
                """);

        // search by name
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND p.name LIKE ? ");
        }
        // filter by brand name
        if (brandName != null && !brandName.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }

        // filter by category name
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sql.append(" AND c.name = ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

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

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalProducts = rs.getInt(1);
            }
            return totalProducts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

