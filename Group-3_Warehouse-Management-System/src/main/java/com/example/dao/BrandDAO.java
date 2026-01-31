package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Brand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {

    public List<Brand> findAll() {
        List<Brand> list = new ArrayList<>();
        // Modified SQL to get both ID and Name, separated by colon
        String sql = "SELECT * from brand";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getLong("id"));
                brand.setName(rs.getString("name"));
                brand.setDescription(rs.getString("description"));
                brand.setActive(rs.getBoolean("is_active"));
                list.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addBrand(Brand brand) {
        String sql = "INSERT INTO brand (name, description, is_active) VALUES (?, ?, ?)";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, brand.getName());
            ps.setString(2, brand.getDescription());
            ps.setBoolean(3, brand.isActive());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBrandExist(String name) {
        String sql = "SELECT 1 FROM brand WHERE name = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Using for select box
    public List<Brand> getAllActive() {
        List<Brand> brands = new ArrayList<>();
        String sql = """
                    SELECT *
                    FROM brands
                    WHERE is_active = true
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Brand brand = new Brand();

                brand.setId(rs.getLong("id"));
                brand.setName(rs.getString("name"));

                // Add to list
                brands.add(brand);
            }
            return brands;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Brand findById(long brandId) {
        String sql = """
                    SELECT *
                    FROM brands
                    WHERE id = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            Brand brand = null;
            ps.setLong(1, brandId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                brand = new Brand();

                brand.setId(rs.getLong("id"));
                brand.setName(rs.getString("name"));
                brand.setDescription(rs.getString("description"));
                brand.setActive(rs.getBoolean("is_active"));
            }
            return brand;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
