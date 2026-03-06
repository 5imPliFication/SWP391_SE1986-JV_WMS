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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderItemDAO {

    public void addItem(OrderItem item) {
        String selectAvailableItemsSql = "SELECT id, current_price, serial FROM product_items " +
                "WHERE product_id = ? AND is_active = 1 ORDER BY id LIMIT ?";
        String insertOrderItemSql = "INSERT INTO order_items (order_id, quantity, price_at_purchase) VALUES (?, ?, ?)";
        String insertLinkSql = "INSERT INTO order_item_product_items (order_item_id, product_item_id) VALUES (?, ?)";

        Long productId = item.getProduct() != null ? item.getProduct().getId() : null;
        if (productId == null && item.getProductItem() != null) {
            productId = item.getProductItem().getProductId();
        }

        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required to add order item");
        }

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);

            try {
                List<ProductItem> selectedProductItems = new ArrayList<>();
                BigDecimal priceAtPurchase = null;

                try (PreparedStatement ps = con.prepareStatement(selectAvailableItemsSql)) {
                    ps.setLong(1, productId);
                    ps.setInt(2, item.getQuantity());
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            ProductItem productItem = new ProductItem();
                            productItem.setId(rs.getLong("id"));
                            productItem.setProductId(productId);
                            productItem.setCurrentPrice(rs.getDouble("current_price"));
                            productItem.setSerial(rs.getString("serial"));
                            selectedProductItems.add(productItem);
                            if (priceAtPurchase == null) {
                                priceAtPurchase = rs.getBigDecimal("current_price");
                            }
                        }
                    }
                }

                if (selectedProductItems.size() < item.getQuantity()) {
                    throw new IllegalStateException("Insufficient stock. Available: " + selectedProductItems.size());
                }

                Long orderItemId;
                try (PreparedStatement ps = con.prepareStatement(insertOrderItemSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, item.getOrder().getId());
                    ps.setInt(2, item.getQuantity());
                    ps.setBigDecimal(3, priceAtPurchase);
                    ps.executeUpdate();

                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new SQLException("Failed to create order item record");
                        }
                        orderItemId = generatedKeys.getLong(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(insertLinkSql)) {
                    for (ProductItem productItem : selectedProductItems) {
                        ps.setLong(1, orderItemId);
                        ps.setLong(2, productItem.getId());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                item.setId(orderItemId);
                item.setProductItems(selectedProductItems);
                if (!selectedProductItems.isEmpty()) {
                    item.setProductItem(selectedProductItems.get(0));
                }
                item.setPriceAtPurchase(priceAtPurchase);

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Add order item failed", e);
        }
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = """
                    SELECT oi.id, oi.quantity, oi.price_at_purchase,
                           pi.id AS product_item_id, pi.current_price, pi.serial AS product_item_serial,
                           p.id AS product_id, p.name AS product_name, p.description
                    FROM order_items oi
                    JOIN order_item_product_items oipi ON oi.id = oipi.order_item_id
                    JOIN product_items pi ON oipi.product_item_id = pi.id
                    JOIN products p ON pi.product_id = p.id
                    WHERE oi.order_id = ?
                    ORDER BY oi.id, pi.id
                """;

        Map<Long, OrderItem> itemMap = new LinkedHashMap<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Long orderItemId = rs.getLong("id");

                    OrderItem item = itemMap.get(orderItemId);
                    if (item == null) {
                        Product product = new Product();
                        product.setId(rs.getLong("product_id"));
                        product.setName(rs.getString("product_name"));
                        product.setDescription(rs.getString("description"));

                        item = new OrderItem();
                        item.setId(orderItemId);
                        item.setProduct(product);
                        item.setQuantity(rs.getInt("quantity"));
                        item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                        item.setProductItems(new ArrayList<>());
                        itemMap.put(orderItemId, item);
                    }

                    ProductItem linkedProductItem = new ProductItem();
                    linkedProductItem.setId(rs.getLong("product_item_id"));
                    linkedProductItem.setSerial(rs.getString("product_item_serial"));
                    linkedProductItem.setCurrentPrice(rs.getDouble("current_price"));
                    linkedProductItem.setProductId(rs.getLong("product_id"));

                    item.getProductItems().add(linkedProductItem);
                    if (item.getProductItem() == null) {
                        item.setProductItem(linkedProductItem);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order items for order ID: " + orderId, e);
        }

        return new ArrayList<>(itemMap.values());
    }

    public boolean deleteByOrderItemId(Long orderId, Long orderItemId) {
        String deleteLinksSql = "DELETE FROM order_item_product_items WHERE order_item_id = ?";
        String deleteItemSql = "DELETE FROM order_items WHERE id = ? AND order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(deleteLinksSql)) {
                    ps.setLong(1, orderItemId);
                    ps.executeUpdate();
                }

                int affected;
                try (PreparedStatement ps = con.prepareStatement(deleteItemSql)) {
                    ps.setLong(1, orderItemId);
                    ps.setLong(2, orderId);
                    affected = ps.executeUpdate();
                }

                con.commit();
                return affected > 0;
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Delete order item failed", e);
        }
    }

    public OrderItem findByIdAndOrderId(Long orderId, Long orderItemId) {
        String sql = "SELECT id, quantity, price_at_purchase FROM order_items WHERE id = ? AND order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderItemId);
            ps.setLong(2, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
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
        String selectCurrentSql = """
                    SELECT oi.quantity, MIN(pi.product_id) AS product_id
                    FROM order_items oi
                    JOIN order_item_product_items oipi ON oi.id = oipi.order_item_id
                    JOIN product_items pi ON oipi.product_item_id = pi.id
                    WHERE oi.id = ?
                    GROUP BY oi.quantity
                """;
        String selectAdditionalItemsSql = """
                    SELECT id FROM product_items
                    WHERE product_id = ? AND is_active = 1
                      AND id NOT IN (
                        SELECT product_item_id FROM order_item_product_items WHERE order_item_id = ?
                      )
                    ORDER BY id
                    LIMIT ?
                """;
        String selectLinksToRemoveSql = "SELECT product_item_id FROM order_item_product_items WHERE order_item_id = ? ORDER BY product_item_id DESC LIMIT ?";
        String insertLinkSql = "INSERT INTO order_item_product_items (order_item_id, product_item_id) VALUES (?, ?)";
        String deleteLinkSql = "DELETE FROM order_item_product_items WHERE order_item_id = ? AND product_item_id = ?";
        String updateQuantitySql = "UPDATE order_items SET quantity = ? WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);

            try {
                int currentQuantity;
                Long productId;

                try (PreparedStatement ps = con.prepareStatement(selectCurrentSql)) {
                    ps.setLong(1, itemId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new IllegalArgumentException("Order item not found with ID: " + itemId);
                        }
                        currentQuantity = rs.getInt("quantity");
                        productId = rs.getLong("product_id");
                    }
                }

                if (quantity > currentQuantity) {
                    int needed = quantity - currentQuantity;
                    List<Long> additionalProductItemIds = new ArrayList<>();

                    try (PreparedStatement ps = con.prepareStatement(selectAdditionalItemsSql)) {
                        ps.setLong(1, productId);
                        ps.setLong(2, itemId);
                        ps.setInt(3, needed);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                additionalProductItemIds.add(rs.getLong("id"));
                            }
                        }
                    }

                    if (additionalProductItemIds.size() < needed) {
                        throw new IllegalStateException("Insufficient stock. Available to add: " + additionalProductItemIds.size());
                    }

                    try (PreparedStatement ps = con.prepareStatement(insertLinkSql)) {
                        for (Long productItemId : additionalProductItemIds) {
                            ps.setLong(1, itemId);
                            ps.setLong(2, productItemId);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                } else if (quantity < currentQuantity) {
                    int toRemove = currentQuantity - quantity;
                    List<Long> removableLinkIds = new ArrayList<>();

                    try (PreparedStatement ps = con.prepareStatement(selectLinksToRemoveSql)) {
                        ps.setLong(1, itemId);
                        ps.setInt(2, toRemove);
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                removableLinkIds.add(rs.getLong("product_item_id"));
                            }
                        }
                    }

                    if (removableLinkIds.size() < toRemove) {
                        throw new IllegalStateException("Not enough linked product items to reduce quantity");
                    }

                    try (PreparedStatement ps = con.prepareStatement(deleteLinkSql)) {
                        for (Long productItemId : removableLinkIds) {
                            ps.setLong(1, itemId);
                            ps.setLong(2, productItemId);
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(updateQuantitySql)) {
                    ps.setInt(1, quantity);
                    ps.setLong(2, itemId);
                    ps.executeUpdate();
                }

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update quantity", e);
        }
    }

    public void deleteByOrderId(Long orderId) {
        String deleteLinksSql = "DELETE oipi FROM order_item_product_items oipi " +
                "JOIN order_items oi ON oi.id = oipi.order_item_id WHERE oi.order_id = ?";
        String deleteItemsSql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(deleteLinksSql)) {
                    ps.setLong(1, orderId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(deleteItemsSql)) {
                    ps.setLong(1, orderId);
                    ps.executeUpdate();
                }

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order items", e);
        }
    }

    public List<OrderItemDTO> findOrderItemsByOrderId(Long orderId) {
        String sql = """
                    SELECT grouped.product_id, SUM(grouped.quantity) AS quantity
                    FROM (
                        SELECT oi.id, oi.quantity, MIN(pi.product_id) AS product_id
                        FROM order_items oi
                        JOIN order_item_product_items oipi ON oi.id = oipi.order_item_id
                        JOIN product_items pi ON oipi.product_item_id = pi.id
                        WHERE oi.order_id = ?
                        GROUP BY oi.id, oi.quantity
                    ) grouped
                    GROUP BY grouped.product_id
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
}
