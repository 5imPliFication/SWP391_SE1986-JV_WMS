package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.SummaryReportDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SummaryReportDAO {

    /**
     * Get summary report filtered by optional date range.
     * If fromDate/toDate are null, returns all-time totals.
     */
    public SummaryReportDTO getSummaryReport(LocalDate fromDate, LocalDate toDate) {
        SummaryReportDTO dto = new SummaryReportDTO();

        try (Connection conn = DBConfig.getDataSource().getConnection()) {

            // 1. Total imports (sum of actual quantity from goods_receipts)
            StringBuilder importSql = new StringBuilder(
                "SELECT COALESCE(SUM(total_actual_quantity), 0) AS total FROM goods_receipts WHERE 1=1"
            );
            if (fromDate != null) importSql.append(" AND DATE(received_at) >= ?");
            if (toDate != null)   importSql.append(" AND DATE(received_at) <= ?");

            try (PreparedStatement ps = conn.prepareStatement(importSql.toString())) {
                int idx = 1;
                if (fromDate != null) ps.setDate(idx++, Date.valueOf(fromDate));
                if (toDate != null)   ps.setDate(idx, Date.valueOf(toDate));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    dto.setTotalImport(rs.getLong("total"));
                }
            }

            // 2. Total exports (count of orders excluding DRAFT)
            StringBuilder exportSql = new StringBuilder(
                "SELECT COUNT(*) AS total FROM orders WHERE status NOT IN ('DRAFT')"
            );
            if (fromDate != null) exportSql.append(" AND DATE(order_date) >= ?");
            if (toDate != null)   exportSql.append(" AND DATE(order_date) <= ?");

            try (PreparedStatement ps = conn.prepareStatement(exportSql.toString())) {
                int idx = 1;
                if (fromDate != null) ps.setDate(idx++, Date.valueOf(fromDate));
                if (toDate != null)   ps.setDate(idx, Date.valueOf(toDate));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    dto.setTotalExport(rs.getLong("total"));
                }
            }

            // 3. Current inventory (sum of total_quantity from active products)
            // Inventory is always current snapshot, not filtered by date
            String inventorySql = "SELECT COALESCE(SUM(total_quantity), 0) AS total FROM products WHERE is_active = true";
            try (PreparedStatement ps = conn.prepareStatement(inventorySql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto.setTotalInStock(rs.getLong("total"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get summary report", e);
        }

        return dto;
    }
}
