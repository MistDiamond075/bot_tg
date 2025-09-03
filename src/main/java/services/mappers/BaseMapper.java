package services.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface BaseMapper<T> {
    T toEntity(ResultSet rs) throws SQLException;
    List<T> toEntityList(ResultSet rs) throws SQLException;
}
