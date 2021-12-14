package local.uniclog;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class MainApp {

    public static void main(String[] args) {
        new MainApp().action();
    }

    public void action() {
        log.info("MainApp init");
        telegramBotInit();
    }

    /* telegram bot section START */
    public static TelegramBotCore telegramBotCore;

    public void telegramBotInit() {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotCore = new TelegramBotCore());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        telegramBotCore.sendQuery(msg);
    }
    /* telegram bot section END */
}
