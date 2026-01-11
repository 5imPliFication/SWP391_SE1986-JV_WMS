package com.example.dao;

import com.example.model.Permission;
import com.example.model.Role;
import java.sql.*;
import java.util.*;

public class RoleDAO {

    private final Connection conn;

    public RoleDAO(Connection conn) {
        this.conn = conn;
    }

    // 12. View role list
    public List<Role> findAll() throws SQLException {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM roles";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Role(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at"),
                    rs.getObject("permission", List.class)
            ));
        }
        return list;
    }

    // 13. View role detail
    public Role findById(String id) throws SQLException {
        String sql = "SELECT * FROM roles WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Role(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBoolean("is_active"),
                    rs.getTimestamp("created_at"),
                    rs.getObject("permission", List.class)
            );
        }
        return null;
    }

    // 14. Update role info
    public void update(Role role) throws SQLException {
        String sql = "UPDATE roles SET name=?, description=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, role.getName());
        ps.setString(2, role.getDescription());
        ps.setLong(3, role.getId());
        ps.executeUpdate();
    }

    // 15. Active / Deactivate
    public void changeStatus(String id, boolean active) throws SQLException {
        String sql = "UPDATE roles SET is_active=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setBoolean(1, active);
        ps.setString(2, id);
        ps.executeUpdate();
    }
}
