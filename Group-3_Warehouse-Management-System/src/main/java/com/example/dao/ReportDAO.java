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

    public List<ReportItemDTO> getImportReportByMonth(String yearMonth) {
        List<ReportItemDTO> items = new ArrayList<>();
        String sql = """
            SELECT p.name AS product_name, SUM(gri.actual_quantity) AS quantity
            FROM goods_receipt_items gri
            JOIN goods_receipts gr ON gri.goods_receipt_id = gr.id
            JOIN products p ON gri.product_id = p.id
            WHERE DATE_FORMAT(gr.received_at, '%Y-%m') = ?
            GROUP BY p.name
            ORDER BY quantity DESC
        """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, yearMonth);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new ReportItemDTO(rs.getString("product_name"), rs.getLong("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Long> getImportChartDataByYear(int year) {
        // Initialize with 0 for 12 months
        List<Long> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(0L);
        }

        String sql = """
            SELECT MONTH(gr.received_at) AS month, SUM(gri.actual_quantity) AS total_quantity
            FROM goods_receipt_items gri
            JOIN goods_receipts gr ON gri.goods_receipt_id = gr.id
            WHERE YEAR(gr.received_at) = ?
            GROUP BY MONTH(gr.received_at)
        """;
        
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month"); // 1-indexed
                    long quantity = rs.getLong("total_quantity");
                    if (month >= 1 && month <= 12) {
                        data.set(month - 1, quantity);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
