import configuration.ConfApp;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import services.ServiceDataBase;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

public class Data_storage {
    private HashMap<Long,ArrayList<String>> userProperties;
    private ArrayList<String> star3Img;
    private ArrayList<String> star4Img;
    private ArrayList<String> star5Img;
    private String[] CurrentRoll;
    private boolean star4Indicator=false;
    private boolean star5Indicator=false;
    private InputFile star4GIF;
    private InputFile star5GIF;
    private boolean CurrentRollGarant;
    private int GarantIterator;
    private String recent_banner="";
    protected ServiceDataBase dbm;

    Data_storage()
    {
        star3Img=new ArrayList<>();
        star4Img=new ArrayList<>();
        star5Img=new ArrayList<>();
        CurrentRoll=new String[10];
       // CurrentRollGarant=new int[10];
        star4GIF=new InputFile();
        star5GIF=new InputFile();
        GarantIterator=0;
        userProperties=new HashMap<>();
        //dbm=new services.ServiceDataBase();
    }

    boolean ConnectToDB(){
        try{Class.forName("com.mysql.jdbc.Driver").newInstance();}
        catch(Exception e){System.err.println("Can't load driver cause: "+e);}
        try {conn=DriverManager.getConnection(ConfApp.get("db.url"),ConfApp.get("db.user"),ConfApp.get("db.password"));}
        catch (SQLException e) {throw new RuntimeException(e);}
        if(conn!=null){return true;}
        System.out.println("=Unable to connect to DB=");
        return false;
    }

    void AddUserInvToDB(Long uid,Long chatID,String name){
                String inv_to_add;
                System.out.println(uid);
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet res = stmt.executeQuery("select inventory_id from user where userID=" + uid);
                    res.next();
                    int user = res.getInt("inventory_id");
                   // File dir = new File("./files\\pics\\wish\\4star_char");
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
        ChangeUserProperties(uid,getUserProperty(uid,"recent_banner"),false,false,false);
    }

    void ChangeUserProperties(Long uid, String recent_banner, boolean garant,boolean star_4_indicator, boolean star_5_indicator){
        ArrayList<String> PropertiesList=new ArrayList<>();
        PropertiesList.add(recent_banner);
        PropertiesList.add(String.valueOf(garant));
        PropertiesList.add(String.valueOf(star_4_indicator));
        PropertiesList.add(String.valueOf(star_5_indicator));
        userProperties.put(uid,PropertiesList);
    }

    String getUserProperty(Long uid, String prop_name){
        ArrayList<String> tempList=userProperties.get(uid);
        int prop_num;
        switch (prop_name){
            case "recent_banner":{prop_num=0;break;}
            case "garant":{prop_num=1;break;}
            case "star_4_indicator":{prop_num=2;break;}
            case "star_5_indicator":{prop_num=3;break;}
            default:{prop_num=-1;}
        }
        if(!tempList.isEmpty()){if(prop_num<tempList.size())return tempList.get(prop_num);}
        return null;
    }

    String AddRecentBanner(Long uid,String text){
        String str="";
        String str2="";
        if(text.length()>=6){
            str2=text;
            if(str2.equals("recent")){if(getUserProperty(uid,"recent_banner").equals("")){return str="Нет последних открытых баннеров";}return getUserProperty(uid,"recent_banner");}
            else if(str2.equals("random")){ChangeUserProperties(uid,getRandomBanner(),Boolean.valueOf(getUserProperty(uid,"garant")),Boolean.valueOf(getUserProperty(uid,"star_4_indicator")),Boolean.valueOf(getUserProperty(uid,"star_5_indicator")));return getUserProperty(uid,"recent_banner");}
            else{ChangeUserProperties(uid,str2,Boolean.valueOf(getUserProperty(uid,"garant")),Boolean.valueOf(getUserProperty(uid,"star_4_indicator")),Boolean.valueOf(getUserProperty(uid,"star_5_indicator")));return getUserProperty(uid,"recent_banner");}
        }
        else{ChangeUserProperties(uid,"neutral",Boolean.valueOf(getUserProperty(uid,"garant")),Boolean.valueOf(getUserProperty(uid,"star_4_indicator")),Boolean.valueOf(getUserProperty(uid,"star_5_indicator")));return getUserProperty(uid,"recent_banner");}
       // str="/open";
      //  return str;
        StringBuilder
    }

