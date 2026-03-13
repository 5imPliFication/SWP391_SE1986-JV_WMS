package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Chip;
import com.example.model.Size;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public List<Size> getSizesByPage(int pageNo, int pageSize) {

        List<Size> list = new ArrayList<>();

        String sql = """
            SELECT id, size,is_active
            FROM sizes
            ORDER BY id
            LIMIT ? OFFSET ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int offset = (pageNo - 1) * pageSize;

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Size size = new Size();
                size.setId(rs.getLong("id"));
                size.setSize(rs.getString("size"));
                size.setActive(rs.getBoolean("is_active"));
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

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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

    public Size getById(long sizeId) {
        String sql = """
            SELECT *
            FROM sizes c
            WHERE c.id = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            Size size = null;
            ps.setLong(1, sizeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                size = new Size();
                size.setId(rs.getLong("id"));
                size.setSize(rs.getString("size"));
            }

            return size;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {

        String sql = "SELECT COUNT(*) FROM sizes";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void updateSizeStatus(long id, boolean active) {

        String sql = "UPDATE sizes SET is_active = ? WHERE id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
