import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    AnonimusChat anonimusChat = new AnonimusChat(this);

    private void sendMessage(Message msg, String text){
        SendMessage sm = new SendMessage();
        //возможность разметки
        sm.enableMarkdown(true);
        //В какой чат отправить
        sm.setChatId(msg.getChatId().toString());
        //на какое сообшение отвечает
        sm.setReplyToMessageId(msg.getMessageId());
        //текст
        sm.setText(text);
        try{
            setButtos(sm);
            execute(new SendMessage()
                    .setChatId(msg.getChatId())
                    .setText(text)
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtos(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/start"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    private void sendMessageWithInline(Message msg, String text){
        try{
            execute(new SendMessage()
                    .setReplyMarkup(setInlineButtons())
                    .setChatId(msg.getChatId())
                    .setText(text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup setInlineButtons() {
        InlineKeyboardMarkup iKM = new InlineKeyboardMarkup();
        InlineKeyboardButton yesButton = new InlineKeyboardButton()
                .setText("yes")
                .setCallbackData("Yes");
        InlineKeyboardButton noButton= new InlineKeyboardButton()
                .setText("nope")
                .setCallbackData("tapedNope");
        List<InlineKeyboardButton> cellsInlineKeyboard = Arrays.asList(yesButton, noButton);
        iKM.setKeyboard(Arrays.asList(cellsInlineKeyboard));
        return iKM;
    }

    public void deleteMessage(Long chatId, int msgId){
        try {
            execute(new DeleteMessage(chatId, msgId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        SendMessage sm = new SendMessage();
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                if(update.getMessage().getText().equals("/start")){
                    deleteMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
                }
                if(update.getMessage().getText().equals("/start")){
                    sendMessage(msg, "Welcome");
                    sendMessageWithInline(msg, "Do u want to start anonymous conversation");
                }
//                if(update.getMessage().getText().equals("/start")){
//                    sendMessage(msg, "Welcome Naxuy");
//                }
                anonimusChat.bootText(msg);
            }
        }
        if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String answer = callbackQuery.getData();
            if(answer.equals("Yes")){
                anonimusChat.setUser(callbackQuery.getFrom());
                sendMessage(callbackQuery.getMessage(), "u taped Yes");
                deleteMessage(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId());
            }
            if(answer.equals("tapedNope")){
                sendMessage(callbackQuery.getMessage(), "u taped No");
            }
        }
    }
    public String getBotUsername() {
        return "irorrerBot";
    }

    public String getBotToken() {
        return "778029216:AAH0t-lSnmtzE4JVvrp94-JLimPJJV5lhHI";
    }
}
