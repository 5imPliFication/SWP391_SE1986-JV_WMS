package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.ImportHistoryDTO;
import com.example.util.AppConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodsHistoryDAO {

    public List<ImportHistoryDTO> getImportHistory(String fromDate, String toDate, int pageNo) {
        List<ImportHistoryDTO> historyList = new ArrayList<>();

        // sql
        StringBuilder sql = new StringBuilder("""
                    SELECT gr.id, pr.request_code as receiptCode, gr.received_at, u.fullname as staffName,
                           gr.total_actual_quantity as totalQuantity, gr.note
                    FROM goods_receipts gr
                    LEFT JOIN purchase_requests pr ON gr.purchase_request_id = pr.id
                    LEFT JOIN users u ON gr.warehouse_staff_id = u.id
                    WHERE 1=1
                """);

        // handle date
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND gr.received_at >= ?");
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND gr.received_at <= ?");
        }

        // pagination
        sql.append(" ORDER BY gr.received_at DESC LIMIT ? OFFSET ?");

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (fromDate != null && !fromDate.isEmpty()) {
                ps.setString(paramIndex++, fromDate + " 00:00:00");
            }
            if (toDate != null && !toDate.isEmpty()) {
                ps.setString(paramIndex++, toDate + " 23:59:59");
            }

            ps.setInt(paramIndex++, AppConstants.PAGE_SIZE);
            ps.setInt(paramIndex, (pageNo - 1) * AppConstants.PAGE_SIZE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportHistoryDTO dto = new ImportHistoryDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setReceiptCode(rs.getString("receiptCode"));
                    dto.setReceivedAt(rs.getTimestamp("received_at"));
                    dto.setStaffName(rs.getString("staffName"));
                    dto.setTotalQuantity(rs.getLong("totalQuantity"));
                    dto.setNote(rs.getString("note"));
                    historyList.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public int countImportHistory(String fromDate, String toDate) {

        // sql
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM goods_receipts gr WHERE 1=1");

        // handle date
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND gr.received_at >= ?");
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND gr.received_at <= ?");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (fromDate != null && !fromDate.isEmpty()) {
                ps.setString(paramIndex++, fromDate + " 00:00:00");
            }
            if (toDate != null && !toDate.isEmpty()) {
                ps.setString(paramIndex, toDate + " 23:59:59");
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
