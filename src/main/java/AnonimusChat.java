import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;

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
    private void sendAudio(long UserId, String audioId){
        System.out.println("workAudio");
        try{
            tlpb.execute(new SendAudio()
                    .setChatId(UserId)
                    .setAudio(audioId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendVideo(long UserId, String videoId){
        try{
            tlpb.execute(new SendVideo()
                    .setChatId(UserId)
                    .setVideo(videoId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendImage(long UserId, List<PhotoSize> imageId){
        SendPhoto sp = new SendPhoto();
        for(PhotoSize ps: imageId){
            sp.setPhoto(ps.getFileId());
        }
        try{
            tlpb.execute(sp
                    .setChatId(UserId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //animation GIF
    private void sendAnimation(long UserId, String AnimId){
        try {
            tlpb.execute(new SendAnimation()
            .setChatId(UserId)
            .setAnimation(AnimId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //sticker
    private void sendSticker(long UserId, String StickerId){
        try {
            tlpb.execute(new SendSticker()
                    .setChatId(UserId)
                    .setSticker(StickerId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void sendDocument(long UserId, String DocumentID){
        SendDocument sendDocument = new SendDocument();
        try {
            tlpb.execute(new SendDocument()
                    .setChatId(UserId)
                    .setDocument(DocumentID));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean setUser(User user) {
        return anonimusRoom.setUser(user);
    }
    public void bootText(Message msg){
        long id;
        if(msg.getChatId().equals(anonimusRoom.getIdFirst()))
            id = anonimusRoom.getIdSecond();
        else if(msg.getChatId().equals(anonimusRoom.getIdSecond()))
            id = anonimusRoom.getIdFirst();
        else
            return ;
        //text
        if (msg.hasText()) {
            sendMessage(id, msg.getText());
            System.out.println(id + " говорит " + msg.getText());
        }
        //sticker
        if (msg.hasSticker()) {
            sendSticker(id, msg.getSticker().getFileId());
            System.out.println(id + " прислал стикер: " + msg.getSticker().getFileId());
        }
        //photo
        if(msg.hasPhoto()){
            sendImage(id, msg.getPhoto());
            System.out.println(id + " прислал фотку: " + msg.getPhoto().get(0).getFileId());
        }
        //animation
        if (msg.hasAnimation()) {
            sendAnimation(id, msg.getAnimation().getFileId());
            System.out.println(id + " отпраил анимированную поебень " + msg.getAnimation().getFileId());
        }
        //video
        if(msg.hasVideo()){
            sendVideo(id, msg.getVideo().getFileId());
            System.out.println(id + " отправил видео " + msg.getVideo().getFileId());
        }
        //document
        if(msg.hasDocument()){
            sendDocument(id, msg.getDocument().getFileId());
            System.out.println(id + " отправил  документ " + msg.getDocument().getFileId());
        }
        //audio
        if(msg.hasAudio()){
            sendAudio(id, msg.getAudio().getFileId());
            System.out.println(id + " отправил аудио " + msg.getAudio().getFileId());
        }
    }
}
