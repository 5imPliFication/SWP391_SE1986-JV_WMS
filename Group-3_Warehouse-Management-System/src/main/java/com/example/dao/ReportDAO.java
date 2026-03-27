package com.example.dao;

import com.example.config.DBConfig;
import com.example.dto.ExportProductOrderDTO;
import com.example.dto.InventoryMovementRowDTO;
import com.example.dto.ReportItemDTO;
import com.example.enums.MovementType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public long getOpeningStockBeforeYear(int year) {
        String sql = """
                SELECT COALESCE(SUM(
                    CASE
                        WHEN type = 'IMPORT' THEN quantity
                        WHEN type = 'EXPORT' THEN -quantity
                        ELSE 0
                    END
                ), 0) AS opening_stock
                FROM stock_movements
                WHERE created_at < ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(year + "-01-01 00:00:00"));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("opening_stock");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get opening stock before year " + year, e);
        }

        return 0;
    }

    // get quantity by month
    public List<Long> getMovementChartDataByYear(int year, MovementType movementType) {
        // init list with 0 for 12 months
        List<Long> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(0L);
        }

        String sql = """
                SELECT MONTH(created_at) AS month, SUM(quantity) AS quantity
                FROM stock_movements
                WHERE YEAR(created_at) = ?
                AND type = ?
                GROUP BY MONTH(created_at)
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);
            ps.setString(2, movementType.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    long quantity = rs.getLong("quantity");
                    data.set(month - 1, quantity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get movement chart data", e);
        }

        return data;
    }

    // Get list of items for export report
    public List<ReportItemDTO> getItems(int month, int year, MovementType movementType) {
        List<ReportItemDTO> items = new ArrayList<>();
        String sql = """
                SELECT p.name, SUM(sm.quantity) as quantity FROM stock_movements sm
                JOIN products p ON sm.product_id = p.id
                WHERE sm.type = ? AND YEAR(sm.created_at) = ? AND MONTH(sm.created_at) = ?
                GROUP BY p.name;
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movementType.name());
            ps.setInt(2, year);
            ps.setInt(3, month);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String productName = rs.getString("name");
                    long quantity = rs.getLong("quantity");
                    items.add(new ReportItemDTO(productName, quantity));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Long> getInventoryChartDataByYear(int year) {
        List<Long> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(0L);
        }

        // 1. Get initial balance before the year starts
        long currentBalance = 0;
        String initialBalanceSql = """
                SELECT SUM(CASE WHEN type = 'IMPORT' THEN quantity ELSE -quantity END) as initial_balance
                FROM stock_movements
                WHERE YEAR(created_at) < ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(initialBalanceSql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentBalance = rs.getLong("initial_balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2. Get monthly net changes for the year
        String monthlyChangesSql = """
                SELECT MONTH(created_at) as month, SUM(CASE WHEN type = 'IMPORT' THEN quantity ELSE -quantity END) as net_change
                FROM stock_movements
                WHERE YEAR(created_at) = ?
                GROUP BY MONTH(created_at)
                ORDER BY MONTH(created_at)
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(monthlyChangesSql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    long netChange = rs.getLong("net_change");
                    // Inventory at end of month = previous balance + current net change

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Let's refine the logic to better handle months with no movements
        long[] netChanges = new long[13]; // 1-12
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(monthlyChangesSql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    netChanges[rs.getInt("month")] = rs.getLong("net_change");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int m = 1; m <= 12; m++) {
            currentBalance += netChanges[m];
            data.set(m - 1, currentBalance);
        }

        return data;
    }

    public List<ReportItemDTO> getInventoryItems(int month, int year) {
        List<ReportItemDTO> items = new ArrayList<>();
        String sql = """
                SELECT p.name, SUM(CASE WHEN sm.type = 'IMPORT' THEN sm.quantity ELSE -sm.quantity END) as quantity
                FROM stock_movements sm
                JOIN products p ON sm.product_id = p.id
                WHERE YEAR(sm.created_at) < ? OR (YEAR(sm.created_at) = ? AND MONTH(sm.created_at) <= ?)
                GROUP BY p.name
                HAVING quantity > 0
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, year);
            ps.setInt(3, month);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new ReportItemDTO(rs.getString("name"), rs.getLong("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<ReportItemDTO> getExportItems(int month, int year) {
        List<ReportItemDTO> items = new ArrayList<>();
        String sql = """
                SELECT p.id AS product_id, p.name AS product_name, COALESCE(SUM(sm.quantity), 0) AS quantity
                FROM stock_movements sm
                JOIN products p ON sm.product_id = p.id
                WHERE sm.type = 'EXPORT'
                  AND YEAR(sm.created_at) = ?
                  AND MONTH(sm.created_at) = ?
                GROUP BY p.id, p.name
                ORDER BY quantity DESC, p.name ASC
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new ReportItemDTO(
                            rs.getLong("product_id"),
                            rs.getString("product_name"),
                            rs.getLong("quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get export items", e);
        }

        return items;
    }

    public int countExportOrdersByProduct(Long productId, int month, int year) {
        String sql = """
                SELECT COUNT(DISTINCT o.id) AS total
                FROM orders o
                JOIN order_items oi ON oi.order_id = o.id
                WHERE o.status = 'COMPLETED'
                  AND oi.product_id = ?
                  AND YEAR(o.processed_at) = ?
                  AND MONTH(o.processed_at) = ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.setInt(2, year);
            ps.setInt(3, month);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count export orders by product", e);
        }

        return 0;
    }

    public List<ExportProductOrderDTO> getExportOrdersByProduct(Long productId, int month, int year, int offset, int limit) {
        List<ExportProductOrderDTO> orders = new ArrayList<>();
        String sql = """
                SELECT
                    o.id AS order_id,
                    o.order_code,
                    o.customer_name,
                    o.processed_at,
                    u.fullname AS processed_by_name,
                    SUM(oi.quantity) AS product_quantity
                FROM orders o
                JOIN order_items oi ON oi.order_id = o.id
                LEFT JOIN users u ON u.id = o.processed_by
                WHERE o.status = 'COMPLETED'
                  AND oi.product_id = ?
                  AND YEAR(o.processed_at) = ?
                  AND MONTH(o.processed_at) = ?
                GROUP BY o.id, o.order_code, o.customer_name, o.processed_at, u.fullname
                ORDER BY o.processed_at DESC
                                LIMIT ? OFFSET ?
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.setInt(2, year);
            ps.setInt(3, month);
                        ps.setInt(4, limit);
                        ps.setInt(5, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new ExportProductOrderDTO(
                            rs.getLong("order_id"),
                            rs.getString("order_code"),
                            rs.getString("customer_name"),
                            rs.getTimestamp("processed_at"),
                            rs.getString("processed_by_name"),
                            rs.getLong("product_quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get export orders by product", e);
        }

        return orders;
    }

    public List<ExportProductOrderDTO> getExportOrdersByProduct(Long productId, int month, int year) {
        return getExportOrdersByProduct(productId, month, year, 0, Integer.MAX_VALUE);
    }

    public long getImportByMonth(int month, int year) {
        String sql = """
                SELECT SUM(quantity) as total FROM stock_movements
                WHERE type = 'IMPORT' AND YEAR(created_at) = ? AND MONTH(created_at) = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getExportByMonth(int month, int year) {
        String sql = """
                SELECT SUM(quantity) as total FROM stock_movements
                WHERE type = 'EXPORT' AND YEAR(created_at) = ? AND MONTH(created_at) = ?
                """;
        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, month);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<InventoryMovementRowDTO> getInventoryMovementRows(int month, int year) {
        LocalDate fromDate = LocalDate.of(year, month, 1);
        LocalDate toDate = fromDate.plusMonths(1).minusDays(1);
        return getInventoryMovementRows(fromDate, toDate);
    }

    public List<InventoryMovementRowDTO> getInventoryMovementRows(LocalDate fromDate, LocalDate toDate) {
        List<InventoryMovementRowDTO> rows = new ArrayList<>();

        LocalDate toDateExclusive = toDate.plusDays(1);

        String sql = """
                SELECT
                    p.id AS product_id,
                    p.name AS product_name,
                    COALESCE(opening_stats.opening_qty, 0) AS opening_qty,
                    COALESCE(import_stats.import_qty, 0) AS import_qty,
                    COALESCE(export_stats.export_qty, 0) AS export_qty,
                    COALESCE(closing_stats.closing_qty, 0) AS closing_qty
                FROM products p
                LEFT JOIN (
                    SELECT
                        product_id,
                        COUNT(*) AS opening_qty
                    FROM (
                        SELECT
                            pi.id,
                            pi.product_id,
                            pi.imported_at,
                            exported_items.exported_at
                        FROM product_items pi
                        LEFT JOIN (
                            SELECT
                                oipi.product_item_id,
                                MIN(o.processed_at) AS exported_at
                            FROM order_item_product_items oipi
                            JOIN order_items oi ON oi.id = oipi.order_item_id
                            JOIN orders o ON o.id = oi.order_id
                            WHERE o.status = 'COMPLETED'
                              AND o.processed_at IS NOT NULL
                            GROUP BY oipi.product_item_id
                        ) exported_items ON exported_items.product_item_id = pi.id
                    ) opening_items
                    WHERE imported_at < ?
                      AND (exported_at IS NULL OR exported_at >= ?)
                    GROUP BY product_id
                ) opening_stats ON opening_stats.product_id = p.id
                LEFT JOIN (
                    SELECT
                        product_id,
                        COUNT(*) AS import_qty
                    FROM product_items
                    WHERE imported_at >= ?
                      AND imported_at < ?
                    GROUP BY product_id
                ) import_stats ON import_stats.product_id = p.id
                LEFT JOIN (
                    SELECT
                        pi.product_id,
                        COUNT(*) AS export_qty
                    FROM product_items pi
                    JOIN (
                        SELECT
                            oipi.product_item_id,
                            MIN(o.processed_at) AS exported_at
                        FROM order_item_product_items oipi
                        JOIN order_items oi ON oi.id = oipi.order_item_id
                        JOIN orders o ON o.id = oi.order_id
                        WHERE o.status = 'COMPLETED'
                          AND o.processed_at IS NOT NULL
                        GROUP BY oipi.product_item_id
                    ) exported_items ON exported_items.product_item_id = pi.id
                    WHERE exported_items.exported_at >= ?
                      AND exported_items.exported_at < ?
                    GROUP BY pi.product_id
                ) export_stats ON export_stats.product_id = p.id
                LEFT JOIN (
                    SELECT
                        product_id,
                        COUNT(*) AS closing_qty
                    FROM (
                        SELECT
                            pi.id,
                            pi.product_id,
                            pi.imported_at,
                            exported_items.exported_at
                        FROM product_items pi
                        LEFT JOIN (
                            SELECT
                                oipi.product_item_id,
                                MIN(o.processed_at) AS exported_at
                            FROM order_item_product_items oipi
                            JOIN order_items oi ON oi.id = oipi.order_item_id
                            JOIN orders o ON o.id = oi.order_id
                            WHERE o.status = 'COMPLETED'
                              AND o.processed_at IS NOT NULL
                            GROUP BY oipi.product_item_id
                        ) exported_items ON exported_items.product_item_id = pi.id
                    ) closing_items
                    WHERE imported_at < ?
                      AND (exported_at IS NULL OR exported_at >= ?)
                    GROUP BY product_id
                ) closing_stats ON closing_stats.product_id = p.id
                WHERE COALESCE(opening_stats.opening_qty, 0) <> 0
                   OR COALESCE(import_stats.import_qty, 0) <> 0
                   OR COALESCE(export_stats.export_qty, 0) <> 0
                ORDER BY p.id ASC
                """;

        try (Connection conn = DBConfig.getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Timestamp fromTs = Timestamp.valueOf(fromDate.atStartOfDay());
            Timestamp toTs = Timestamp.valueOf(toDateExclusive.atStartOfDay());

            ps.setTimestamp(1, fromTs);
            ps.setTimestamp(2, fromTs);
            ps.setTimestamp(3, fromTs);
            ps.setTimestamp(4, toTs);
            ps.setTimestamp(5, fromTs);
            ps.setTimestamp(6, toTs);
            ps.setTimestamp(7, toTs);
            ps.setTimestamp(8, toTs);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long openingQty = rs.getLong("opening_qty");
                    long importQty = rs.getLong("import_qty");
                    long exportQty = rs.getLong("export_qty");
                    long closingQty = rs.getLong("closing_qty");

                    rows.add(new InventoryMovementRowDTO(
                            rs.getLong("product_id"),
                            rs.getString("product_name"),
                            openingQty,
                            importQty,
                            exportQty,
                            closingQty
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get inventory movement rows", e);
        }

        return rows;
    }


}
