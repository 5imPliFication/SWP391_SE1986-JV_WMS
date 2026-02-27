package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    protected LocalDateTime getLocalDateTime(ResultSet rs, String column)
            throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts == null ? null : ts.toLocalDateTime();
    }

    // Using for select box
    public List<Category> getAllActive() {
        List<Category> categories = new ArrayList<>();
        String sql = """
                SELECT *
                FROM categories
                WHERE is_active = 1
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                categories.add(category);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting active categories", e);
        }
        return categories;
    }

    public void createCategory(Category category) {
        String sql = "INSERT INTO categories (name, description, is_active) VALUES (?, ?, ?)";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getIsActive());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error creating category", e);
        }
    }

    public void updateCategory(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ?, is_active = ? WHERE id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getIsActive());
            ps.setLong(4, category.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }
    }

    public Category findById(long id) {

        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));


                category.setCreatedAt(getLocalDateTime(rs, "created_at"));
                category.setUpdatedAt(getLocalDateTime(rs, "updated_at"));

                return category;
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category getCategoryById(Long id) {
        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getLong("id"));
                    category.setName(rs.getString("name"));
                    category.setDescription(rs.getString("description"));
                    category.setIsActive(rs.getInt("is_active"));
                    return category;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting category by id", e);
        }
        return null;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getLong("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));
                category.setIsActive(rs.getInt("is_active"));
                categories.add(category);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all categories", e);
        }
        return categories;
    }
}
