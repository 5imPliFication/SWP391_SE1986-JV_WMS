package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Chip;

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

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
}
