package local.uniclog;

import local.uniclog.model.Config;
import local.uniclog.model.TelegramUser;
import local.uniclog.service.BadLogicService;
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

    public BadLogicService service;

    public TelegramBotCore() {
        service = new BadLogicService(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message;
        String login = "none";
        Long id = 0L;
        if (update.getEditedMessage() != null) {
            message = update.getEditedMessage().getText();
            id = update.getEditedMessage().getChatId();
        } else {
            message = update.getMessage().getText();
            login = update.getMessage().getFrom().getUserName();
            id = update.getMessage().getChatId();
        }
        log.info(message);
        service.messageSeparator(id, login, message);
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

    /**
     * Отправить сообщение всем подписчикам
     *
     * @param msg текст сообщения
     */
    public void sendMessageForAllSubscribers(String msg) {
        service.getDataManagement().getData().stream()
                .filter(TelegramUser::getSubscriber)
                .forEach(obs -> sendMessage(obs.getId(), msg));
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