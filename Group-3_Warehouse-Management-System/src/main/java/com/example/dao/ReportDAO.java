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
}
