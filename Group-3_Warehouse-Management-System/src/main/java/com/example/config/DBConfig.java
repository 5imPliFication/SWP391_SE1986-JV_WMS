package com.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DBConfig {

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        // JDBC URL
        config.setJdbcUrl(
                "jdbc:mysql://localhost:3306/laptop_wms" +
                        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );

        config.setUsername("root");
        config.setPassword("1234");

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
}
