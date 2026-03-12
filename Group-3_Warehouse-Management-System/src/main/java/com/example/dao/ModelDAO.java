package com.example.dao;

import com.example.config.DBConfig;
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
                model.setBrandName(rs.getString("brand_name"));

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
                model.setBrandName(rs.getString("brand_name"));

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
                model.setBrandName(rs.getString("brand_name"));

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
                model.setBrandName(rs.getString("brand_name"));
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

}
