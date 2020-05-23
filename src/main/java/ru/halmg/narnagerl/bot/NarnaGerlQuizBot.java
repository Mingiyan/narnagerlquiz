package ru.halmg.narnagerl.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.halmg.narnagerl.service.command.CommandListener;

public class NarnaGerlQuizBot extends TelegramLongPollingBot {

    private CommandListener commandListener;

    private String token;
    private String username;

    public NarnaGerlQuizBot(DefaultBotOptions options,
                              String token,
                              String username,
                              CommandListener commandListener) {
        super(options);
        this.token = token;
        this.username = username;
        this.commandListener = commandListener;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            execute(commandListener.getCommand(update));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
