package com.example.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (DBConfig.getDataSource() instanceof HikariDataSource ds) {
            ds.close();
        }
    }
}