    boolean CheckUserExist(Long uid){
        try {
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

    String shInv(Long uid){
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
                res=res+shInvChangeItemName(inv_md.getColumnName(i)) + " x" + inv_get.getInt(inv_md.getColumnName(i))+"\n";
            }
            stmt.close();
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        return res;
    }

    protected String shInvChangeItemName(String name){
        String res="";
        switch(name){
            case "barbara":{res="<b>Барбара</b>";break;}
            case "bei_dou":{res="<b>Бей Доу</b>";break;}
            case "bennet":{res="<b>Беннет</b>";break;}
            case "chong_jun":{res="<b>Чун Юнь</b>";break;}
            case "diona":{res="<b>Диона</b>";break;}
            case "kirara":{res="<b>Кирара</b>";break;}
            case "nin_guang":{res="<b>Нин Гуан</b>";break;}
            case "noelle":{res="<b>Ноэлль</b>";break;}
            case "rosaria":{res="<b>Розария</b>";break;}
            case "sara":{res="<b>Сара</b>";break;}
            case "sucrose":{res="<b>Сахароза</b>";break;}
            case "toma":{res="<b>Тома</b>";break;}
            case "xian_ling":{res="<b>Сян Лин</b>";break;}
            case "xin_qu":{res="<b>Син Цю</b>";break;}
            case "diluc":{res="<b>Дилюк</b>";break;}
            case "jean":{res="<b>Джинн</b>";break;}
            case "keqing":{res="<b>Кэ Цин</b>";break;}
            case "mona":{res="<b>Мона</b>";break;}
            case "qiqi":{res="<b>Ци ци</b>";break;}
            case "tignari":{res="<b>Тигнари</b>";break;}
            case "fischl":{res="<b>Фишль</b>";break;}
            case "razor":{res="<b>Рэйзор</b>";break;}
            case "gorou":{res="<b>Горо</b>";break;}
            case "xinyan":{res="<b>Синь Янь</b>";break;}
            case "shinobu":{res="<b>Шинобу</b>";break;}
            case "sayu":{res="<b>Саю</b>";break;}
            case "yun_jin":{res="<b>Юнь Цзинь</b>";break;}
            case "dori":{res="<b>Дори</b>";break;}
            case "yanfei":{res="<b>Янь Фей</b>";break;}
            case "layla":{res="<b>Лайла</b>";break;}
            case "heizou":{res="<b>Хэйдзо</b>";break;}
            case "candace":{res="<b>Кандакия</b>";break;}
            case "mika":{res="<b>Мика</b>";break;}
            case "collei":{res="<b>Коллеи</b>";break;}
            case "amber":{res="<b>Эмбер</b>";break;}
            case "faruzan":{res="<b>Фарузан</b>";break;}
            case "kaveh":{res="<b>Кавех</b>";break;}
            case "lynette":{res="<b>Линетт</b>";break;}
            case "lisa":{res="<b>Лиза</b>";break;}
            case "kayea":{res="<b>Кэйа</b>";break;}
            case "yaoyao":{res="<b>Яо Яо</b>";break;}
            case "gaming":{res="<b>Ка Минг</b>";break;}
            case "chevreuse":{res="<b>Шеврёз</b>";break;}
            case "charlotte":{res="<b>Шарлотта</b>";break;}
            case "freminet":{res="<b>Фремине</b>";break;}
            default:res=name;
        }
        return res;
    }

    String CreateCaseResult(Long uid){
        BufferedImage result = new BufferedImage(880, 440, BufferedImage.TYPE_INT_ARGB);
        Graphics g = result.getGraphics();
        String[] images =new String[10];
        int k=0;
        for(int i=0;i<images.length;i++){
            images[i]=getRandPic();
        }
        GarantIterator=0;
        int x=0;int y=0;
        for(String image : images){
            BufferedImage img;
            try {
                img = ImageIO.read(new File(image));
            }
            catch (IOException e) {throw new RuntimeException(e);}
            if(k<10){CurrentRoll[k]=new File(image).getName();}
            g.drawImage(img, x, y, new Color(100,0,100,0),null);
            x += 88;
            if(x > result.getWidth()){
                x = 0;
                y += img.getHeight();
            }
            k++;
        }
        try {
            ImageIO.write(result,"png",new File("files/pics/result"+uid+".png"));
        }
        catch (IOException e) {throw new RuntimeException(e);}
        return "./files\\pics\\result"+uid+".png";
    }

    void LoadPic(String bannerName) {
        File dir = new File("./files\\pics\\wish\\3star_wpn");
        star5Img.clear();
        star4Img.clear();
        int k = 2;
        if (!bannerName.equals("")) {
            while (k >= 0) {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.isFile())
                        if (k == 2) star3Img.add(file.getPath());
                        else if (k == 1) star4Img.add(file.getPath());
                        else if (k == 0) star5Img.add(file.getPath());
                }
                k--;
                switch (k) {
                    case 1: {
                        dir = new File("./files\\pics\\wish\\4star_all");
                        break;
                    }
                    case 0: {
                        dir = new File("./files\\pics\\wish\\banner\\" + bannerName);
                        break;
                    }
                    default:
                        k = -1;
                }
            }
           // setStar4GIF(new InputFile(new File("./files\\pics\\wish\\4star.gif")));
            //setStar5GIF(new InputFile(new File("./files\\pics\\wish\\5star.gif")));
              setStar4GIF(new InputFile("https://i.imgur.com/0h45nq7.gif"));  //"https://i.imgur.com/0h45nq7.gif"
            setStar5GIF(new InputFile("https://imgur.com/fpeYMCn.gif"));  //"https://imgur.com/fpeYMCn.gif"
        /*for(int i=0;i<star3Img.size();i++){
            System.out.println(star3Img.get(i));
        }
        System.out.println("------------------------");
        for(int i=0;i<star4Img.size();i++){
            System.out.println(star4Img.get(i));
        }
        System.out.println("------------------------");
        for(int i=0;i<star5Img.size();i++){
            System.out.println(star5Img.get(i));
        }*/
        }
    }

    String getRandPic(){
        String res="";
        float randValue = ((float)(Math.random() * 200)+0)/200;
        if((GarantIterator==9)&&(randValue>0.16)){
            if(!CurrentRollGarant){randValue=(float)0.10;}
        }
        if(randValue>0.16){
            int randPic=(int)(Math.random() * star3Img.size());
            res=star3Img.get(randPic);
            CurrentRollGarant=false;
            if(GarantIterator<10)GarantIterator++;
        }
        else if((randValue<=0.16)&&(randValue>0.03)){
            int randPic=(int)(Math.random() * star4Img.size());
            res=star4Img.get(randPic);
            if(!star5Indicator)star4Indicator=true;
            CurrentRollGarant=true;
            if(GarantIterator<10)GarantIterator++;
        }
        else if(randValue<=0.03){
            int randPic=(int)(Math.random() * star5Img.size());
            res=star5Img.get(randPic);
            star5Indicator=true;
            star4Indicator=false;
            CurrentRollGarant=true;
            if(GarantIterator<10)GarantIterator++;
        }
        return res;
    }
    public InputFile getStar4GIF() {if(star4Indicator)return star4GIF;else return null;}
    public void setStar4GIF(InputFile star4GIF) {this.star4GIF = star4GIF;}
    public InputFile getStar5GIF() {if(star5Indicator)return star5GIF;else return null;}
    public void setStar5GIF(InputFile star5GIF) {this.star5GIF = star5GIF;}
    public String getRandomBanner(){
        String res="neutral";
        int count=0;
        ArrayList<String> AllBanners=new ArrayList<>();
        File dir = new File("./files\\pics\\wish\\banner");
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) AllBanners.add(file.getName());
            count++;
        }
        int randValue = ((int) (Math.random() * count));
        if(!AllBanners.isEmpty())res=AllBanners.get(randValue);
        return res;
    }
    String getUpdateLog(){
        String res="";
        try(FileReader reader = new FileReader("./files/update_logs/updates.txt")){
            int c;
            while((c=reader.read())!=-1){res+=(char) c;}
        }
        catch(IOException ex){System.out.println(ex.getMessage());}
        return res;
    }

    int getKeyboardPage(Long uid){
        String str;
        int page=1;
        Statement stmt;
        try {
            stmt = conn.createStatement();
        str = "select kb_page from user where userID=" + uid+";";
        ResultSet res = stmt.executeQuery(str);
        res.next();
        page=res.getInt("kb_page");
        }
        catch (SQLException e) {throw new RuntimeException(e);}
        return page;
    }

    void setKeyboardPage(Long uid,int page){
        String str;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            str = "update user set kb_page="+page+" where userID="+uid +";";
            stmt.executeUpdate(str);
        }
        catch (SQLException e) {throw new RuntimeException(e);}
    }
}
