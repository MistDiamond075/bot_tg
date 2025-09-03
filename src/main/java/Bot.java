import configuration.ConfApp;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Bot implements LongPollingBot {
    private final String BOT_TOKEN = ConfApp.get("tg.bot.token");
    private final String BOT_NAME = ConfApp.get("tg.bot.name");
    private final Data_storage storage=new Data_storage();;
    protected HashMap<Long, ArrayList<InputFile>> userSendFiles=new HashMap<>();;
    protected String recentBanner="";

    public Bot() {
        super();
        if(!storage.ConnectToDB()){
            System.exit(0);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {return BOT_TOKEN;}

    @Override
    public BotOptions getOptions() {
        return null;
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

    @Override
    public void onUpdateReceived(Update update) {
        Thread th=new Thread(()-> {
            try {
                    if (update.hasMessage() && update.getMessage().hasText()) {
                        InputFile pic_file=null;
                        InputFile gif_file=null;
                        String user_name=null;
                        Long user_id=null;
                        Long chatID=null;
                        Message inMess = update.getMessage();
                        String chatId = inMess.getChatId().toString();
                        chatID = inMess.getChatId();
                        user_id = update.getMessage().getFrom().getId();
                        user_name = update.getMessage().getFrom().getUserName();
                        String response = GetUsrMessage(inMess.getText(),user_id,chatID, user_name);
                        if(!userSendFiles.isEmpty()){
                            pic_file=userSendFiles.get(user_id).get(0);
                        gif_file=userSendFiles.get(user_id).get(1);
                        }
                        if (response != null) {
                            SendMessage outMess = new SendMessage();
                            setButtons(outMess,user_id);
                            outMess.enableHtml(true);
                            outMess.setChatId(chatId);
                            outMess.setText(response);
                            Message msg = execute(outMess);
                            if ((inMess.getText().equals("->")) || (inMess.getText().equals("<-"))) {
                                DeleteMessage delmsg = new DeleteMessage();
                                delmsg.setChatId(chatId);
                                delmsg.setMessageId(inMess.getMessageId());
                                execute(delmsg);
                            }
                        }
                        if (gif_file != null) {
                            SendAnimation outGif = new SendAnimation(chatId, gif_file);
                            outGif.setDuration(6);
                            SendChatAction wait_action = new SendChatAction();
                            wait_action.setChatId(chatId);
                            wait_action.setAction(ActionType.UPLOADDOCUMENT);
                            execute(wait_action);
                            Message msg = execute(outGif);
                            try {
                                Thread.sleep(8500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            DeleteMessage delmsg = new DeleteMessage();
                            delmsg.setChatId(outGif.getChatId());
                            delmsg.setMessageId(msg.getMessageId());
                            execute(delmsg);
                            gif_file = null;
                        }
                        if (pic_file != null) {
                            SendPhoto outPhoto = new SendPhoto(chatId, pic_file);
                            System.out.println("photo: "+pic_file.getAttachName()+" for "+chatId);
                            SendChatAction wait_action = new SendChatAction();
                            wait_action.setChatId(chatId);
                            wait_action.setAction(ActionType.UPLOADPHOTO);
                            execute(wait_action);
                            execute(outPhoto);
                            pic_file = null;
                        }
                    }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            });
        th.start();
    }
    public String GetUsrMessage(String textMsg, Long user_id, Long chatID, String user_name) {
        String response=null;
        String banner_name="";
        String str="";
        LinkedList<InputFile> userFiles=new LinkedList<>();
        for(int i=0;i<2;i++){userFiles.add(null);}
        userSendFiles.put(user_id,userFiles);
        if(textMsg.startsWith("/open")){
       if(textMsg.length()>6)str=textMsg.substring(6);
           textMsg="/open";
        }
        switch(textMsg){
            case "/start":{
                response = "Приветствую, "+user_name+", это симулятор кейсов из genshin impact. Воспользуйся клавиатурой снизу или посмотри доступные команды через /command_list";
                    if(!storage.CheckUserExist(user_id))storage.AddUserInvToDB(user_id,chatID,user_name);
                    storage.ChangeUserProperties(user_id,"",false,false,false);
                storage.setKeyboardPage(user_id,1);
                break;}
            case "/open":{
                if(storage.getKeyboardPage(user_id)==3) {
                    response = null;
                    banner_name=storage.AddRecentBanner(user_id,str);
                    if(banner_name.equals("Нет последних открытых баннеров")){return banner_name;}
                    System.out.println("="+banner_name);
                    storage.LoadPic(banner_name);
                    InputFile pic_file = new InputFile(new File(storage.CreateCaseResult(user_id)));
                    InputFile gif_file = storage.getStar4GIF();
                    userFiles.set(0, pic_file);
                    if (gif_file == null){
                        gif_file = storage.getStar5GIF();}
                    userFiles.set(1, gif_file);
                    storage.AddUserInvToDB(user_id, chatID, user_name);
                    userSendFiles.put(user_id,userFiles);
                }
                else if(storage.getKeyboardPage(user_id)!=3){
                    storage.setKeyboardPage(user_id,3);
                    response="Выбери опцию на клавиатуре или введи команду вручную";
                }
                break;}
            case "/show_inventory":{
                response=storage.shInv(user_id);
            break;}
            case "/show_update_log":{
                response=storage.getUpdateLog();
            break;}
            case ("/list_command"):{
                response="/start\nНачало работы с ботом\n/open\nОткрыть кейс (по умолчанию стандартный)\n/show_inventory\nПосмотреть список своих персонажей\n/show_update_log\nПосмотреть историю обновления бота\n/list_command\nПосмотреть список команд\n/list_banner\nПосмотреть баннеры";
                break;}
            case "/list_banner":{
                response="Альбедо - albedo\nАль-Хайтам - alhaitham\nАяка - ayaka\nАято - ayato\nБай Чжу - baizhu\nСайно - cyno\nДэхья - dehya\nЭола - eula\nФурина - furina\nГань Юй - ganyu\nХу Тао - hutao\nИтто - itto\nКадзуха - kazuha\nКли - klee\nКокоми - kokomi\nЛинни - lyney\nНахида - nahida\nНавия - navia" +
                        "\nНёвилетт - neuvilette\nНилу - nilou\nРайден - raiden\nШэнь Хэ - shenhe\nТарталья - tartaglia\nВенти - venti\nСтранник - wanderer\nРизли - wriothesley\nСянь Юнь - xianyun\nЯэ Мико - yae\nЕ Лань - yelan\nЁимия - yoimiya\nЧжун Ли - zhongli\nНейтральный - neutral";
            break;
            }
            case "/back_to_menu":{response="Смена клавиатуры";storage.setKeyboardPage(user_id,1);break;}
            case "->":{response="Смена клавиатуры";int KeyboardPage= storage.getKeyboardPage(user_id)+1;if(KeyboardPage>2){storage.setKeyboardPage(user_id,1);}else storage.setKeyboardPage(user_id,KeyboardPage);break;}
            case "<-":{response="Смена клавиатуры";int KeyboardPage= storage.getKeyboardPage(user_id)-1;if(KeyboardPage<1){storage.setKeyboardPage(user_id,2);}else storage.setKeyboardPage(user_id,KeyboardPage);break;}
            default:{response="Недействительная команда";}
        }
        return response;
    }


}