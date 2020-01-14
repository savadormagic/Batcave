import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnonimusChat {
    private TelegramLongPollingBot tlpb;
    public AnonimusChat(TelegramLongPollingBot tlpb){
        this.tlpb = tlpb;
    }

    private AnonimusRoom anonimusRoom = new AnonimusRoom();
    private void sendMessage(long UsersId, String text){
        try{
            tlpb.execute(new SendMessage()
                    .setChatId(UsersId)
                    .setText(text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public boolean setUser(User user) {
        return anonimusRoom.setUser(user);
    }
    public void bootText(Message msg){
        msg.getChatId();
        if(msg.getChatId().equals(anonimusRoom.getIdFirst())){
            sendMessage(anonimusRoom.getIdSecond(), msg.getText());
            System.out.println(anonimusRoom.getNameFirst() + " говорит: " + msg.getText());
        }
        if(msg.getChatId().equals(anonimusRoom.getIdSecond())){
            sendMessage(anonimusRoom.getIdFirst(), msg.getText());
            System.out.println(anonimusRoom.getNameSecond() + " говорит: " + msg.getText());

        }
    }

}
