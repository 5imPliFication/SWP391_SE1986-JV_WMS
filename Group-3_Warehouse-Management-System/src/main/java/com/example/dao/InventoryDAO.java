package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.OrderDTO;
import com.example.dto.ProductDTO;
import com.example.dto.ProductItemDTO;
import com.example.util.AppConstants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryDAO {

    // get list product by name
    public List<ProductDTO> findProductByName(String searchName) {
        List<ProductDTO> listProducts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select id, name, description, total_quantity from products as p "
                + " where 1 = 1 ");

        // if param has value of searchName
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" and p.name like ? ");
        }

        int index = 1;
        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // if searchName has value -> set value to query
            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index, "%" + searchName + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                long totalQuantity = rs.getLong("total_quantity");
                listProducts.add(new ProductDTO(id, name, description, totalQuantity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listProducts;
    }

    public boolean saveProductItems(Long purchaseRequestId, Long warehouseUserId, List<ProductItemDTO> productItemDTOs) {

        // count quantity import product for each product
        Map<Long, Long> quantityByProduct = countQuantityImportByProductId(productItemDTOs);

        try {
            // save product item: increase total quantity in table products and save each product items
            // to table product items
            insertProductItems(productItemDTOs);

            // update status of purchase request -> completed
            completePurchaseRequest(purchaseRequestId);

            // save purchase request to table good receipts
            Long receiptId = insertGoodsReceipt(purchaseRequestId, warehouseUserId);

            // save purchase request item to table goods receipt items
            insertGoodsReceiptItems(receiptId, quantityByProduct);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<Long, Long> countQuantityImportByProductId(List<ProductItemDTO> productItemDTOs) {
        Map<Long, Long> quantityProduct = new HashMap<>();
        for (ProductItemDTO dto : productItemDTOs) {
            //get id of product
            Long productId = dto.getProductId();
            //get current quantity
            Long currentQuantity = quantityProduct.get(productId);
            // if not exist -> new quantity = 1
            if (currentQuantity == null) {
                quantityProduct.put(productId, 1L);
            } else {
                // exist -> quantity + 1
                quantityProduct.put(productId, currentQuantity + 1);
            }
        }
        return quantityProduct;
    }

    private Long insertGoodsReceipt(Long purchaseRequestId, Long warehouseUserId)
            throws SQLException {
        String insertReceiptSql = "INSERT INTO goods_receipts(purchase_request_id, warehouse_id, received_at) VALUES (?, ?, NOW())";
        Long receiptId = null;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(insertReceiptSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, purchaseRequestId);
            ps.setLong(2, warehouseUserId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    receiptId = rs.getLong(1);
                }
            }
        }
        return receiptId;
    }

    private void insertGoodsReceiptItems(Long receiptId, Map<Long, Long> qtyByProduct)
            throws SQLException {
        String insertReceiptItemSql = "INSERT INTO goods_receipt_items(goods_receipt_id, product_id, actual_quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(insertReceiptItemSql)) {
            for (Map.Entry<Long, Long> entry : qtyByProduct.entrySet()) {
                int idx = 1;
                ps.setLong(idx++, receiptId);
                ps.setLong(idx++, entry.getKey());
                ps.setLong(idx, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertProductItems(List<ProductItemDTO> productItemDTOs) throws SQLException {
        String insertProductItemSql = "INSERT INTO product_items(serial, imported_price, current_price, is_active, imported_at, updated_at, product_id) VALUES (?, ?, ?, ?, NOW(), NOW(), ?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(insertProductItemSql)) {
            for (ProductItemDTO item : productItemDTOs) {
                int idx = 1;
                ps.setString(idx++, item.getSerial());
                ps.setDouble(idx++, item.getImportPrice());
                ps.setDouble(idx++, item.getImportPrice());
                ps.setBoolean(idx++, true);
                ps.setLong(idx, item.getProductId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void completePurchaseRequest(Long purchaseRequestId) throws SQLException {
        String completePrSql = "UPDATE purchase_requests SET status = 'COMPLETED', updated_at = NOW() WHERE id = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(completePrSql)) {
            ps.setLong(1, purchaseRequestId);
            ps.executeUpdate();
        }
    }

    public boolean isExistSerial(String serial) {
        StringBuilder sql = new StringBuilder("select serial from product_items as pt "
                + "where 1 = 1 ");

        // if param has value of searchName
        if (serial != null && !serial.trim().isEmpty()) {
            sql.append(" and pt.serial like ? ");
        }

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // if searchName has value -> set value to query
            if (serial != null) {
                ps.setString(1, "%" + serial + "%");
            }

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> searchExportOrders(String name, LocalDate fromDate, LocalDate toDate, String status, int offset) {
        StringBuilder sql = new StringBuilder("""
                    select o.id, o.order_code, o.order_date, u.fullname as salesman_name, o.customer_name, o.status
                    from orders o
                    join users u ON o.created_by = u.id
                    where 1=1
                """);

        // name
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" and o.customer_name like ? ");
        }

        // handle date
        if (fromDate != null) {
            sql.append(" and CAST(o.order_date AS DATE) >= ?");
        }
        if (toDate != null) {
            sql.append(" and CAST(o.order_date AS DATE) <= ?");
        }

        // status
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" and o.status like ? ");
        } else {
            sql.append(" and o.status not in ('DRAFT', 'COMPLETED', 'CANCELLED') ");
        }

        // pagination
        sql.append(" ORDER BY o.order_date DESC LIMIT ? OFFSET ?");

        List<OrderDTO> list = new ArrayList<>();
        try (Connection con = DBConfig.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (name != null && !name.trim().isEmpty()) {
                ps.setString(index++, "%" + name + "%");
            }

            if (fromDate != null) {
                ps.setDate(index++, Date.valueOf(fromDate));
            }
            if (toDate != null) {
                ps.setDate(index++, Date.valueOf(toDate));
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, "%" + status + "%");
            }
            ps.setInt(index++, AppConstants.PAGE_SIZE);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderDTO dto = new OrderDTO();
                dto.setId(rs.getLong("id"));
                dto.setCode(rs.getString("order_code"));
                dto.setExportDate(rs.getTimestamp("order_date"));
                dto.setSalesmanName(rs.getString("salesman_name"));
                dto.setCustomerName(rs.getString("customer_name"));
                dto.setStatus(rs.getString("status"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find order fail", e);
        }
        return list;
    }

    // count total export product order
    public int countExportOrders(String name, LocalDate fromDate, LocalDate toDate, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders o WHERE 1=1 and o.status not in ('DRAFT', 'COMPLETED', 'CANCELLED') ");
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" and o.customer_name like ? ");
        }

        if (fromDate != null) {
            sql.append(" and cast(o.order_date AS DATE) >= ?");
        }
        if (toDate != null) {
            sql.append(" and cast(o.order_date AS DATE) <= ?");
        }

        // status
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" and o.status like ? ");
        } else {
            sql.append(" and o.status not in ('DRAFT', 'COMPLETED', 'CANCELLED') ");
        }

        try (Connection con = DBConfig.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (name != null && !name.trim().isEmpty()) {
                ps.setString(index++, "%" + name + "%");
            }

            if (fromDate != null) {
                ps.setDate(index++, Date.valueOf(fromDate));
            }
            if (toDate != null) {
                ps.setDate(index++, Date.valueOf(toDate));
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, "%" + status + "%");
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Count fail", e);
        }
        return 0;
    }
}
