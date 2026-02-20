package com.example.dao;

import com.example.config.DBConfig;
import com.example.enums.InventoryAuditStatus;
import com.example.model.*;
import com.example.util.UserConstant;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryAuditDAO {

    protected LocalDateTime getLocalDateTime(ResultSet rs, String column)
            throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts == null ? null : ts.toLocalDateTime();
    }

    public List<InventoryAudit> getAll(String auditCode, String status, int pageNo) {

        List<InventoryAudit> inventoryAudits = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT i.id, i.audit_code, u.id as user_id, u.fullname, i.status, i.created_at, i.updated_at
            FROM inventory_audits i
            JOIN users u ON i.created_by = u.id
            WHERE 1=1
        """);

        if (auditCode != null && !auditCode.trim().isEmpty()) {
            sql.append(" AND i.audit_code LIKE ? ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }

        sql.append(" ORDER BY i.created_at DESC ");
        sql.append(" LIMIT ? OFFSET ? ");

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (auditCode != null && !auditCode.trim().isEmpty()) {
                ps.setString(index++, "%" + auditCode + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ps.setInt(index++, UserConstant.PAGE_SIZE);
            ps.setInt(index++, (pageNo - 1) * UserConstant.PAGE_SIZE);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InventoryAudit inventoryAudit = new InventoryAudit();
                inventoryAudit.setId(rs.getLong("id"));
                inventoryAudit.setAuditCode(rs.getString("audit_code"));
                inventoryAudit.setStatus(InventoryAuditStatus.valueOf(rs.getString("status")));
                inventoryAudit.setCreatedAt(getLocalDateTime(rs, "created_at"));
                inventoryAudit.setUpdatedAt(getLocalDateTime(rs, "updated_at"));

                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setFullName(rs.getString("fullname"));
                inventoryAudit.setUser(user);

                inventoryAudits.add(inventoryAudit);
            }

            return inventoryAudits;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countInventoryAudits(String auditCode, String status) {

        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*)
            FROM inventory_audits i
            WHERE 1=1
        """);

        if (auditCode != null && !auditCode.trim().isEmpty()) {
            sql.append(" AND i.audit_code LIKE ? ");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND b.name = ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (auditCode != null && !auditCode.trim().isEmpty()) {
                ps.setString(index++, "%" + auditCode + "%");
            }
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(index++, status);
            }

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    // Create new product
    public boolean create(Product product) {
        String sql = """
                INSERT INTO products (name, description, img_url, brand_id, category_id, is_active, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, 1, NOW(), NOW())
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImgUrl());
            ps.setLong(4, product.getBrand().getId());
            ps.setLong(5, product.getCategory().getId());

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product findById(long productId) {
        String sql = """
                SELECT p.id as product_id, p.name as product_name, p.description, p.img_url, p.is_active, p.total_quantity,
                       b.id as brand_id, b.name as brand_name,
                       c.id as category_id, c.name as category_name
                FROM products p
                JOIN brands b on p.brand_id = b.id
                JOIN categories c on p.category_id = c.id
                WHERE p.id = ?;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            Product product = null;

            ps.setLong(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = new Product();

                product.setId(rs.getLong("product_id"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setImgUrl(rs.getString("img_url"));
                product.setIsActive(rs.getBoolean("is_active"));
                product.setTotalQuantity(rs.getLong("total_quantity"));

                Brand brand = new Brand();
                brand.setId(rs.getLong("brand_id"));
                brand.setName(rs.getString("brand_name"));
                product.setBrand(brand);

                Category category = new Category();
                category.setId(rs.getLong("category_id"));
                category.setName(rs.getString("category_name"));
                product.setCategory(category);
            }

            return product;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Product product) {
        String sql = """
                UPDATE products
                SET name = ?,
                    description = ?,
                    img_url = ?,
                    brand_id = ?,
                    category_id = ?,
                    is_active = ?,
                    updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImgUrl());
            ps.setLong(4, product.getBrand().getId());
            ps.setLong(5, product.getCategory().getId());
            ps.setBoolean(6, product.getIsActive());
            ps.setLong(7, product.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductItem> getItemsByProductId(long productId, String searchSerial, Boolean isActive, int pageNo) {
        List<ProductItem> productItems = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                    SELECT pi.id, pi.serial, pi.imported_price, pi.current_price, pi.imported_at, pi.updated_at, pi.is_active, pi.product_id
                    FROM product_items pi
                    WHERE 1 = 1
                """);
        // filter by productId
        sql.append(" AND pi.product_id = ? ");

        // search by serial
        if (searchSerial != null && !searchSerial.trim().isEmpty()) {
            sql.append(" AND pi.serial LIKE ? ");
        }
        // filter by active status
        if (isActive != null) {
            sql.append(" AND pi.is_active = ? ");
        }

        // filter by date created
        sql.append(" ORDER BY pi.imported_at DESC ");

        // handle pagination
        sql.append(" limit ? offset ? ");

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setLong(index++, productId);

            if (searchSerial != null && !searchSerial.trim().isEmpty()) {
                ps.setString(index++, "%" + searchSerial + "%");
            }
            if (isActive != null) {
                ps.setBoolean(index++, isActive);
            }
            // set value for pagination of SQL
            ps.setInt(index++, UserConstant.PAGE_SIZE);

            int offset = (pageNo - 1) * UserConstant.PAGE_SIZE;
            ps.setInt(index++, offset);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductItem productItem = new ProductItem();
                productItem.setId(rs.getLong("id"));
                productItem.setSerial(rs.getString("serial"));
                productItem.setImportedPrice(rs.getDouble("imported_price"));
                productItem.setCurrentPrice(rs.getDouble("current_price"));
                productItem.setImportedAt(rs.getTimestamp("imported_at").toLocalDateTime());
                productItem.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                productItem.setIsActive(rs.getBoolean("is_active"));
                productItem.setProductId(rs.getLong("product_id"));

                productItems.add(productItem);
            }
            return productItems;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countProductItems(long productId, String searchSerial, Boolean isActive) {
        int totalProductItems = 0;
        StringBuilder sql = new StringBuilder("""
                    SELECT count(*)
                    FROM product_items pi
                    WHERE 1 = 1
                """);
        // filter by productId
        sql.append(" AND pi.product_id = ? ");

        // search by serial
        if (searchSerial != null && !searchSerial.trim().isEmpty()) {
            sql.append(" AND pi.serial LIKE ? ");
        }
        // filter by active status
        if (isActive != null) {
            sql.append(" AND pi.is_active = ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setLong(index++, productId);

            if (searchSerial != null && !searchSerial.trim().isEmpty()) {
                ps.setString(index++, "%" + searchSerial + "%");
            }
            if (isActive != null) {
                ps.setBoolean(index++, isActive);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalProductItems = rs.getInt(1);
            }
            return totalProductItems;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductItem findItemById(long productItemId) {
        String sql = """
                SELECT *
                FROM product_items pi
                WHERE pi.id = ?;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ProductItem productItem = null;

            ps.setLong(1, productItemId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                productItem = new ProductItem();

                productItem.setId(rs.getLong("id"));
                productItem.setSerial(rs.getString("serial"));
                productItem.setImportedPrice(rs.getDouble("imported_price"));
                productItem.setCurrentPrice(rs.getDouble("current_price"));
                productItem.setImportedAt(rs.getTimestamp("imported_at").toLocalDateTime());
                productItem.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                productItem.setIsActive(rs.getBoolean("is_active"));
                productItem.setProductId(rs.getLong("product_id"));

            }

            return productItem;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateItem(ProductItem productItem) {
        String sql = """
                UPDATE product_items
                SET current_price = ?,
                    is_active = ?,
                    updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, productItem.getCurrentPrice());
            ps.setBoolean(2, productItem.getIsActive());
            ps.setLong(3, productItem.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     */
}