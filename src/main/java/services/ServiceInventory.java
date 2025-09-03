package services;

import entity.EntityInventory;
import exception.DatabaseException;
import services.mappers.MapperInventory;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ServiceInventory {
    private static final Logger log = Logger.getLogger(ServiceInventory.class.getName());
    private final MapperInventory mapperInventory=new MapperInventory();

    public void updateInventory(Long userId, String[] roll) throws DatabaseException {
        String inv_to_add;
        log.info("updating user's inventory with user id "+userId+": "+ Arrays.toString(roll));
        String sql="select * from inventory where userId=?";
        try(Connection conn=ServiceDatabase.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData rsmd = rs.getMetaData();
                conn.commit();
            }catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
        catch (SQLException e) {throw new DatabaseException(e,EntityInventory.class.getSimpleName());}
    }

    public List<EntityInventory> getInventory(Long userId) throws DatabaseException {
        String sql = "select * from inventory where userId=?";
        try (Connection conn = ServiceDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            return mapperInventory.toEntityList(rs);
        }
        catch (SQLException e) {throw new DatabaseException(e,EntityInventory.class.getSimpleName());}
    }
}
