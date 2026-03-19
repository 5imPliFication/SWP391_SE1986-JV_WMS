package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.ReportItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    // get quantity imported for each month of the year
    public List<Long> getImportChartDataByYear(int year) {
        // init list with 0 for 12 months
        List<Long> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(0L);
        }

        String sql = """
                    select month(gr.received_at) as month, sum(gri.actual_quantity) as quantity from goods_receipts gr
                  join goods_receipt_items gri\s
                  on gr.id = gri.goods_receipt_id
                  where year(gr.received_at) = ?
                  group by month(gr.received_at)
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    long quantity = rs.getLong("quantity");
                    data.set(month - 1, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    // Get list of items for import report
    public List<ReportItemDTO> getImportItems(int month, int year) {
        List<ReportItemDTO> items = new ArrayList<>();
        String sql = """
                    select p.name, sum(gri.actual_quantity) as quantity from goods_receipt_items gri
                  join products p\s
                  on gri.product_id = p.id
                  join goods_receipts gr\s
                  on gr.id = gri.goods_receipt_id
                  where year(gr.received_at) = ? and month(gr.received_at) = ?
                  group by (p.name);
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                long quantity = rs.getLong("quantity");
                items.add(new ReportItemDTO(productName, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Long> getInventoryChartDataByYear(int year) {
        List<Long> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(0L);
        }

        // 1. Get initial balance before the year starts
        long currentBalance = 0;
        String initialBalanceSql = """
                SELECT SUM(CASE WHEN type = 'IMPORT' THEN quantity ELSE -quantity END) as initial_balance
                FROM stock_movements
                WHERE YEAR(created_at) < ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(initialBalanceSql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentBalance = rs.getLong("initial_balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Get monthly net changes for the year
        String monthlyChangesSql = """
                SELECT MONTH(created_at) as month, SUM(CASE WHEN type = 'IMPORT' THEN quantity ELSE -quantity END) as net_change
                FROM stock_movements
                WHERE YEAR(created_at) = ?
                GROUP BY MONTH(created_at)
                ORDER BY MONTH(created_at)
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(monthlyChangesSql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    long netChange = rs.getLong("net_change");
                    // Inventory at end of month = previous balance + current net change

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Let's refine the logic to better handle months with no movements
        long[] netChanges = new long[13]; // 1-12
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(monthlyChangesSql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    netChanges[rs.getInt("month")] = rs.getLong("net_change");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int m = 1; m <= 12; m++) {
            currentBalance += netChanges[m];
            data.set(m - 1, currentBalance);
        }

        return data;
    }

    public List<ReportItemDTO> getInventoryItems(int month, int year) {
        List<ReportItemDTO> items = new ArrayList<>();
        String sql = """
                SELECT p.name, SUM(CASE WHEN sm.type = 'IMPORT' THEN sm.quantity ELSE -sm.quantity END) as quantity
                FROM stock_movements sm
                JOIN products p ON sm.product_id = p.id
                WHERE YEAR(sm.created_at) < ? OR (YEAR(sm.created_at) = ? AND MONTH(sm.created_at) <= ?)
                GROUP BY p.name
                HAVING quantity > 0
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, year);
            ps.setInt(3, month);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new ReportItemDTO(rs.getString("name"), rs.getLong("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public long getImportByMonth(int month, int year) {
        String sql = """
                SELECT SUM(quantity) as total FROM stock_movements
                WHERE type = 'IMPORT' AND YEAR(created_at) = ? AND MONTH(created_at) = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getExportByMonth(int month, int year) {
        String sql = """
                SELECT SUM(quantity) as total FROM stock_movements
                WHERE type = 'EXPORT' AND YEAR(created_at) = ? AND MONTH(created_at) = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
