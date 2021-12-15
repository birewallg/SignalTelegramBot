package local.uniclog.service;

import local.uniclog.TelegramBotCore;
import local.uniclog.model.TelegramUser;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BadLogicService {
    @Getter
    private final DataManagement dataManagement = new DataManagement("data.json");
    private final TelegramBotCore bot;

    private int checkCount = 0;

    public BadLogicService(TelegramBotCore bot) {
        this.bot = bot;
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
                for (TelegramUser u : dataManagement.getData()) {
                    if (u.getId().equals(id)) {
                        user = u;
                        break;
                    }
                }
                if (user != null)
                    user.setSubscriber(!user.getSubscriber());
                else
                    user = TelegramUser.builder().id(id).userName(login == null ? "none" : login).subscriber(true).build();
                dataManagement.update(user);
            }
            case "/start" -> bot.sendMessage(id, "привет");
            default -> check(message);
        }
    }

    /**
     * Разбор по маске
     *
     * @param message сообщение
     */
    private void check(String message) {
        String regex = "(\\(.{6}\\))(.*)(\\(.*\\))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);
        if (matcher.find() && message.contains("\u2705")) {
            checkCount++;
            bot.sendMessageForAllSubscribers("checkCount = " + checkCount);
            if (checkCount == 9) {
                bot.sendMessageForAllSubscribers("9 trigger!");
                checkCount = 0;
            }
        } else checkCount = 0;
    }
}
