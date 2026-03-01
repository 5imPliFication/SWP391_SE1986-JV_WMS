package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.GoodsReceiptDTO;
import com.example.dto.GoodsReceiptItemDTO;
import com.example.dto.ImportHistoryDTO;
import com.example.util.AppConstants;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GoodsHistoryDAO {

    public List<GoodsReceiptDTO> getGoodsReceipts(String code, LocalDate fromDate, LocalDate toDate, int pageNo) {
        List<GoodsReceiptDTO> receiptList = new ArrayList<>();

        // sql
        StringBuilder sql = new StringBuilder("""
                    select gr.id, pr.request_code as 'receipt_code', gr.received_at, u.fullname as 'warehouse_name'
                    from goods_receipts gr
                    join purchase_requests pr
                    on gr.purchase_request_id = pr.id
                    join users u
                    on u.id = gr.warehouse_id
                    where 1 = 1
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
                    GoodsReceiptDTO dto = new GoodsReceiptDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setReceiptCode(rs.getString("receipt_code"));
                    dto.setReceivedAt(rs.getObject("received_at", LocalDateTime.class));
                    dto.setWarehouseName(rs.getString("warehouse_name"));
                    receiptList.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receiptList;
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

    public GoodsReceiptDTO getGoodsReceiptById(Long id) {
        StringBuilder sql = new StringBuilder("""
                select gr.id, pr.request_code, gr.received_at, u.fullname, pr.note
                from goods_receipts gr
                join purchase_requests pr
                on gr.purchase_request_id = pr.id
                join users u
                on u.id = gr.warehouse_id where gr.id = ?
                """);
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // set value
            if (id != null) {
                ps.setLong(1, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    GoodsReceiptDTO dto = new GoodsReceiptDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setReceiptCode(rs.getString("request_code"));
                    dto.setReceivedAt(rs.getObject("received_at", LocalDateTime.class));
                    dto.setWarehouseName(rs.getString("fullname"));
                    dto.setNote(rs.getString("note"));
                    return dto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // get goods receipt item by good receiptId
    public List<GoodsReceiptItemDTO> getGoodsReceiptItems(Long receiptId) {
        List<GoodsReceiptItemDTO> items = new ArrayList<>();
        String sql = """
                select p.name, gri.actual_quantity, pri.quantity as 'expected_quantity' from goods_receipts gr
                join goods_receipt_items gri
                on gr.id = gri.goods_receipt_id
                join products p
                on gri.product_id = p.id
                join purchase_request_items pri
                on pri.purchase_request_id = gr.purchase_request_id\s
                where gri.goods_receipt_id = ?
                and pri.product_id = gri.product_id;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // set value
            if (receiptId != null) {
                ps.setLong(1, receiptId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GoodsReceiptItemDTO dto = new GoodsReceiptItemDTO();
                    dto.setProductName(rs.getString("name"));
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
