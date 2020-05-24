package ru.halmg.narnagerl.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class DropLevelCommand implements Command {
    @Override
    public String commandDescription() {
        return null;
    }

    @Override
    public String commandName() {
        return null;
    }

    @Override
    public BotApiMethod execute(SessionContext context) {
        return null;
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        return null;
    }

    @Override
    public boolean isPublic() {
        return false;
    }
}
