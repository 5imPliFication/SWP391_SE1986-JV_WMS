package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Brand;
import com.example.model.Model;
import com.example.model.ProductItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelDAO {

    public List<Model> getAllActive() {
        List<Model> list = new ArrayList<>();

        String sql = """
            SELECT 
                m.id,
                m.name,
                m.is_active,
                m.brand_id,
                b.name AS brand_name
            FROM models m
            JOIN brands b ON m.brand_id = b.id
            WHERE m.is_active = true
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Model model = new Model();
                model.setId(rs.getLong("id"));
                model.setName(rs.getString("name"));
                model.setActive(rs.getBoolean("is_active"));

                Brand brand = new Brand();
                brand.setId(rs.getLong("brand_id"));
                brand.setName(rs.getString("brand_name"));
                model.setBrand(brand);

                list.add(model);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Model> getModelsByPage(int pageNo, int pageSize) {

        List<Model> list = new ArrayList<>();

        String sql = """
        SELECT 
            m.id,
            m.name,
            m.is_active,
            b.name AS brand_name
        FROM models m
        JOIN brands b ON m.brand_id = b.id
        ORDER BY m.id
        LIMIT ? OFFSET ?
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int offset = (pageNo - 1) * pageSize;

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Model model = new Model();
                model.setId(rs.getLong("id"));
                model.setName(rs.getString("name"));
                model.setActive(rs.getBoolean("is_active"));

                Brand brand = new Brand();
                brand.setName(rs.getString("brand_name"));
                model.setBrand(brand);
                list.add(model);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Model> getAll() {
        List<Model> list = new ArrayList<>();

        String sql = """
            SELECT 
                m.id,
                m.name,
                m.is_active,
                m.brand_id,
                b.name AS brand_name
            FROM models m
            JOIN brands b ON m.brand_id = b.id
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Model model = new Model();
                model.setId(rs.getLong("id"));
                model.setName(rs.getString("name"));
                model.setActive(rs.getBoolean("is_active"));

                Brand brand = new Brand();
                brand.setId(rs.getLong("brand_id"));
                brand.setName(rs.getString("brand_name"));
                model.setBrand(brand);

                list.add(model);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public Model getById(long modelId) {
        String sql = """
            SELECT 
                m.id,
                m.name,
                m.is_active,
                m.brand_id,
                b.name AS brand_name
            FROM models m
            JOIN brands b ON m.brand_id = b.id
            WHERE m.id = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            Model model = null;
            ps.setLong(1, modelId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                model = new Model();
                model.setId(rs.getLong("id"));
                model.setName(rs.getString("name"));
                model.setActive(rs.getBoolean("is_active"));

                Brand brand = new Brand();
                brand.setId(rs.getLong("brand_id"));
                brand.setName(rs.getString("brand_name"));
                model.setBrand(brand);
            }

            return model;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {

        String sql = "SELECT COUNT(*) FROM models";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void updateModelStatus(long id, boolean active) {

        String sql = "UPDATE models SET is_active = ? WHERE id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsByNameAndBrand(String name, long brandId) {
        String sql = "SELECT COUNT(*) FROM models WHERE brand_id = ? AND name = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, brandId);
            ps.setString(2, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean createModel(String name, long brandId, boolean active) {

        String sql = """
        INSERT INTO models (name, brand_id, is_active)
        VALUES (?, ?, ?)
    """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setLong(2, brandId);
            ps.setBoolean(3, active);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
