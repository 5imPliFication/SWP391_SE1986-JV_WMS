package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.GoodsReceiptDTO;
import com.example.dto.GoodsReceiptItemDTO;
import com.example.dto.ImportHistoryDTO;
import com.example.dto.ProductItemDTO;
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
        StringBuilder sql = new StringBuilder(
                """
                        select gr.id, pr.request_code, request_users.fullname as requestor_name,
                         warehouse.fullname as warehouse_name, pr.created_at as request_date,
                         gr.received_at, pr.note, approver.fullname as approver, gr.supplier
                         from goods_receipts gr
                         join purchase_requests pr
                         on gr.purchase_request_id = pr.id
                         join users request_users
                         on request_users.id = pr.created_by
                         join users warehouse
                         on warehouse.id = gr.warehouse_id
                         join users approver
                         on approver.id = pr.approved_by
                         where gr.id = ?
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
                    dto.setWarehouseName(rs.getString("warehouse_name"));
                    dto.setNote(rs.getString("note"));
                    dto.setRequestorName(rs.getString("requestor_name"));
                    dto.setRequestDate(rs.getObject("request_date", LocalDateTime.class));
                    dto.setApprover(rs.getString("approver"));
                    dto.setSupplier(rs.getString("supplier"));
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
                select gri.id, pr.name, pri.quantity as expected_quantity, gri.actual_quantity, u.name as unit_name from
               goods_receipt_items gri
               join goods_receipts gr
               on gri.goods_receipt_id = gr.id
               join products pr
               on gri.product_id = pr.id
               join units u on pr.unit_id = u.id
               join purchase_request_items pri
               on pri.purchase_request_id = gr.purchase_request_id and pri.product_id = gri.product_id
                where gri.goods_receipt_id = ?;
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
                    dto.setId(rs.getLong("id"));
                    dto.setProductName(rs.getString("name"));
                    dto.setUnitName(rs.getString("unit_name"));
                    dto.setExpectedQuantity(rs.getLong("expected_quantity"));
                    dto.setActualQuantity(rs.getLong("actual_quantity"));
                    // get product items by receipt item id
                    dto.setProductItems(getProductItemsByReceiptItemId(dto.getId()));
                    items.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // get product items by receipt item id
    private List<ProductItemDTO> getProductItemsByReceiptItemId(Long goodsReceiptItemId) {
        List<ProductItemDTO> list = new ArrayList<>();
        String sql = """
                select pi.id as item_id, p.id, p.name, pi.serial, pi.imported_price from\s
                product_items pi
                join products p
                on p.id = pi.product_id
                where pi.goods_receipt_item_id = ?;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, goodsReceiptItemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductItemDTO dto = new ProductItemDTO();
                    dto.setId(rs.getLong("item_id"));
                    dto.setProductId(rs.getLong("id"));
                    dto.setProductName(rs.getString("name"));
                    dto.setSerial(rs.getString("serial"));
                    dto.setImportPrice(rs.getDouble("imported_price"));
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
