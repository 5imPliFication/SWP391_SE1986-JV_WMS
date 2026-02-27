package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.ImportHistoryDTO;
import com.example.util.AppConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoodsHistoryDAO {

    public List<ImportHistoryDTO> getImportHistory(String code, String fromDate, String toDate, int pageNo) {
        List<ImportHistoryDTO> historyList = new ArrayList<>();

        // sql
        StringBuilder sql = new StringBuilder("""
                    select gr.id, pr.request_code as receiptCode, gr.received_at, u.fullname as warehouseName,
                           gr.total_actual_quantity as totalQuantity, gr.note
                    from goods_receipts gr
                     join purchase_requests pr on gr.purchase_request_id = pr.id
                     join users u ON gr.warehouse_staff_id = u.id
                    where 1=1
                """);

        if (code != null && !code.trim().isEmpty()) {
            sql.append(" and pr.request_code like ? ");
        }

        // handle date
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" and gr.received_at >= ?");
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" and gr.received_at <= ?");
        }

        // pagination
        sql.append(" order by gr.received_at desc limit ? offset ?");

        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (code != null && !code.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + code + "%");
            }

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
                    dto.setWarehouseName(rs.getString("warehouseName"));
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

    public int countImportHistory(String code, String fromDate, String toDate) {

        // sql
        StringBuilder sql = new StringBuilder("select count(*) from goods_receipts gr " +
                "join purchase_requests pr ON gr.purchase_request_id = pr.id WHERE 1=1 ");

        if (code != null && !code.trim().isEmpty()) {
            sql.append(" and pr.request_code like ? ");
        }

        // handle date
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" and gr.received_at >= ?");
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" and gr.received_at <= ?");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (code != null && !code.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + code + "%");
            }

            if (fromDate != null && !fromDate.isEmpty()) {
                ps.setString(paramIndex++, fromDate + " 00:00:00");
            }
            if (toDate != null && !toDate.isEmpty()) {
                ps.setString(paramIndex++, toDate + " 23:59:59");
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
