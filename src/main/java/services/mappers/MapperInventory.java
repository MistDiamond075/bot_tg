package services.mappers;

import entity.EntityInventory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapperInventory implements BaseMapper<EntityInventory>{
    @Override
    public EntityInventory toEntity(ResultSet rs) throws SQLException {
        return new EntityInventory(
                rs.getLong("id"),
                rs.getLong("userId"),
                rs.getLong("itemId"),
                rs.getLong("amount")
        );
    }

    @Override
    public List<EntityInventory> toEntityList(ResultSet rs) throws SQLException {
        List<EntityInventory> list = new ArrayList<>();
        while(rs.next()){
            list.add(toEntity(rs));
        }
        return list;
    }
}
