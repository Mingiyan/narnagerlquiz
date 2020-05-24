package ru.halmg.narnagerl.service.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {

    String commandDescription();
    String commandName();
    BotApiMethod execute(SessionContext context);
    BotApiMethod process(SessionContext context, Update update);
    boolean isPublic();
}
