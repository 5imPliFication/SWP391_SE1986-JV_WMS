package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConfig {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        // JDBC URL
        config.setJdbcUrl(
                "jdbc:mysql://localhost:3306/laptop_wms" +
                        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );

        config.setUsername("root");
        config.setPassword("root@1234");

        // MySQL Driver
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Pool settings (sane defaults)
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setMaxLifetime(1800000);

        dataSource = new HikariDataSource(config);
    }

    private DBConfig() {}

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void testConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(0)) {
                System.out.println("Connected to the database");
            } else {
                System.out.println("Cannot connect to the database");
            }
        }
    }
}

