import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import utils.Pair;

import java.util.*;

public class BotButtons {
    public enum ButtonsNames {START,OPEN,SHOW_INVENTORY,SHOW_COMMANDS,SHOW_BANNERS,BACK_TO_MENU,ARROW_RIGHT,ARROW_LEFT}
    private final Map<String,String> buttons_responses = new HashMap<>();
    private final Map<Integer,List<Pair<String,Integer>>> pages_buttonsRows = new LinkedHashMap<>();
    private final int maxRows=4;

    public BotButtons() {
        setNames();
        setPositions();
    }

    public void setButtons(SendMessage sendMessage, int page) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<Pair<String,Integer>> buttons = pages_buttonsRows.get(page);
        replyKeyboardMarkup.setKeyboard(KeyboardButtonsBuilder(buttons));
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    private void setNames(){
        buttons_responses.put(ButtonsNames.START.toString().toLowerCase(),"Приветствую, %s, это симулятор кейсов из genshin impact. Воспользуйся клавиатурой снизу или посмотри доступные команды через /"+ButtonsNames.SHOW_COMMANDS.toString().toLowerCase());
        buttons_responses.put(ButtonsNames.SHOW_INVENTORY.toString().toLowerCase(),null);
        buttons_responses.put(ButtonsNames.SHOW_BANNERS.toString().toLowerCase(),null);
        buttons_responses.put(ButtonsNames.ARROW_RIGHT.toString().toLowerCase(),"Смена клавиатуры");
        buttons_responses.put(ButtonsNames.ARROW_LEFT.toString().toLowerCase(),"Смена клавиатуры");
        buttons_responses.put(ButtonsNames.BACK_TO_MENU.toString().toLowerCase(),"Смена клавиатуры");
        buttons_responses.put(ButtonsNames.OPEN.toString().toLowerCase(),null);
    }
    
    private void setPositions(){
        pages_buttonsRows.put(1,List.of(
                createButton(ButtonsNames.START.toString().toLowerCase(),1),
                createButton(ButtonsNames.OPEN.toString().toLowerCase(),2),
                createButton(ButtonsNames.SHOW_BANNERS.toString().toLowerCase(),3),
                createButton(ButtonsNames.ARROW_LEFT.toString().toLowerCase(),4),
                createButton(ButtonsNames.ARROW_RIGHT.toString().toLowerCase(),4)
        ));
        pages_buttonsRows.put(2,List.of(
                createButton(ButtonsNames.SHOW_INVENTORY.toString().toLowerCase(),1),
                createButton(ButtonsNames.SHOW_COMMANDS.toString().toLowerCase(),2),
                createButton(ButtonsNames.ARROW_LEFT.toString().toLowerCase(),3),
                createButton(ButtonsNames.ARROW_RIGHT.toString().toLowerCase(),3)
        ));
        pages_buttonsRows.put(3,List.of(
                createButton(ButtonsNames.OPEN.toString().toLowerCase()+":random",1),
                createButton(ButtonsNames.OPEN.toString().toLowerCase()+":recent",2),
                createButton(ButtonsNames.SHOW_BANNERS.toString().toLowerCase(),3),
                createButton(ButtonsNames.BACK_TO_MENU.toString().toLowerCase(),4)
        ));
    }
    
    private List<KeyboardRow> KeyboardButtonsBuilder(List<Pair<String,Integer>> buttonsList) {
        List<KeyboardRow> keyboard= new ArrayList<>();
        Optional<Pair<String,Integer>> maxRowValue=buttonsList.stream().max(Comparator.comparingInt(x -> x.second));
        if(maxRowValue.isPresent()) {
            if(maxRowValue.get().second>maxRows){
                return keyboard;
            }
        }
        List<Pair<String,Integer>> buttons=buttonsList.stream().sorted(Comparator.comparingInt(x -> x.second)).toList();
        Iterator<Pair<String,Integer>> iterator = buttons.iterator();
        if(!iterator.hasNext()) {return keyboard;}
        int prev= iterator.next().second;
        KeyboardRow keyboardRow = new KeyboardRow();
        while(iterator.hasNext()) {
            Pair<String,Integer> pair = iterator.next();
            if(prev!=pair.second) {
                prev= pair.second;
                keyboardRow=new KeyboardRow();
            }
            keyboardRow.add(new KeyboardButton(pair.first));
            keyboard.add(keyboardRow);
        }
        return keyboard;
    }

    private Pair<String,Integer> createButton(String name,int row){
        return new Pair.Builder<String,Integer>().first(name).second(row).build();
    }
}
