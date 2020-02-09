import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.function.Function;

public class Main {
    public static void main(String[] args){
        Function<Integer, Integer> add1 = x ->x * x;
        ApiContextInitializer.init();
        TelegramBotsApi bot = new TelegramBotsApi();
        try {
            bot.registerBot(new Bot());
        }catch(TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }
}
