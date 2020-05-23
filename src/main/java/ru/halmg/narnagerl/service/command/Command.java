package ru.halmg.narnagerl.service.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface Command {

    String commandDescription();
    String commandName();
    BotApiMethod execute(SessionContext context);
    boolean isPublic();
}
