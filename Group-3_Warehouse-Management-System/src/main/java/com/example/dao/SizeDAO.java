package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SizeDAO {

    public List<Size> getAllActive() {
        List<Size> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM sizes
                WHERE is_active = true
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Size size = new Size();
                size.setId(rs.getLong("id"));
                size.setSize(rs.getString("size"));

                list.add(size);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Size> getAll() {
        List<Size> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM sizes
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Size size = new Size();
                size.setId(rs.getLong("id"));
                size.setSize(rs.getString("size"));

                list.add(size);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
