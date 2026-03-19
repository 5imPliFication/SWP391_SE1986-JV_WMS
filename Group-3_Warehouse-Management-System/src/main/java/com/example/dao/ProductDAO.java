package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.*;
import com.example.util.AppConstants;
import com.example.dto.ProductDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    protected LocalDateTime getLocalDateTime(ResultSet rs, String column)
            throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return ts == null ? null : ts.toLocalDateTime();
    }

    public List<Product> getAll(String searchName, Long brandId, Long categoryId, Long modelId, Long chipId,
                                Long ramId, Long storageId, Long sizeId, Boolean isActive, int pageNo)
    {

        List<Product> products = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                    SELECT p.id, p.name, p.description, p.img_url, p.is_active, p.total_quantity, p.created_at, p.updated_at,
                              	b.id AS brand_id, b.name AS brand_name,
                              	c.id AS category_id, c.name AS category_name,
                                m.id AS model_id, m.name AS model_name,
                                ch.id AS chip_id, ch.name AS chip_name,
                                r.id AS ram_id, r.size AS ram_size,
                                s.id AS storage_id, s.size AS storage_size,
                                sz.id AS screen_id, sz.size AS screen_size,
                                u.id AS unit_id, u.name AS unit_name
                      FROM products p
                      JOIN brands b ON p.brand_id = b.id
                      JOIN categories c ON p.category_id = c.id
                      JOIN models m ON p.model_id = m.id
                      JOIN chips ch ON p.chip_id = ch.id
                      JOIN rams r ON p.ram_id = r.id
                      JOIN storages s ON p.storage_id = s.id
                      JOIN sizes sz ON p.size_id = sz.id
                      JOIN units u on p.unit_id = u.id
                      WHERE 1=1
                """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND p.name LIKE ? ");
        }
        if (brandId != null) {
            sql.append(" AND b.id = ? ");
        }
        if (categoryId != null) {
            sql.append(" AND c.id = ? ");
        }
        if (modelId != null) {
            sql.append(" AND m.id = ? ");
        }
        if (chipId != null) {
            sql.append(" AND ch.id = ? ");
        }
        if (ramId != null) {
            sql.append(" AND r.id = ? ");
        }
        if (storageId != null) {
            sql.append(" AND s.id = ? ");
        }
        if (sizeId != null) {
            sql.append(" AND sz.id = ? ");
        }
        if (isActive != null) {
            sql.append(" AND p.is_active = ? ");
        }

        sql.append(" ORDER BY p.created_at DESC ");
        sql.append(" LIMIT ? OFFSET ? ");

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (brandId != null) {
                ps.setLong(index++, brandId);
            }
            if (categoryId != null) {
                ps.setLong(index++, categoryId);
            }
            if (modelId != null) {
                ps.setLong(index++, modelId);
            }
            if (chipId != null) {
                ps.setLong(index++, chipId);
            }
            if (ramId != null) {
                ps.setLong(index++, ramId);
            }
            if (storageId != null) {
                ps.setLong(index++, storageId);
            }
            if (sizeId != null) {
                ps.setLong(index++, sizeId);
            }
            if (isActive != null) {
                ps.setBoolean(index++, isActive);
            }

            ps.setInt(index++, AppConstants.PAGE_SIZE);
            ps.setInt(index++, (pageNo - 1) * AppConstants.PAGE_SIZE);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setImgUrl(rs.getString("img_url"));
                product.setIsActive(rs.getBoolean("is_active"));
                product.setTotalQuantity(rs.getLong("total_quantity"));

                // SAFE timestamp
                product.setCreatedAt(getLocalDateTime(rs, "created_at"));
                product.setUpdatedAt(getLocalDateTime(rs, "updated_at"));

                Brand brand = new Brand();
                brand.setId(rs.getLong("brand_id"));
                brand.setName(rs.getString("brand_name"));
                product.setBrand(brand);

                Category category = new Category();
                category.setId(rs.getLong("category_id"));
                category.setName(rs.getString("category_name"));
                product.setCategory(category);

                Model model = new Model();
                model.setId(rs.getLong("model_id"));
                model.setName(rs.getString("model_name"));
                product.setModel(model);

                Chip chip = new Chip();
                chip.setId(rs.getLong("chip_id"));
                chip.setName(rs.getString("chip_name"));
                product.setChip(chip);

                Ram ram = new Ram();
                ram.setId(rs.getLong("ram_id"));
                ram.setSize(rs.getString("ram_size"));
                product.setRam(ram);

                Storage storage = new Storage();
                storage.setId(rs.getLong("storage_id"));
                storage.setSize(rs.getString("storage_size"));
                product.setStorage(storage);

                Size size = new Size();
                size.setId(rs.getLong("screen_id"));
                size.setSize(rs.getString("screen_size"));
                product.setSize(size);

                Unit unit = new Unit();
                unit.setId(rs.getLong("unit_id"));
                unit.setName(rs.getString("unit_name"));
                product.setUnit(unit);

                products.add(product);
            }

            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countProducts(String searchName, Long brandId, Long categoryId, Long modelId, Long chipId,
                             Long ramId, Long storageId, Long sizeId, Boolean isActive) {

        StringBuilder sql = new StringBuilder("""
                    SELECT COUNT(*)
                    FROM products p
                      JOIN brands b ON p.brand_id = b.id
                      JOIN categories c ON p.category_id = c.id
                      JOIN models m ON p.model_id = m.id
                      JOIN chips ch ON p.chip_id = ch.id
                      JOIN rams r ON p.ram_id = r.id
                      JOIN storages s ON p.storage_id = s.id
                      JOIN sizes sz ON p.size_id = sz.id
                      JOIN units u on p.unit_id = u.id
                      WHERE 1=1
                """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND p.name LIKE ? ");
        }
        if (brandId != null) {
            sql.append(" AND b.id = ? ");
        }
        if (categoryId != null) {
            sql.append(" AND c.id = ? ");
        }
        if (modelId != null) {
            sql.append(" AND m.id = ? ");
        }
        if (chipId != null) {
            sql.append(" AND ch.id = ? ");
        }
        if (ramId != null) {
            sql.append(" AND r.id = ? ");
        }
        if (storageId != null) {
            sql.append(" AND s.id = ? ");
        }
        if (sizeId != null) {
            sql.append(" AND sz.id = ? ");
        }
        if (isActive != null) {
            sql.append(" AND p.is_active = ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (searchName != null && !searchName.trim().isEmpty()) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (brandId != null) {
                ps.setLong(index++, brandId);
            }
            if (categoryId != null) {
                ps.setLong(index++, categoryId);
            }
            if (modelId != null) {
                ps.setLong(index++, modelId);
            }
            if (chipId != null) {
                ps.setLong(index++, chipId);
            }
            if (ramId != null) {
                ps.setLong(index++, ramId);
            }
            if (storageId != null) {
                ps.setLong(index++, storageId);
            }
            if (sizeId != null) {
                ps.setLong(index++, sizeId);
            }
            if (isActive != null) {
                ps.setBoolean(index++, isActive);
            }

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Create new product
    public boolean create(Product product) {
        String sql = """
                INSERT INTO products (name, description, img_url, brand_id, category_id, model_id, chip_id, ram_id, storage_id, size_id, unit_id ,is_active, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, NOW(), NOW())
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImgUrl());
            ps.setLong(4, product.getBrand().getId());
            ps.setLong(5, product.getCategory().getId());
            ps.setLong(6, product.getModel().getId());
            ps.setLong(7, product.getChip().getId());
            ps.setLong(8, product.getRam().getId());
            ps.setLong(9, product.getStorage().getId());
            ps.setLong(10, product.getSize().getId());
            ps.setLong(11, product.getUnit().getId());

            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product findById(long productId) {
        String sql = """
                SELECT p.id AS product_id, p.name AS product_name, p.description, p.img_url, p.is_active, p.total_quantity,
                            b.name AS brand_name, b.id AS brand_id,
                            c.name AS category_name, c.id AS category_id,
                              m.name AS model_name, m.id AS model_id,
                              ch.name AS chip_name, ch.id AS chip_id,
                              r.size AS ram_size, r.id AS ram_id,
                              s.size AS storage_size, s.id AS storage_id,
                              sz.size AS screen_size, sz.id AS size_id,
                              u.name AS unit_name, u.id AS unit_id,
                            p.created_at, p.updated_at
                  FROM products p
                  JOIN brands b ON p.brand_id = b.id
                  JOIN categories c ON p.category_id = c.id
                  JOIN models m ON p.model_id = m.id
                  JOIN chips ch ON p.chip_id = ch.id
                  JOIN rams r ON p.ram_id = r.id
                  JOIN storages s ON p.storage_id = s.id
                  JOIN sizes sz ON p.size_id = sz.id
                  JOIN units u on p.unit_id = u.id
                WHERE p.id = ?;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

                Model model = new Model();
                model.setId(rs.getLong("model_id"));
                model.setName(rs.getString("model_name"));
                product.setModel(model);

                Chip chip = new Chip();
                chip.setId(rs.getLong("chip_id"));
                chip.setName(rs.getString("chip_name"));
                product.setChip(chip);

                Ram ram = new Ram();
                ram.setId(rs.getLong("ram_id"));
                ram.setSize(rs.getString("ram_size"));
                product.setRam(ram);

                Storage storage = new Storage();
                storage.setId(rs.getLong("storage_id"));
                storage.setSize(rs.getString("storage_size"));
                product.setStorage(storage);

                Size size = new Size();
                size.setId(rs.getLong("size_id"));
                size.setSize(rs.getString("screen_size"));
                product.setSize(size);

                Unit unit = new Unit();
                unit.setId(rs.getLong("unit_id"));
                unit.setName(rs.getString("unit_name"));
                product.setUnit(unit);

                // SAFE timestamp
                product.setCreatedAt(getLocalDateTime(rs, "created_at"));
                product.setUpdatedAt(getLocalDateTime(rs, "updated_at"));
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
                    model_id = ?,
                    chip_id = ?,
                    ram_id = ?,
                    storage_id = ?,
                    size_id = ?,
                    unit_id = ?,
                    is_active = ?,
                    updated_at = NOW()
                WHERE id = ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImgUrl());
            ps.setLong(4, product.getBrand().getId());
            ps.setLong(5, product.getCategory().getId());
            ps.setLong(6, product.getModel().getId());
            ps.setLong(7, product.getChip().getId());
            ps.setLong(8, product.getRam().getId());
            ps.setLong(9, product.getStorage().getId());
            ps.setLong(10, product.getSize().getId());
            ps.setLong(11, product.getUnit().getId());
            ps.setBoolean(12, product.getIsActive());
            ps.setLong(13, product.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductItem> getItemsByProductId(long productId, String searchSerial, Boolean isActive, int pageNo) {
        List<ProductItem> productItems = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                """
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
            ps.setInt(index++, AppConstants.PAGE_SIZE);

            int offset = (pageNo - 1) * AppConstants.PAGE_SIZE;
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
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, productItem.getCurrentPrice());
            ps.setBoolean(2, productItem.getIsActive());
            ps.setLong(3, productItem.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductDTO> getLowStockProducts(String name, int offset) {
        List<ProductDTO> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select * from products where total_quantity < 10 ");

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" and name like ? ");
        }

        // pagination
        sql.append(" ORDER BY total_quantity asc LIMIT ? OFFSET ?");

        int index = 1;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            // set value
            if (name != null && !name.trim().isEmpty()) {
                ps.setString(index++, "%" + name + "%");
            }

            ps.setInt(index++, AppConstants.PAGE_SIZE);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                product.setTotalQuantity(rs.getLong("total_quantity"));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public int countTotalProducts(String name) {
        StringBuilder sql = new StringBuilder("select count(*) from products where total_quantity < 10 ");

        if (name != null && !name.trim().isEmpty()) {
            sql.append(" and name like ? ");
        }

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // set value
            if (name != null && !name.trim().isEmpty()) {
                ps.setString(1, "%" + name + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public List<Product> getAllProducts() {
        String sql = """
                SELECT p.id,
                       p.name,
                       p.description,
                       p.img_url,
                       p.is_active,
                       p.brand_id,
                       COUNT(pi.id) AS active_item_count
                FROM products p
                JOIN product_items pi ON pi.product_id = p.id AND pi.is_active = 1
                GROUP BY p.id, p.name, p.description, p.img_url, p.is_active, p.brand_id
                ORDER BY p.name ASC
                """;
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getLong("id"));
                product.setName(rs.getString("name"));
                // In order screens, availability is based on active product items only.
                product.setTotalQuantity(rs.getLong("active_item_count"));
                product.setIsActive(rs.getBoolean("is_active"));
                product.setImgUrl(rs.getString("img_url"));
                Brand brand = new Brand();
                brand.setId(rs.getLong("brand_id"));
                product.setDescription(rs.getString("description"));
                product.setBrand(brand);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public int countAvailableProductsForOrder() {
        String sql = """
                SELECT COUNT(*)
                FROM (
                    SELECT p.id
                    FROM products p
                    JOIN product_items pi ON pi.product_id = p.id AND pi.is_active = 1
                    GROUP BY p.id
                ) grouped
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAvailableProductsForOrder(int pageNo, int pageSize) {
        String sql = """
                SELECT p.id,
                       p.name,
                       p.description,
                       p.img_url,
                       p.is_active,
                       p.brand_id,
                       COUNT(pi.id) AS active_item_count
                FROM products p
                JOIN product_items pi ON pi.product_id = p.id AND pi.is_active = 1
                GROUP BY p.id, p.name, p.description, p.img_url, p.is_active, p.brand_id
                ORDER BY p.name ASC
                LIMIT ? OFFSET ?
                """;

        List<Product> products = new ArrayList<>();
        int safePageNo = Math.max(pageNo, 1);
        int offset = (safePageNo - 1) * pageSize;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setTotalQuantity(rs.getLong("active_item_count"));
                    product.setIsActive(rs.getBoolean("is_active"));
                    product.setImgUrl(rs.getString("img_url"));
                    Brand brand = new Brand();
                    brand.setId(rs.getLong("brand_id"));
                    product.setDescription(rs.getString("description"));
                    product.setBrand(brand);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int countLowStock(int threshold) {
        String sql = "SELECT COUNT(*) FROM products WHERE total_quantity <= ?";
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public List<Product> findLowStock(int threshold) {
        String sql = "SELECT * FROM products WHERE total_quantity <= ? ORDER BY total_quantity ASC";
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getLong("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setImgUrl(rs.getString("img_url"));
                    product.setIsActive(rs.getBoolean("is_active"));
                    product.setTotalQuantity(rs.getLong("total_quantity"));

                    Brand brand = new Brand();
                    brand.setId(rs.getLong("brand_id"));
                    product.setBrand(brand);

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public boolean existedByName(String productName) {
        String sql = """
                SELECT *
                FROM products p
                WHERE p.name = ?;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productName);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}