package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.OrderDTO;
import com.example.dto.ProductDTO;
import com.example.dto.ProductItemDTO;
import com.example.util.AppConstants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // get list product by name
    public List<ProductDTO> findProductByName(String searchName) {
        List<ProductDTO> listProducts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select id, name, description, total_quantity from products as p " +
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
                long totalQuantity = rs.getLong("total_quantity");
                listProducts.add(new ProductDTO(id, name, description, totalQuantity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listProducts;
    }

    // save list products item to db
    public boolean saveProductItems(List<ProductItemDTO> productItemDTOs) {
        String sql = "INSERT INTO product_items(serial, imported_price, current_price, is_active, imported_at, updated_at, product_id) "
                +
                "VALUES (?, ?, ?, ?, NOW(), NOW(), ?)";

        // access data
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // iterate each item
            for (ProductItemDTO item : productItemDTOs) {

                // set data
                int index = 1;
                ps.setString(index++, item.getSerial());
                ps.setDouble(index++, item.getImportPrice());
                ps.setDouble(index++, item.getImportPrice());
                ps.setBoolean(index++, true);
                ps.setLong(index++, item.getProductId());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public List<OrderDTO> searchExportOrders(String name, LocalDate fromDate, LocalDate toDate, int offset) {
        StringBuilder sql = new StringBuilder("""
                    select o.id, o.order_code, o.order_date, u.fullname as salesman_name, o.customer_name, o.status
                    from orders o
                    join users u ON o.created_by = u.id
                    where 1=1 and o.status not in ('SUBMITTED', 'COMPLETED', 'CANCELLED') 
                """);

        // name
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" and o.customer_name like ? ");
        }

        // handle date
        if (fromDate != null)
            sql.append(" and CAST(o.order_date AS DATE) >= ?");
        if (toDate != null)
            sql.append(" and CAST(o.order_date AS DATE) <= ?");

        // pagination
        sql.append(" ORDER BY o.order_date DESC LIMIT ? OFFSET ?");

        List<OrderDTO> list = new ArrayList<>();
        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (name != null && !name.trim().isEmpty()) {
                ps.setString(index++, "%" + name + "%");
            }
            if (fromDate != null)
                ps.setDate(index++, Date.valueOf(fromDate));
            if (toDate != null)
                ps.setDate(index++, Date.valueOf(toDate));
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
    public int countExportOrders(String name, LocalDate fromDate, LocalDate toDate) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM orders o WHERE 1=1 ");
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" and o.customer_name like ? ");
        }
        if (fromDate != null) {
            sql.append(" and cast(o.order_date AS DATE) >= ?");
        }
        if (toDate != null) {
            sql.append(" and cast(o.order_date AS DATE) <= ?");
        }

        try (Connection con = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if(name != null && !name.trim().isEmpty()) {
                ps.setString(index++, "%" + name + "%");
            }
            if (fromDate != null)
                ps.setDate(index++, Date.valueOf(fromDate));
            if (toDate != null)
                ps.setDate(index++, Date.valueOf(toDate));

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Count fail", e);
        }
        return 0;
    }
}
