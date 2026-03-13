package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Chip;
import com.example.model.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChipDAO {

    public List<Chip> getAllActive() {
        List<Chip> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM chips
                WHERE is_active = true
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Chip chip = new Chip();
                chip.setId(rs.getLong("id"));
                chip.setName(rs.getString("name"));

                list.add(chip);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Chip> getChipsByPage(int pageNo, int pageSize) {

        List<Chip> list = new ArrayList<>();

        String sql = """
        SELECT id, name, is_active
        FROM chips
        ORDER BY id
        LIMIT ? OFFSET ?
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            int offset = (pageNo - 1) * pageSize;

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Chip chip = new Chip();

                chip.setId(rs.getLong("id"));
                chip.setName(rs.getString("name"));
                chip.setActive(rs.getBoolean("is_active"));

                list.add(chip);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Chip> getAll() {
        List<Chip> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM chips
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Chip chip = new Chip();
                chip.setId(rs.getLong("id"));
                chip.setName(rs.getString("name"));

                list.add(chip);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public Chip getById(long chipId) {
        String sql = """
            SELECT *
            FROM chips c
            WHERE c.id = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            Chip chip = null;
            ps.setLong(1, chipId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                chip = new Chip();
                chip.setId(rs.getLong("id"));
                chip.setName(rs.getString("name"));
            }

            return chip;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int count() {

        String sql = "SELECT COUNT(*) FROM chips";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void updateChipStatus(long id, boolean active) {

        String sql = "UPDATE chips SET is_active = ? WHERE id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createChip(String name, boolean active) {

        String sql = """
        INSERT INTO chips (name, is_active)
        VALUES (?, ?)
    """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setBoolean(2, active);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
