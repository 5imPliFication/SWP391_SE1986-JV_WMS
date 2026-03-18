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
}
