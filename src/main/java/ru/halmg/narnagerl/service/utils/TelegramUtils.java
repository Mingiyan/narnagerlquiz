package ru.halmg.narnagerl.service.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramUtils {

    public String getMessageText(Update update) {
        if (!update.hasMessage()) {
            return update.getCallbackQuery().getMessage().getText();
        } else {
            return update.getMessage().getText();
        }
    }

    public Message getMessage(Update update) {
        if (!update.hasMessage()) {
            return update.getCallbackQuery().getMessage();
        } else {
            return update.getMessage();
        }
    }
}
