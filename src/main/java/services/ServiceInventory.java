package services;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceInventory {
    public void updateInventory(Long uid,Long chatID,String name,String[] CurrentRoll){
        String inv_to_add;
        System.out.println(uid);
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("select inventory_id from user where userID=" + uid);
            res.next();
            int user = res.getInt("inventory_id");
            int k = 0;
            ResultSet rs=stmt.executeQuery("select*from inventory");
            ResultSetMetaData rsmd=rs.getMetaData();
            String column_name;
            String column_to_add="";
            for(int i=2;i<=rsmd.getColumnCount();i++) {
                column_name = rsmd.getColumnName(i);
                for (int j = 0; j < 10; j++) {
                    if (CurrentRoll[j].substring(0, CurrentRoll[j].length() - 4).equals(column_name)) {column_to_add = CurrentRoll[j];}
                    if (!column_to_add.equals("")) {
                        inv_to_add = "update inventory set " + column_name + "=" + column_name + "+1" + " where id=" + user;
                        if (k < CurrentRoll.length) k++;
                        if (!inv_to_add.equals("")) {stmt.executeUpdate(inv_to_add);}
                    }
                    column_to_add = "";
                }
            }
            stmt.close();
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        //}
        //   star4Indicator=false;
        // star5Indicator=false;
    }

    public String getInventory(Long uid){
        Statement stmt = null;
        String str;
        int invID;
        String res="Персонажи:\n\n";
        try {
            stmt = conn.createStatement();
            str = "select inventory_id from user where userID=" + uid;
            ResultSet userIDres = stmt.executeQuery(str);
            userIDres.next();
            invID=userIDres.getInt("inventory_id");
            str = "select * from inventory where id=" + invID;
            ResultSet inv_get=stmt.executeQuery(str);
            ResultSetMetaData inv_md=inv_get.getMetaData();
            inv_get.next();
            for(int i=2;i<=inv_md.getColumnCount();i++) {
                //       res=res+shInvChangeItemName(inv_md.getColumnName(i)) + " x" + inv_get.getInt(inv_md.getColumnName(i))+"\n";
            }
            stmt.close();
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        return res;
    }
}
