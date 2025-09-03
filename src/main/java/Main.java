import configuration.ConfApp;
import exception.ConfigurationException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.URI;
import java.util.logging.Logger;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            ConfApp.load();
        } catch (ConfigurationException e) {
            System.err.println("ConfigurationException: " + e.getMessage());
            System.exit(1);
        }
        try {
                TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(new Bot());
            } catch (TelegramApiException e) {
                logger.severe(e.getMessage());
            }
        }

        private static void init(){

        }
}

class Main_test {
    public static void main(String[] args) {
        Data_storage data = new Data_storage();
        long time = System.currentTimeMillis();
        String test = "/open:smth";
        System.out.println(test.substring(6));
        System.out.println(data.getRandomBanner());
        //   data.LoadPic("neutral");
        // data.CreateCaseResult();
        System.out.println("time: " + (System.currentTimeMillis() - time));
        URI test1 = null;
       /* try {
            test1 = new URL("https://i.imgur.com/nWHBRCJ.gif").toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        File file=new File(test1);
        System.out.println(file.isFile());*/
        InputFile inputFile = new InputFile("https://i.imgur.com/nWHBRCJ.gif");
        try {
            inputFile.validate();
        } catch (TelegramApiValidationException e) {
            throw new RuntimeException(e);
        }
     data.ChangeUserProperties(580L,"rec",false,true,false);
        System.out.println((String) data.getUserProperty(580L,"garant"));
        System.out.println((String) data.getUserProperty(580L,"star_5_indicator"));
        System.out.println((String) data.getUserProperty(580L,"recent_banner"));
        Boolean tmpbool=Boolean.valueOf(data.getUserProperty(580L,"garant"));
        System.out.println(tmpbool);
        System.out.println("--------------");
        data.AddRecentBanner(580L,"nahida");
        System.out.println((String) data.getUserProperty(580L,"garant"));
        System.out.println((String) data.getUserProperty(580L,"star_5_indicator"));
        System.out.println((String) data.getUserProperty(580L,"recent_banner"));
        for(int i=0;i<2;i++){System.out.println(i);}
    System.out.println(data.getRandomBanner());
        System.out.println(data.getUpdateLog());
    }
}
