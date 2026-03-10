package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StorageDAO {

    public List<Storage> getAllActive() {
        List<Storage> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM storages
                WHERE is_active = true
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Storage storage = new Storage();
                storage.setId(rs.getLong("id"));
                storage.setSize(rs.getString("size"));

                list.add(storage);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Storage> getAll() {
        List<Storage> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM storages
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Storage storage = new Storage();
                storage.setId(rs.getLong("id"));
                storage.setSize(rs.getString("size"));

                list.add(storage);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
