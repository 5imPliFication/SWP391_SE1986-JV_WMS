package com.example.dao;

import com.example.config.DBConfig;
import com.example.enums.MovementType;
import com.example.enums.ReferenceType;
import com.example.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StockMovementDAO {

    public List<StockMovement> getStockHistory(
            LocalDate fromDate,
            LocalDate toDate,
            MovementType type,
            ReferenceType referenceType,
            int limit,
            int offset) {

        List<StockMovement> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT id, product_id, quantity, type, reference_type, created_at
                FROM stock_movements
                WHERE 1=1
                """);

        if (fromDate != null)
            sql.append(" AND DATE(created_at) >= ?");

        if (toDate != null)
            sql.append(" AND DATE(created_at) <= ?");

        if (type != null)
            sql.append(" AND type = ?");

        if (referenceType != null)
            sql.append(" AND reference_type = ?");

        sql.append(" ORDER BY created_at DESC");
        sql.append(" LIMIT ? OFFSET ?");

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (fromDate != null)
                ps.setDate(index++, Date.valueOf(fromDate));

            if (toDate != null)
                ps.setDate(index++, Date.valueOf(toDate));

            if (type != null)
                ps.setString(index++, type.name());

            if (referenceType != null)
                ps.setString(index++, referenceType.name());

            ps.setInt(index++, limit);
            ps.setInt(index++, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                StockMovement movement = new StockMovement(
                        rs.getLong("id"),
                        rs.getLong("product_id"),
                        rs.getLong("quantity"),
                        MovementType.valueOf(rs.getString("type")),
                        ReferenceType.valueOf(rs.getString("reference_type")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );

                list.add(movement);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching stock history", e);
        }

        return list;
    }

    public int getTotalCount(LocalDate fromDate, LocalDate toDate, MovementType type, ReferenceType referenceType) {
        StringBuilder sql = new StringBuilder("""
                SELECT COUNT(*)
                FROM stock_movements
                WHERE 1=1
                """);

        if (fromDate != null)
            sql.append(" AND DATE(created_at) >= ?");

        if (toDate != null)
            sql.append(" AND DATE(created_at) <= ?");

        if (type != null)
            sql.append(" AND type = ?");

        if (referenceType != null)
            sql.append(" AND reference_type = ?");

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (fromDate != null)
                ps.setDate(index++, Date.valueOf(fromDate));

            if (toDate != null)
                ps.setDate(index++, Date.valueOf(toDate));

            if (type != null)
                ps.setString(index++, type.name());

            if (referenceType != null)
                ps.setString(index++, referenceType.name());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting stock history", e);
        }
        return 0;
    }
}