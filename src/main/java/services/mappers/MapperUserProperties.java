package services.mappers;

import entity.EntityUserProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapperUserProperties<T> implements BaseMapper<EntityUserProperties<T>> {
    private final Class<T> entityClass;

    public MapperUserProperties(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public EntityUserProperties<T> toEntity(ResultSet rs) throws SQLException {
        EntityUserProperties<T> entityUserProperties = new EntityUserProperties<>();
            entityUserProperties.setId(rs.getInt("id"));
            entityUserProperties.setName(rs.getString("name"));
            entityUserProperties.setUserId(rs.getLong("userId"));
            entityUserProperties.setType(entityClass);
            try {
                Object value=rs.getObject("value");
                if(entityClass.isInstance(value)) {
                    entityUserProperties.setValue(entityClass.cast(value));
                }
            }catch (ClassCastException e){
                return null;
            }
        return entityUserProperties;
    }

    @Override
    public List<EntityUserProperties<T>> toEntityList(ResultSet rs) throws SQLException {
        List<EntityUserProperties<T>> entityUserPropertiesList = new ArrayList<>();
        while (rs.next()) {
            entityUserPropertiesList.add(toEntity(rs));
        }
        return entityUserPropertiesList;
    }
}
