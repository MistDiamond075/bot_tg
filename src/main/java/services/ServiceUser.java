package services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceUser {
    public boolean CheckIfUserExists(Long uid){
        try (Connection conn = ds.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet res=stmt.executeQuery("select userID from user");
            while(res.next()){
                if(uid==res.getLong("userID"))return true;
            }
            stmt.close();
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        return false;
    }
}
