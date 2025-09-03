import configuration.ConfApp;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import services.ServiceDatabase;

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
    private boolean star4Indicator=false;
    private boolean star5Indicator=false;
    private InputFile star4GIF;
    private InputFile star5GIF;
    private boolean CurrentRollGarant;
    private int GarantIterator;
    private String recent_banner="";
    protected ServiceDatabase dbm;

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
        //dbm=new services.ServiceDatabase();
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
}
