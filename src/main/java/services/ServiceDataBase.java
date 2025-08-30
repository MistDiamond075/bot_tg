package services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import configuration.ConfApp;

import java.sql.*;

public class ServiceDataBase {
    private final HikariConfig config = new HikariConfig();
    private final HikariDataSource ds;

    public ServiceDataBase(){
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setJdbcUrl(ConfApp.get("db.url"));
        config.setUsername(ConfApp.get("db.user"));
        config.setPassword(ConfApp.get("db.password"));
        ds = new HikariDataSource(config);
    }

    public Connection getConnection(){
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
