package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Using for select box
    public List<Category> getAllActive() {
        List<Category> categories = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                    SELECT *
                    FROM categories
                    WHERE is_active = true
                """);

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();

                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));

                // Add to list
                categories.add(category);
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category findById(long categoryId) {
        String sql = """
                    SELECT *
                    FROM categories
                    WHERE id = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            Category category = null;
            ps.setLong(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                category = new Category();

                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setIsActive(rs.getBoolean("is_active"));
                category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                category.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            }
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
