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

    public List<SummaryReportDTO> getInventoryReport(LocalDate fromDate, LocalDate toDate) {

        List<SummaryReportDTO> list = new ArrayList<>();

        boolean filterByDate = fromDate != null && toDate != null;

        String sql;
        if (filterByDate) {
            // Lọc theo tháng: tính tồn đầu + import/export trong tháng + closing đúng tháng
            sql = """
            SELECT 
                p.name AS product_name,
                
                -- tồn đầu
                COALESCE(SUM(CASE 
                    WHEN sm.created_at < ? AND sm.type = 'IMPORT' THEN sm.quantity
                    WHEN sm.created_at < ? AND sm.type = 'EXPORT' THEN -sm.quantity
                    ELSE 0 END),0) AS opening_stock,
                
                -- tổng nhập trong tháng
                COALESCE(SUM(CASE WHEN sm.type = 'IMPORT' AND sm.created_at BETWEEN ? AND ? THEN sm.quantity ELSE 0 END),0) AS total_import,
                
                -- tổng xuất trong tháng
                COALESCE(SUM(CASE WHEN sm.type = 'EXPORT' AND sm.created_at BETWEEN ? AND ? THEN sm.quantity ELSE 0 END),0) AS total_export,
                
                -- tồn cuối = tồn đầu + import - export
                COALESCE(SUM(CASE 
                    WHEN sm.created_at < ? AND sm.type = 'IMPORT' THEN sm.quantity
                    WHEN sm.created_at < ? AND sm.type = 'EXPORT' THEN -sm.quantity
                    ELSE 0 END),0)
                + COALESCE(SUM(CASE WHEN sm.type = 'IMPORT' AND sm.created_at BETWEEN ? AND ? THEN sm.quantity ELSE 0 END),0)
                - COALESCE(SUM(CASE WHEN sm.type = 'EXPORT' AND sm.created_at BETWEEN ? AND ? THEN sm.quantity ELSE 0 END),0)
                AS closing_stock

            FROM products p
            LEFT JOIN stock_movements sm 
                ON p.id = sm.product_id AND sm.type IN ('IMPORT','EXPORT')
            GROUP BY p.id, p.name
            ORDER BY closing_stock DESC
            """;
        } else {
            // Clear / all-time: tổng cả đời, opening = 0
            sql = """
            SELECT 
                p.name AS product_name,
                0 AS opening_stock,
                COALESCE(SUM(CASE WHEN sm.type = 'IMPORT' THEN sm.quantity ELSE 0 END),0) AS total_import,
                COALESCE(SUM(CASE WHEN sm.type = 'EXPORT' THEN sm.quantity ELSE 0 END),0) AS total_export,
                COALESCE(SUM(CASE WHEN sm.type = 'IMPORT' THEN sm.quantity
                                  WHEN sm.type = 'EXPORT' THEN -sm.quantity
                                  ELSE 0 END),0) AS closing_stock
            FROM products p
            LEFT JOIN stock_movements sm 
                ON p.id = sm.product_id AND sm.type IN ('IMPORT','EXPORT')
            GROUP BY p.id, p.name
            ORDER BY closing_stock DESC
            """;
        }

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (filterByDate) {
                // tham số cho opening_stock
                ps.setDate(1, Date.valueOf(fromDate));
                ps.setDate(2, Date.valueOf(fromDate));

                // tham số cho total_import
                ps.setDate(3, Date.valueOf(fromDate));
                ps.setDate(4, Date.valueOf(toDate));

                // tham số cho total_export
                ps.setDate(5, Date.valueOf(fromDate));
                ps.setDate(6, Date.valueOf(toDate));

                // tham số cho closing_stock = tồn đầu + import - export
                ps.setDate(7, Date.valueOf(fromDate));
                ps.setDate(8, Date.valueOf(fromDate));
                ps.setDate(9, Date.valueOf(fromDate));
                ps.setDate(10, Date.valueOf(toDate));
                ps.setDate(11, Date.valueOf(fromDate));
                ps.setDate(12, Date.valueOf(toDate));
            }

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
