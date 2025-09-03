package services;

import entity.EntityUser;
import exception.DatabaseException;
import services.mappers.MapperUser;

import java.sql.*;

public class ServiceUser {
    private final MapperUser mapUser = new MapperUser();

    public ServiceUser() {
    }

    public boolean CheckIfUserExists(Long userId) throws DatabaseException {
        String sql = "select exists(select 1 from users where id = ?)";
        try (Connection conn = ServiceDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                ResultSet res = stmt.executeQuery();
                return res.next() && res.getInt(1) == 1;
            }
        catch (SQLException e) {
            throw new DatabaseException(e, EntityUser.class.getSimpleName());
        }
    }

    public EntityUser getUserById(Long userId) throws DatabaseException {
        String sql = "select * from users where id = ?";
        try (Connection conn = ServiceDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet res = stmt.executeQuery();
            conn.commit();
            return mapUser.toEntity(res);
        }
        catch (SQLException e) {
            throw new DatabaseException(e, EntityUser.class.getSimpleName());
        }
    }

    public void addUser(String name,Long userId,Long chatId) throws DatabaseException {
        String sql="insert into users (name, user_id, chat_id) values (?, ?, ?)";
        try(Connection conn=ServiceDatabase.getInstance().getConnection()){
            try(PreparedStatement stmt=conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setLong(2, userId);
                stmt.setLong(3, chatId);
                stmt.executeUpdate();
                conn.commit();
            }catch (Exception e){
                conn.rollback();
                throw new DatabaseException(e, EntityUser.class.getSimpleName());
            }
        }catch (SQLException e) {
            throw new DatabaseException(e, EntityUser.class.getSimpleName());
        }
    }
}
