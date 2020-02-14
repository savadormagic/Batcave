package tgBotDeveloping;

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
import tgBotDeveloping.AnonimusChat;
import tgBotDeveloping.AnonimusRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    AnonimusChat anonimusChat = new AnonimusChat(this);
    AnonimusRoom anonimusRoom = new AnonimusRoom();
    //the most simple sendmessage
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
    //building simple buttons
    public void setButtos(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/start"));
        keyboardSecondRow.add(new KeyboardButton("/disconnect"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    //this method was created 4 send messages 4 start
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
    //building inlineButtons for start
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
    //this method was created 4 send messages 4 disconnect
    private void sendMessageWithInlineDis(Message msg, String text){
        try{
            execute(new SendMessage()
                    .setReplyMarkup(setInlineButtonsDis())
                    .setChatId(msg.getChatId())
                    .setText(text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //building inlineButtons for disconnect
    public InlineKeyboardMarkup setInlineButtonsDis() {
        InlineKeyboardMarkup iKMDIS = new InlineKeyboardMarkup();
        InlineKeyboardButton yesButtonDIS = new InlineKeyboardButton()
                .setText("disconnect")
                .setCallbackData("disconnect");
        InlineKeyboardButton noButtonDIS= new InlineKeyboardButton()
                .setText("wrong")
                .setCallbackData("ok");
        List<InlineKeyboardButton> cellsInlineKeyboard = Arrays.asList(yesButtonDIS, noButtonDIS);
        iKMDIS.setKeyboard(Arrays.asList(cellsInlineKeyboard));
        return iKMDIS;
    }


    //method that can delete any message
    public void deleteMessage(Long chatId, int msgId){
        try {
            execute(new DeleteMessage(chatId, msgId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //flag connection
    boolean connection;
    //almost main method in program
    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        if (update.hasMessage()) {
            if(msg.isCommand()){
                if (msg.getText().equals("/start")) {
                    deleteMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
                    connection = true;
                    sendMessage(msg, "Welcome");
                    sendMessageWithInline(msg, "Do u want to start anonymous conversation");
                }
                if (msg.getText().equals("/disconnect")) {
                    connection = false;
                    sendMessageWithInlineDis(msg, "Do you want to disconnect from conversation?");
                }
            }
            if(connection == true){
                anonimusChat.bootText(msg);
            }
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String answer = callbackQuery.getData();
            if (answer.equals("Yes")) {
                anonimusChat.setUser(callbackQuery.getFrom());
                sendMessage(callbackQuery.getMessage(), "Dialog started\nNow you can talk to anonymous friend\uD83D\uDE08 ");
                deleteMessage(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId());
            }
            if (answer.equals("tapedNope")) {
                sendMessage(callbackQuery.getMessage(), "u taped No");
            }
            if (answer.equals("disconnect")) {
                anonimusChat.removeUser(callbackQuery.getFrom());
                sendMessage(callbackQuery.getMessage(), "Dialog stopped ");
                deleteMessage(callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId());
            }
            if (answer.equals("wrong")) {
                sendMessage(callbackQuery.getMessage(), "ok");
            }
        }
    }
    //user Name of BOT
    public String getBotUsername() {
        return "irorrerBot";
    }
    //BOT's token
    public String getBotToken() {
        return "778029216:AAH0t-lSnmtzE4JVvrp94-JLimPJJV5lhHI";
    }
}