package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.SummaryReportDTO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SummaryReportDAO {

    /**
     * Get summary report filtered by optional date range. If fromDate/toDate
     * are null, returns all-time totals.
     */
    public List<SummaryReportDTO> getInventoryReport(LocalDate fromDate, LocalDate toDate) {

        List<SummaryReportDTO> list = new ArrayList<>();

        // ✅ set default dates nếu null
        LocalDate from = (fromDate != null) ? fromDate : LocalDate.of(2000, 1, 1);
        LocalDate to = (toDate != null) ? toDate : LocalDate.now();

        String sql = """
        SELECT 
            p.name AS product_name,

            -- tồn đầu
            COALESCE(SUM(
                CASE 
                    WHEN sm.created_at < ? THEN
                        CASE 
                            WHEN sm.type = 'IMPORT' THEN sm.quantity
                            WHEN sm.type = 'EXPORT' THEN -sm.quantity
                        END
                END
            ),0) AS opening_stock,

            -- nhập
            COALESCE(SUM(
                CASE 
                    WHEN sm.type = 'IMPORT'
                    AND sm.created_at BETWEEN ? AND ? THEN sm.quantity
                END
            ),0) AS total_import,

            -- xuất
            COALESCE(SUM(
                CASE 
                    WHEN sm.type = 'EXPORT'
                    AND sm.created_at BETWEEN ? AND ? THEN sm.quantity
                END
            ),0) AS total_export,

            -- tồn cuối
            COALESCE(SUM(
                CASE 
                    WHEN sm.created_at <= ? THEN
                        CASE 
                            WHEN sm.type = 'IMPORT' THEN sm.quantity
                            WHEN sm.type = 'EXPORT' THEN -sm.quantity
                        END
                END
            ),0) AS closing_stock

        FROM products p
        LEFT JOIN stock_movements sm 
            ON p.id = sm.product_id
            AND sm.type IN ('IMPORT','EXPORT')

        GROUP BY p.id, p.name
        """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // ✅ set param với from/to đã đảm bảo không null
            ps.setDate(1, Date.valueOf(from)); // opening stock
            ps.setDate(2, Date.valueOf(from)); // total_import start
            ps.setDate(3, Date.valueOf(to));   // total_import end
            ps.setDate(4, Date.valueOf(from)); // total_export start
            ps.setDate(5, Date.valueOf(to));   // total_export end
            ps.setDate(6, Date.valueOf(to));   // closing stock

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SummaryReportDTO dto = new SummaryReportDTO();
                dto.setProductName(rs.getString("product_name"));
                dto.setOpeningStock(rs.getLong("opening_stock"));
                dto.setTotalImport(rs.getLong("total_import"));
                dto.setTotalExport(rs.getLong("total_export"));
                dto.setClosingStock(rs.getLong("closing_stock"));

                list.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting inventory report", e);
        }

        return list;
    }
}
