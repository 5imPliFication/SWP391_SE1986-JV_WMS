package com.example.dao;

import com.example.config.DBConfig;
import com.example.model.Product;
import com.example.model.ProductItem;
import com.example.model.Role;
import com.example.model.User;
import com.example.util.UserConstant;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // get list product by name
    public List<Product> findProductByName(String searchName) {
        List<Product> listProducts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select id, `name`, description from products as p " +
                " where 1 = 1 ");

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
                listProducts.add(new Product(id, name, description));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listProducts;
    }

    // save list products item to db
    public boolean saveProductItems(List<ProductItem> productItems) {
        StringBuilder sql = new StringBuilder(
                "insert into product_items(serial, import_price, import_date, is_active, product_id) values (?, ?, ?, ?, ?);");

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (ProductItem item : productItems) {
                ps.setString(1, item.getSerial());
                ps.setDouble(2, item.getImportPrice());
                ps.setTimestamp(3, Timestamp.valueOf(item.getImportDate()));
                ps.setBoolean(4, true);
                ps.setLong(5, item.getProductId());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String findProductNameById(String id) {
        String name = "";
        StringBuilder sql = new StringBuilder("select name from products as p " +
                "where 1 = 1 ");

        // if param has value of searchName
        if (id != null) {
            sql.append(" and p.id = ? ");
        }

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // if searchName has value -> set value to query
            if (id != null) {
                ps.setString(1, "%" + id + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                name = rs.getString("name");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return name;
    }

    public boolean isExistSerial(String serial) {
        StringBuilder sql = new StringBuilder("select serial from product_items as pt " +
                "where 1 = 1 ");

        // if param has value of searchName
        if (serial != null && !serial.trim().isEmpty()) {
            sql.append(" and pt.serial like ? ");
        }

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

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
}
