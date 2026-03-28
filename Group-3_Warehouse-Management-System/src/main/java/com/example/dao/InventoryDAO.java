package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.ExportDTO;
import com.example.dto.ExportProductDTO;
import com.example.dto.OrderDTO;
import com.example.dto.ProductItemDTO;
import com.example.enums.MovementType;
import com.example.enums.ReferenceType;
import com.example.util.AppConstants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryDAO {

    // get list product by name
    public Long findProductIdByName(String searchName) {
        StringBuilder sql = new StringBuilder("select id from products as p where 1 = 1 ");

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
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean saveProductItems(Long purchaseRequestId, Long warehouseUserId,
                                    List<ProductItemDTO> productItemDTOs, String supplier) {

        // count quantity import product for each product
        Map<Long, Long> quantityByProduct = countQuantityImportByProductId(productItemDTOs);

        try {
            // update status of purchase request -> completed
            completePurchaseRequest(purchaseRequestId);

            // save to table goods receipts
            Long receiptId = insertGoodsReceipt(purchaseRequestId, warehouseUserId, supplier);

            // save purchase request item to table goods receipt items and get generated ids
            Map<Long, Long> receiptItemIds = insertGoodsReceiptItems(receiptId, quantityByProduct);

            // save product item
            insertProductItems(productItemDTOs, receiptItemIds);

            // update quantity in stock of product
            updateQuantityOfProducts(quantityByProduct);

            // save stock movement (Truong)
            new StockMovementDAO().insertStockMovements(
                    quantityByProduct,
                    MovementType.IMPORT,
                    ReferenceType.GOODS_RECEIPT,
                    purchaseRequestId
            );

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateQuantityOfProducts(Map<Long, Long> quantityByProduct) {

        String sql = "update products set total_quantity = total_quantity + ?, updated_at = NOW() where id = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Map.Entry<Long, Long> entry : quantityByProduct.entrySet()) {
                ps.setLong(1, entry.getValue());
                ps.setLong(2, entry.getKey());
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // count quantity of product corresponding
    private Map<Long, Long> countQuantityImportByProductId(List<ProductItemDTO> productItemDTOs) {
        Map<Long, Long> quantityProduct = new HashMap<>();
        for (ProductItemDTO dto : productItemDTOs) {
            // get id of product
            Long productId = dto.getProductId();
            // get current quantity
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

    private Long insertGoodsReceipt(Long purchaseRequestId, Long warehouseUserId, String supplier)
            throws SQLException {
        StringBuilder sql = new StringBuilder(
                "insert into goods_receipts(purchase_request_id, warehouse_id, received_at, supplier) values (?, ?, NOW(), ?)");
        Long receiptId = null;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {

            // set value
            ps.setLong(1, purchaseRequestId);
            ps.setLong(2, warehouseUserId);
            ps.setString(3, supplier);
            ps.executeUpdate();

            // get id of goods receipt
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    receiptId = rs.getLong(1);
                }
            }
        }

        // return id of goods receipt
        return receiptId;
    }

    // save goods receipt item correspond receiptId
    private Map<Long, Long> insertGoodsReceiptItems(Long receiptId, Map<Long, Long> quantityByProduct)
            throws SQLException {
        // create map to save productId and goods receipt item id after insert
        Map<Long, Long> productIdGoodsReceiptItemId = new HashMap<>();
        String sql = "insert into goods_receipt_items(goods_receipt_id, product_id, actual_quantity) values (?, ?, ?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // create list to save productId for get generated keys after insert
            List<Long> productIds = new ArrayList<>();
            // for each any product (key: productId, value: quantity)
            for (Map.Entry<Long, Long> entry : quantityByProduct.entrySet()) {
                int index = 1;
                ps.setLong(index++, receiptId);
                // set productId
                ps.setLong(index++, entry.getKey());
                // set quantity
                ps.setLong(index, entry.getValue());
                ps.addBatch();
                // add productId to list for get generated keys after insert
                productIds.add(entry.getKey());
            }
            ps.executeBatch();

            // get generated keys and map productId to goods receipt item id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                int i = 0;
                while (rs.next()) {
                    // get generated id of goods receipt item
                    Long generatedId = rs.getLong(1);
                    // map productId to goods receipt item id
                    productIdGoodsReceiptItemId.put(productIds.get(i), generatedId);
                    i++;
                }
            }
        }
        return productIdGoodsReceiptItemId;
    }

    // insert product item to table product_items
    private void insertProductItems(List<ProductItemDTO> productItemDTOs, Map<Long, Long> receiptItemIds)
            throws SQLException {
        String sql = "insert into product_items(serial, imported_price, current_price, is_active, imported_at, updated_at, product_id, goods_receipt_item_id) values (?, ?, ?, ?, NOW(), NOW(), ?, ?)";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // for each productItems to get data
            for (ProductItemDTO item : productItemDTOs) {
                int index = 1;
                ps.setString(index++, item.getSerial());
                ps.setDouble(index++, item.getImportPrice());
                ps.setDouble(index++, item.getImportPrice());
                ps.setBoolean(index++, true);
                ps.setLong(index++, item.getProductId());

                // get goods receipt item id correspond productId
                Long goodsReceiptItemId = receiptItemIds.get(item.getProductId());
                if (goodsReceiptItemId != null) {
                    ps.setLong(index, goodsReceiptItemId);
                }

                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // update status purchase request from approve -> completed in table
    // purchase_requests
    private void completePurchaseRequest(Long purchaseRequestId) throws SQLException {
        String sql = "update purchase_requests set status = 'COMPLETED', updated_at = NOW() where id = ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // set value
            if (purchaseRequestId != null) {
                ps.setLong(1, purchaseRequestId);
            }
            ps.executeUpdate();
        }
    }

    public boolean isExistSerial(String serial) {
        String sql = "SELECT 1 FROM product_items pt WHERE pt.serial = ?";

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, serial);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<OrderDTO> searchExportOrders(String name, LocalDate fromDate, LocalDate toDate, String status,
                                             int offset) {
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
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

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
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM orders o WHERE 1=1 and o.status not in ('DRAFT', 'COMPLETED', 'CANCELLED') ");
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

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

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

    // get information of pending export order by order id
    public ExportDTO getPendingOrder(Long orderId) {
        String sql = """
                  select o.id, o.order_code, o.customer_name, o.total_price, o.note, o.order_date,
                  o.processed_at, creater.fullname as salesman_name
                  from orders o
                  join users creater on creater.id = o.created_by
                  where o.id = ?;
                """;
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExportDTO dto = new ExportDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setOrderCode(rs.getString("order_code"));
                    dto.setCustomerName(rs.getString("customer_name"));
                    dto.setTotal(rs.getDouble("total_price"));
                    dto.setSalesmanName(rs.getString("salesman_name"));
                    dto.setCreatedAt(rs.getTimestamp("order_date").toLocalDateTime());
                    dto.setNote(rs.getString("note"));

                    // get list item of export order
                    dto.setItems(getPendingExportItems(orderId));
                    return dto;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get pending export order for order ID: " + orderId, e);
        }
        return null;
    }

    // get list item of pending export order by order id
    public List<ExportProductDTO> getPendingExportItems(Long orderId) {
        String sql = """
                select oi.id as item_id, oi.quantity, p.name, u.name as unit
                from order_items oi
                join products p on p.id = oi.product_id
                left join units u on u.id = p.unit_id
                where oi.order_id = ?;
                """;
        List<ExportProductDTO> list = new ArrayList<>();
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExportProductDTO item = new ExportProductDTO();
                    item.setId(rs.getLong("item_id"));
                    item.setProductName(rs.getString("name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnit(rs.getString("unit"));
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get pending export items for order ID: " + orderId, e);
        }
        return list;
    }

    public void executeExport(Long orderId, Long warehouseKeeperId, Map<Long, List<String>> orderItemSerialsMap) {
        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);
            try {
                assignSerialsToOrderItems(con, orderItemSerialsMap);
                subtractInventory(con, orderItemSerialsMap);
                saveStockMovements(con, orderId, orderItemSerialsMap);
                updateOrderStatus(con, orderId, warehouseKeeperId);
                con.commit();
            } catch (Exception e) {
                con.rollback();
                if (e instanceof IllegalArgumentException) {
                    throw (IllegalArgumentException) e;
                }
                throw new RuntimeException("Transaction failed during export order", e);
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during export transaction", e);
        }
    }

    public void updateOrderStatus(Connection con, Long orderId, Long warehouseKeeperId) throws SQLException {
        String sql = "UPDATE orders SET status = 'COMPLETED', processed_by = ?, processed_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, warehouseKeeperId);
            ps.setLong(2, orderId);
            ps.executeUpdate();
        }
    }

    // assign order item correspond serial and check validate serial
    private void assignSerialsToOrderItems(Connection con, Map<Long, List<String>> orderItemSerialsMap) throws SQLException {
        for (Map.Entry<Long, List<String>> entry : orderItemSerialsMap.entrySet()) {
            Long orderItemId = entry.getKey();
            for (String serial : entry.getValue()) {
                Long productItemId = getProductItemIdBySerial(con, serial);
                if (productItemId == null) {
                    throw new IllegalArgumentException("Serial " + serial + " does not exist or inactive");
                }
                insertOrderItemProductItem(con, orderItemId, productItemId);
            }
        }
    }

    // get product item id by serial
    private Long getProductItemIdBySerial(Connection con, String serial) throws SQLException {
        String sql = "SELECT id FROM product_items WHERE serial = ? AND is_active = 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, serial);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return null;
    }

    // insert data to table order_item_product_items to assign product item correspond order item
    private void insertOrderItemProductItem(Connection con, Long orderItemId, Long productItemId) throws SQLException {
        String sql = "INSERT INTO order_item_product_items (order_item_id, product_item_id) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderItemId);
            ps.setLong(2, productItemId);
            ps.executeUpdate();
        }
    }

    // subtract quantity of product in inventory
    private void subtractInventory(Connection con, Map<Long, List<String>> orderItemSerialsMap) throws SQLException {
        String sql = "UPDATE product_items SET is_active = 0, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (List<String> serials : orderItemSerialsMap.values()) {
                for (String serial : serials) {
                    Long productItemId = getProductItemIdBySerial(con, serial);
                    if (productItemId != null) {
                        ps.setLong(1, productItemId);
                        ps.addBatch();
                    }
                }
            }
            ps.executeBatch();
        }
    }

    private void saveStockMovements(Connection con, Long id, Map<Long, List<String>> orderItemSerialsMap) throws SQLException {
        String queryProductId = "SELECT product_id FROM order_items WHERE id = ?";
        String moveSql = "INSERT INTO stock_movements (product_id, quantity, type, reference_type, created_at, reference_id) VALUES (?, ?, ?, ?, NOW(), ?)";
        String updateQuantity = "UPDATE products SET total_quantity = total_quantity - ? WHERE id = ?";
        
        try (PreparedStatement psRead = con.prepareStatement(queryProductId);
             PreparedStatement psMove = con.prepareStatement(moveSql);
             PreparedStatement psUpdate = con.prepareStatement(updateQuantity)) {
             
            for (Map.Entry<Long, List<String>> entry : orderItemSerialsMap.entrySet()) {
                Long orderItemId = entry.getKey();
                int quantity = entry.getValue().size();

                Long productId = null;
                psRead.setLong(1, orderItemId);
                try (ResultSet rs = psRead.executeQuery()) {
                    if (rs.next()) {
                        productId = rs.getLong("product_id");
                    }
                }
                
                if (productId != null) {
                    // Save stock movement
                    psMove.setLong(1, productId);
                    psMove.setInt(2, quantity);
                    psMove.setString(3, MovementType.EXPORT.name());
                    psMove.setString(4, ReferenceType.ORDER.name());
                    psMove.setLong(5, id);
                    psMove.addBatch();
                    
                    // Decrease product total quantity
                    psUpdate.setInt(1, quantity);
                    psUpdate.setLong(2, productId);
                    psUpdate.addBatch();
                }
            }
            psMove.executeBatch();
            psUpdate.executeBatch();
        }
    }
}