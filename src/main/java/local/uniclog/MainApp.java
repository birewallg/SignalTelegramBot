package local.uniclog;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class MainApp {
    public static TelegramBotCore telegramBotCore;

    public static void main(String[] args) {
        new MainApp().action();
    }

    public void action() {
        log.info("MainApp init");
        telegramBotInit();
    }

    public void telegramBotInit() {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotCore = new TelegramBotCore());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
