package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Ram;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RamDAO {

    public List<Ram> getAllActive() {
        List<Ram> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM rams
                WHERE is_active = true
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ram ram = new Ram();
                ram.setId(rs.getLong("id"));
                ram.setSize(rs.getString("size"));

                list.add(ram);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Ram> getAll() {
        List<Ram> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM rams
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ram ram = new Ram();
                ram.setId(rs.getLong("id"));
                ram.setSize(rs.getString("size"));

                list.add(ram);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
