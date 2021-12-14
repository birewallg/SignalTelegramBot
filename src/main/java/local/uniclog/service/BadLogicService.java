package local.uniclog.service;

import local.uniclog.TelegramBotCore;
import local.uniclog.model.TelegramUser;
import lombok.Getter;

public class BadLogicService {
    @Getter
    private final DataManagement dataManagement = new DataManagement("data.json");
    private TelegramBotCore bot;

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
                    user = TelegramUser.builder().id(id).userName(login).subscriber(true).build();
                dataManagement.update(user);
            }
            case "/start" -> bot.sendMessage(id, "привет");
        }
    }
}
