package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.Product;
import com.example.model.ProductItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public void addItem(OrderItem item) {
        String checkStockSql = "SELECT pi.current_price, p.total_quantity AS quantity FROM product_items pi JOIN products p " +
                "ON pi.product_id=p.id" +
                " WHERE pi.id = ?";
        String insertSql = "INSERT INTO order_items (order_id, product_item_id, quantity, price_at_purchase) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConfig.getDataSource().getConnection()) {

            con.setAutoCommit(false);  // Start transaction

            try {
                // 1. Get product item price and check stock FIRST
                BigDecimal currentPrice = null;
                Integer availableQuantity = null;

                try (PreparedStatement ps = con.prepareStatement(checkStockSql)) {
                    ps.setLong(1, item.getProductItem().getId());  // Use product_item_id
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        currentPrice = rs.getBigDecimal("current_price");
                        availableQuantity = rs.getInt("quantity");
                    } else {
                        throw new IllegalArgumentException("Product item not found");
                    }
                }

                // 2. Check if enough stock is available
                if (availableQuantity < item.getQuantity()) {
                    throw new IllegalStateException("Insufficient stock. Available: " + availableQuantity);
                }

                // 3. Insert order item with price snapshot
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setLong(1, item.getOrder().getId());
                    ps.setLong(2, item.getProductItem().getId());  // product_item_id
                    ps.setInt(3, item.getQuantity());
                    ps.setBigDecimal(4, currentPrice);  // Store price_at_purchase
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
            throw new RuntimeException("Add order item failed", e);
        }
    }

    /**
     * Find all items for an order with product details
     */
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = """
                    SELECT oi.id, oi.quantity, oi.price_at_purchase,
                           pi.id AS product_item_id, pi.current_price, pi.serial AS product_item_serial,
                           p.id AS product_id, p.name AS product_name, p.description
                    FROM order_items oi
                    JOIN product_items pi ON oi.product_item_id = pi.id
                    JOIN products p ON pi.product_id = p.id
                    WHERE oi.order_id = ?
                    ORDER BY oi.id
                """;

        List<OrderItem> items = new ArrayList<>();

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Create Product
                    Product product = new Product();
                    product.setId(rs.getLong("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setDescription(rs.getString("description"));

                    // Create ProductItem
                    ProductItem productItem = new ProductItem();
                    productItem.setId(rs.getLong("product_item_id"));
                    productItem.setSerial(rs.getString("product_item_serial"));
                    productItem.setCurrentPrice(rs.getDouble("current_price"));
                    productItem.setProductId(product.getId());

                    // Create OrderItem
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setProductItem(productItem);
                    item.setProduct(product);  // For convenience in JSP
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order items for order ID: " + orderId, e);
        }

        return items;
    }

    /**
     * Delete item from order by product_item_id
     */
    public boolean deleteByOrderAndProductItem(Long orderId, Long productItemId) {
        String sql = "DELETE FROM order_items WHERE order_id = ? AND product_item_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ps.setLong(2, productItemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Delete order item failed", e);
        }
    }

    /**
     * Find specific order item by order and product_item
     */
    public OrderItem findByOrderAndProductItem(Long orderId, Long productItemId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ? AND product_item_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ps.setLong(2, productItemId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getLong("id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
                return item;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find order item", e);
        }

        return null;
    }

    /**
     * Update quantity for an order item
     */
    public void updateQuantity(Long itemId, int quantity) {
        String sql = "UPDATE order_items SET quantity = ? WHERE id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setLong(2, itemId);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new IllegalArgumentException("Order item not found with ID: " + itemId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update quantity", e);
        }
    }

    /**
     * Delete all items for an order (useful when canceling order)
     */
    public void deleteByOrderId(Long orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order items", e);
        }
    }
}
