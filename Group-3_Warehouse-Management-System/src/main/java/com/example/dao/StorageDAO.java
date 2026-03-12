package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Chip;
import com.example.model.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public List<Storage> getStoragesByPage(int pageNo, int pageSize) {

        List<Storage> list = new ArrayList<>();

        String sql = """
            SELECT id, size
            FROM storages
            ORDER BY id
            LIMIT ? OFFSET ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int offset = (pageNo - 1) * pageSize;

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

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

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public Storage getById(long storageId) {
        String sql = """
            SELECT *
            FROM storages c
            WHERE c.id = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            Storage storage = null;
            ps.setLong(1, storageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                storage = new Storage();
                storage.setId(rs.getLong("id"));
                storage.setSize(rs.getString("size"));
            }

            return storage;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {

        String sql = "SELECT COUNT(*) FROM storages";

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
