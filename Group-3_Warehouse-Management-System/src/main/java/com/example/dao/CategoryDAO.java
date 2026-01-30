package com.example.dao;


import com.example.config.DBConfig;
import com.example.model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CategoryDAO {


    public void createCategory(Category category) {
        String sql = "INSERT INTO categories (name, description, is_active) VALUES (?, ?, ?)";


        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {


            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isActive());
            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating category", e);
        }
    }
}
