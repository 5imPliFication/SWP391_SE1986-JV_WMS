package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.OrderItemDTO;
import com.example.model.OrderItem;
import com.example.model.Product;
import com.example.model.ProductItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public void addItem(OrderItem item) {
    String insertOrderItemSql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES (?, ?, ?, ?)";

        Long productId = item.getProduct() != null ? item.getProduct().getId() : null;
        if (productId == null && item.getProductItem() != null) {
            productId = item.getProductItem().getProductId();
        }

        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required to add order item");
        }

        if (item.getOrder() == null || item.getOrder().getId() == null) {
            throw new IllegalArgumentException("Order ID is required to add order item");
        }

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        BigDecimal priceAtPurchase = item.getPriceAtPurchase();
        if (priceAtPurchase == null && item.getProduct() != null && item.getProduct().getCurrentPrice() != null) {
            priceAtPurchase = BigDecimal.valueOf(item.getProduct().getCurrentPrice());
        }

        if (priceAtPurchase == null) {
            throw new IllegalArgumentException("Price at purchase is required to add order item");
        }

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            Long orderItemId;
            try (PreparedStatement ps = con.prepareStatement(insertOrderItemSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, item.getOrder().getId());
                ps.setLong(2, productId);
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, priceAtPurchase);
                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("Failed to create order item record");
                    }
                    orderItemId = generatedKeys.getLong(1);
                }
            }

            item.setId(orderItemId);
            if (item.getProduct() == null) {
                Product product = new Product();
                product.setId(productId);
                item.setProduct(product);
            }
            item.setProductItems(new ArrayList<>());
            ProductItem displayItem = new ProductItem();
            displayItem.setProductId(productId);
            displayItem.setImportedPrice(priceAtPurchase.doubleValue());
            item.setProductItem(displayItem);
            item.setPriceAtPurchase(priceAtPurchase);
        } catch (SQLException e) {
            throw new RuntimeException("Add order item failed", e);
        }
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = """
                    SELECT oi.id, oi.quantity, oi.price_at_purchase,
                           p.id AS product_id, p.name AS product_name, p.description
                    FROM order_items oi
                    JOIN products p ON oi.product_id = p.id
                    WHERE oi.order_id = ?
                    ORDER BY oi.id
                """;

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);

            List<OrderItem> items = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    Long productId = rs.getLong("product_id");
                    product.setId(productId);
                    product.setName(rs.getString("product_name"));
                    product.setDescription(rs.getString("description"));

                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                    item.setProductItems(new ArrayList<>());

                    ProductItem displayItem = new ProductItem();
                    displayItem.setProductId(productId);
                    if (item.getPriceAtPurchase() != null) {
                        displayItem.setImportedPrice(item.getPriceAtPurchase().doubleValue());
                    }
                    item.setProductItem(displayItem);

                    items.add(item);
                }
            }

            return items;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order items for order ID: " + orderId, e);
        }
    }

    public int countByOrderId(Long orderId) {
        String sql = "SELECT COUNT(*) FROM order_items WHERE order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count order items for order ID: " + orderId, e);
        }
    }

    public List<OrderItem> findByOrderId(Long orderId, int offset, int limit) {
        String idSql = "SELECT id FROM order_items WHERE order_id = ? ORDER BY id LIMIT ? OFFSET ?";
        List<Long> pageItemIds = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(idSql)) {
            ps.setLong(1, orderId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pageItemIds.add(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paged order item IDs", e);
        }

        if (pageItemIds.isEmpty()) {
            return new ArrayList<>();
        }

        StringBuilder detailSql = new StringBuilder(
            "SELECT oi.id, oi.quantity, oi.price_at_purchase, " +
                "p.id AS product_id, p.name AS product_name, p.description " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ? AND oi.id IN ("
        );

        for (int i = 0; i < pageItemIds.size(); i++) {
            if (i > 0) {
                detailSql.append(",");
            }
            detailSql.append("?");
        }
        detailSql.append(") ORDER BY oi.id");

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(detailSql.toString())) {

            int paramIndex = 1;
            ps.setLong(paramIndex++, orderId);
            for (Long itemId : pageItemIds) {
                ps.setLong(paramIndex++, itemId);
            }

            List<OrderItem> items = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    Long productId = rs.getLong("product_id");
                    product.setId(productId);
                    product.setName(rs.getString("product_name"));
                    product.setDescription(rs.getString("description"));

                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                    item.setProductItems(new ArrayList<>());

                    ProductItem displayItem = new ProductItem();
                    displayItem.setProductId(productId);
                    if (item.getPriceAtPurchase() != null) {
                        displayItem.setImportedPrice(item.getPriceAtPurchase().doubleValue());
                    }
                    item.setProductItem(displayItem);

                    items.add(item);
                }
            }

            return items;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch paged order items for order ID: " + orderId, e);
        }
    }

    public boolean deleteByOrderItemId(Long orderId, Long orderItemId) {
        String deleteItemSql = "DELETE FROM order_items WHERE id = ? AND order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(deleteItemSql)) {
            ps.setLong(1, orderItemId);
            ps.setLong(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Delete order item failed", e);
        }
    }

    public OrderItem findByIdAndOrderId(Long orderId, Long orderItemId) {
        String sql = "SELECT id, product_id, quantity, price_at_purchase FROM order_items WHERE id = ? AND order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderItemId);
            ps.setLong(2, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    Product product = new Product();
                    product.setId(rs.getLong("product_id"));
                    item.setProduct(product);
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                    return item;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order item", e);
        }

        return null;
    }

    public void updateQuantity(Long itemId, int quantity) {
        String selectCurrentSql = "SELECT product_id FROM order_items WHERE id = ?";
        String countAvailableSql = "SELECT COUNT(*) FROM product_items WHERE product_id = ? AND is_active = 1";
        String updateQuantitySql = "UPDATE order_items SET quantity = ? WHERE id = ?";

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            Long productId;
            try (PreparedStatement ps = con.prepareStatement(selectCurrentSql)) {
                ps.setLong(1, itemId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Order item not found with ID: " + itemId);
                    }
                    productId = rs.getLong("product_id");
                }
            }

            int available;
            try (PreparedStatement ps = con.prepareStatement(countAvailableSql)) {
                ps.setLong(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    available = rs.getInt(1);
                }
            }

            if (quantity > available) {
                throw new IllegalStateException("Insufficient stock. Available: " + available + ", Requested: " + quantity);
            }

            try (PreparedStatement ps = con.prepareStatement(updateQuantitySql)) {
                ps.setInt(1, quantity);
                ps.setLong(2, itemId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update quantity", e);
        }
    }

    public void deleteByOrderId(Long orderId) {
        String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(deleteItemsSql)) {
            ps.setLong(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order items", e);
        }
    }

    public List<OrderItemDTO> findOrderItemsByOrderId(Long orderId) {
        String sql = """
                    SELECT oi.product_id, SUM(oi.quantity) AS quantity
                    FROM order_items oi
                    WHERE oi.order_id = ?
                    GROUP BY oi.product_id
                """;
        List<OrderItemDTO> items = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new OrderItemDTO(
                            rs.getLong("product_id"),
                            rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order item DTOs", e);
        }
        return items;
    }

    public void activateReservedItemsByOrderId(Long orderId) {
        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);
            try {
                List<Long> linkedItemIds = findLinkedProductItemIdsByOrder(con, orderId);
                setProductItemsActiveState(con, linkedItemIds, true, false, null);

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to release reserved product items for order ID: " + orderId, e);
        }
    }

    private List<Long> findLinkedProductItemIdsByOrderItem(Connection con, Long orderId, Long orderItemId) throws SQLException {
        String sql = "SELECT oipi.product_item_id FROM order_item_product_items oipi " +
                "JOIN order_items oi ON oi.id = oipi.order_item_id WHERE oi.order_id = ? AND oi.id = ?";

        List<Long> linkedItemIds = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, orderItemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    linkedItemIds.add(rs.getLong("product_item_id"));
                }
            }
        }
        return linkedItemIds;
    }

    private List<Long> findLinkedProductItemIdsByOrder(Connection con, Long orderId) throws SQLException {
        String sql = "SELECT oipi.product_item_id FROM order_item_product_items oipi " +
                "JOIN order_items oi ON oi.id = oipi.order_item_id WHERE oi.order_id = ?";

        List<Long> linkedItemIds = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    linkedItemIds.add(rs.getLong("product_item_id"));
                }
            }
        }
        return linkedItemIds;
    }

    private void setProductItemsActiveState(Connection con,
                                            List<Long> itemIds,
                                            boolean active,
                                            boolean strictCount,
                                            String strictCountErrorMessage) throws SQLException {
        if (itemIds == null || itemIds.isEmpty()) {
            return;
        }

        String sql = active
                ? "UPDATE product_items SET is_active = 1, updated_at = NOW() WHERE id = ? AND is_active = 0"
                : "UPDATE product_items SET is_active = 0, updated_at = NOW() WHERE id = ? AND is_active = 1";

        int updated = 0;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Long itemId : itemIds) {
                ps.setLong(1, itemId);
                updated += ps.executeUpdate();
            }
        }

        if (strictCount && updated < itemIds.size()) {
            throw new IllegalStateException(strictCountErrorMessage != null
                    ? strictCountErrorMessage
                    : "Failed to update product item state");
        }
    }
}
