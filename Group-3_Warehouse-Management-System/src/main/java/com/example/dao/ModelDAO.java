package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Model;

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

}
