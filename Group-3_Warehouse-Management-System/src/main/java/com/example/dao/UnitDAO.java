package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Ram;
import com.example.model.Storage;
import com.example.model.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO {
    public List<Unit> getAll() {
        List<Unit> units = new ArrayList<>();
        String sql = """
                SELECT *
                FROM units
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setId(rs.getLong("id"));
                unit.setName(rs.getString("name"));

                units.add(unit);
            }
            return units;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Unit getById(long unitId) {
        String sql = """
            SELECT *
            FROM units u
            WHERE u.id = ?
            """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            Unit unit = null;
            ps.setLong(1, unitId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                unit = new Unit();
                unit.setId(rs.getLong("id"));
                unit.setName(rs.getString("name"));
            }

            return unit;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
