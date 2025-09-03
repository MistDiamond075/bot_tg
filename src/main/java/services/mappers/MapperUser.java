package services.mappers;

import entity.EntityUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapperUser implements BaseMapper<EntityUser> {
    @Override
    public EntityUser toEntity(ResultSet rs) throws SQLException {
        return new EntityUser(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("userId"),
                rs.getLong("chatId")
        );
    }

    @Override
    public List<EntityUser> toEntityList(ResultSet rs) throws SQLException {
        List<EntityUser> entityUserList= new ArrayList<>();
        while(rs.next()) {
            entityUserList.add(toEntity(rs));
        }
        return entityUserList;
    }
}
