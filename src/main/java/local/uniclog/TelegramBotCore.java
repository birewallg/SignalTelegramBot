package local.uniclog;

import local.uniclog.model.Config;
import local.uniclog.model.TelegramUser;
import local.uniclog.service.DataManagement;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.charset.StandardCharsets;

/**
 * Telegram Bot Core
 */
@Slf4j
public class TelegramBotCore extends TelegramLongPollingBot {
    private final Config config = new Config("config.json");
    private final String BOT_NAME = config.getName();
    private final String BOT_TOKEN = config.getToken();

    private final DataManagement data = new DataManagement("data.json");

    /**
     * отправить сообщение всем подписчикам
     *
     * @param msg текст сообщения
     */
    public void sendQuery(String msg) {
        data.getData().stream()
                .filter(TelegramUser::getSubscriber)
                .forEach(obs -> sendMessage(obs.getId(), msg));
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message;
        String login;
        if (update.getEditedMessage() != null) {
            message = update.getEditedMessage().getText();
            login = update.getEditedMessage().getFrom().getUserName();
        } else {
            message = update.getMessage().getText();
            login = update.getMessage().getFrom().getUserName();
        }
        log.info(message);
        messageSeparator(update.getMessage().getChatId(), login, message);
    }

    /**
     * разбор входящих сообщений
     *
     * @param id      id пользователя
     * @param message текст сообщения
     */
    public void messageSeparator(Long id, String login, String message) {
        switch (message) {
            case "/sub" -> {
                TelegramUser user = null;
                for (TelegramUser u : data.getData()) {
                    if (u.getId().equals(id)) {
                        user = u;
                        break;
                    }
                }
                if (user != null)
                    user.setSubscriber(!user.getSubscriber());
                else
                    user = TelegramUser.builder().id(id).userName(login).subscriber(true).build();
                data.update(user);
            }
        }
    }

    /**
     * Sender
     *
     * @param chatId чат куда отправлять
     * @param text   сообщение
     */
    public synchronized void sendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(convertToUTF8(text));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private String convertToUTF8(String text) {
        return new String(text.getBytes(), StandardCharsets.UTF_8);
    }

    private String convertToASCII(String text) {
        return new String(text.getBytes(), StandardCharsets.US_ASCII);
    }

}