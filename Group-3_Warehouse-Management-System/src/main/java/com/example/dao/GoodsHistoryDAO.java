package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.ImportHistoryDTO;
import com.example.dto.ImportHistoryDetailDTO;
import com.example.util.AppConstants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoodsHistoryDAO {

    public List<ImportHistoryDTO> getImportHistory(String code, LocalDate fromDate, LocalDate toDate, int pageNo) {
        List<ImportHistoryDTO> historyList = new ArrayList<>();

        // sql
        StringBuilder sql = new StringBuilder("""
                    select gr.id,
                           pr.request_code as receiptCode,
                           gr.received_at,
                           u.fullname as warehouseName,
                           coalesce(sum(gri.actual_quantity), 0) as totalQuantity,
                           gr.note
                    from goods_receipts gr
                    join purchase_requests pr on gr.purchase_request_id = pr.id
                    join users u ON gr.warehouse_id = u.id
                    left join goods_receipt_items gri on gri.goods_receipt_id = gr.id
                    where 1=1
                """);

        if (code != null && !code.trim().isEmpty()) {
            sql.append(" and pr.request_code like ? ");
        }

        // handle date
        if (fromDate != null) {
            sql.append(" and cast(gr.received_at as date) >= ?");
        }
        if (toDate != null) {
            sql.append(" and cast(gr.received_at as date) <= ?");
        }

        // pagination
        sql.append(" group by gr.id, pr.request_code, gr.received_at, u.fullname, gr.note ");
        sql.append(" order by gr.received_at desc limit ? offset ?");

        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (code != null && !code.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + code + "%");
            }

            if (fromDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(fromDate));
            }
            if (toDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(toDate));
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

    public int countImportHistory(String code, LocalDate fromDate, LocalDate toDate) {

        // sql
        StringBuilder sql = new StringBuilder("select count(*) from goods_receipts gr " +
                "join purchase_requests pr ON gr.purchase_request_id = pr.id WHERE 1=1 ");

        if (code != null && !code.trim().isEmpty()) {
            sql.append(" and pr.request_code like ? ");
        }

        // handle date
        if (fromDate != null) {
            sql.append(" and cast(gr.received_at as date) >= ?");
        }
        if (toDate != null) {
            sql.append(" and cast(gr.received_at as date) <= ?");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (code != null && !code.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + code + "%");
            }

            if (fromDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(fromDate));
            }
            if (toDate != null) {
                ps.setDate(paramIndex++, Date.valueOf(toDate));
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

    public ImportHistoryDTO getImportHistoryById(Long id) {
        String sql = """
                    select gr.id,
                           pr.request_code as receiptCode,
                           gr.received_at,
                           u.fullname as warehouseName,
                           coalesce(sum(gri.actual_quantity), 0) as totalQuantity,
                           gr.note
                    from goods_receipts gr
                    join purchase_requests pr on gr.purchase_request_id = pr.id
                    join users u ON gr.warehouse_id = u.id
                    left join goods_receipt_items gri on gri.goods_receipt_id = gr.id
                    where gr.id = ?
                    group by gr.id, pr.request_code, gr.received_at, u.fullname, gr.note
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ImportHistoryDTO dto = new ImportHistoryDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setReceiptCode(rs.getString("receiptCode"));
                    dto.setReceivedAt(rs.getTimestamp("received_at"));
                    dto.setWarehouseName(rs.getString("warehouseName"));
                    dto.setTotalQuantity(rs.getLong("totalQuantity"));
                    dto.setNote(rs.getString("note"));
                    return dto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ImportHistoryDetailDTO> getImportHistoryItems(Long receiptId) {
        List<ImportHistoryDetailDTO> items = new ArrayList<>();
        String sql = """
                    select p.name as productName,
                           pri.quantity as expected_quantity,
                           gri.actual_quantity
                    from goods_receipt_items gri
                    join goods_receipts gr on gri.goods_receipt_id = gr.id
                    join products p on gri.product_id = p.id
                    left join purchase_request_items pri
                      on pri.purchase_request_id = gr.purchase_request_id
                     and pri.product_id = gri.product_id
                    where gri.goods_receipt_id = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, receiptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImportHistoryDetailDTO dto = new ImportHistoryDetailDTO();
                    dto.setProductName(rs.getString("productName"));
                    dto.setExpectedQuantity(rs.getLong("expected_quantity"));
                    dto.setActualQuantity(rs.getLong("actual_quantity"));
                    items.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
