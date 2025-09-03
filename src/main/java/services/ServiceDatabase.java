package services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import configuration.ConfApp;
import exception.DatabaseException;

import java.sql.*;

public class ServiceDatabase {
    private final HikariDataSource ds;

    private ServiceDatabase(){
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setJdbcUrl(ConfApp.get("db.url"));
        config.setUsername(ConfApp.get("db.user"));
        config.setPassword(ConfApp.get("db.password"));
        config.setAutoCommit(false);
        ds = new HikariDataSource(config);
    }

    private static class ServiceDatabaseHolder{
        private static final ServiceDatabase INSTANCE=new ServiceDatabase();
    }

    public Connection getConnection() throws DatabaseException {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static ServiceDatabase getInstance(){
        return ServiceDatabaseHolder.INSTANCE;
    }
}
