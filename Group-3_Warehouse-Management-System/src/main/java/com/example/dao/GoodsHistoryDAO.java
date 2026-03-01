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
                    GoodsReceiptDTO dto = new GoodsReceiptDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setReceiptCode(rs.getString("receiptCode"));
//                    dto.setReceivedAt(rs.getTimestamp("received_at"));
                    dto.setWarehouseName(rs.getString("warehouseName"));
                    dto.setNote(rs.getString("note"));
                    return dto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get GoodsReceipt items by receipt ID for detail view
     */
    public List<GoodsReceiptItemDTO> getGoodsReceiptItems(Long receiptId) {
        List<GoodsReceiptItemDTO> items = new ArrayList<>();
        String sql = """
                    select gri.product_id,
                           p.name as productName,
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
                    GoodsReceiptItemDTO dto = new GoodsReceiptItemDTO();
                    dto.setProductId(rs.getLong("product_id"));
                    dto.setProductName(rs.getString("productName"));
                    Long expectedQty = rs.getLong("expected_quantity");
                    dto.setExpectedQuantity(rs.wasNull() ? null : expectedQty);
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
