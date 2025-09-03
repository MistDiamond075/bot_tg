package services;

import configuration.ConfApp;
import entity.EntityUserProperties;
import exception.DatabaseException;
import exception.PropertyException;
import services.mappers.MapperUserProperties;
import utils.Pair;
import utils.Parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ServiceUserProperties {
    private static final Logger log = Logger.getLogger(ServiceUserProperties.class.getName());
    private final Map<String, String> property_type = new ConcurrentHashMap<>();

    public ServiceUserProperties() {
        loadProperties();
    }

    public EntityUserProperties<?> getProperty(String name, Long userId) throws DatabaseException {
        if (property_type.containsKey(name)) {
            try {
                Class<?> cl = Class.forName(property_type.get(name));
                final MapperUserProperties<?> mapperUserProperties = new MapperUserProperties<>(cl);
                String sql = "select * from user_properties where user_id=? and name=?";
                try (Connection conn = ServiceDatabase.getInstance().getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setLong(1, userId);
                    stmt.setString(2, name);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        return addProperty(new EntityUserProperties<>(null, userId, name, null, cl));
                    }
                    return mapperUserProperties.toEntity(rs);
                } catch (SQLException e) {
                    throw new DatabaseException(e);
                }
            } catch (ClassNotFoundException e) {
                throw new DatabaseException("Wrong property type " + e);
            }
        }
        throw new DatabaseException("No such property " + name);
    }

    private EntityUserProperties<?> addProperty(EntityUserProperties<?> entityUserProperties) throws DatabaseException {
        String sql = "insert into user_properties(userId,name,value,type) values(?,?,?,?)";
        try (Connection conn = ServiceDatabase.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, entityUserProperties.getUserId());
                stmt.setString(2, entityUserProperties.getName());
                stmt.setString(3, String.valueOf(entityUserProperties.getValue()));
                stmt.setObject(4, entityUserProperties.getType());
                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                log.severe("addProperty transaction rollback for " + entityUserProperties);
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return entityUserProperties;
    }

    private EntityUserProperties<?> updateProperty(EntityUserProperties<?> entityUserProperties) throws DatabaseException {
        String sql = "update user_properties set value=? where user_id=? and name=?";
        try (Connection conn = ServiceDatabase.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, String.valueOf(entityUserProperties.getValue()));
                stmt.setLong(2, entityUserProperties.getUserId());
                stmt.setString(3, entityUserProperties.getName());
                stmt.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                log.severe("updateProperty transaction rollback for " + entityUserProperties);
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return entityUserProperties;
    }

    private void loadProperties() {
        String configPropertiesRaw = ConfApp.get("db.table.user.properties.names");
        try {
            List<Pair<String, String>> propertiesList = Parser.parseAppProperties(configPropertiesRaw);
            for (Pair<String, String> pair : propertiesList) {
                try {
                    Class.forName(pair.second);
                } catch (ClassNotFoundException e) {
                    log.warning("Configuration property " + pair.second + " type " + pair.second + " is wrong. Property will be skipped");
                    continue;
                }
                property_type.put(pair.first, pair.second);
            }
        } catch (PropertyException e) {
            log.severe("property parsing error: " + e.getMessage());
            return;
        }
    }
}
