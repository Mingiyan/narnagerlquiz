package ru.halmg.narnagerl.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final Set<Command> allCommands;

    @Override
    public String commandDescription() {
        return "Получить список команд";
    }

    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public BotApiMethod execute(SessionContext context) {
        String msg = allCommands.stream()
                .filter(Command::isPublic)
                .map(this::getCommandDisplayName)
                .collect(Collectors.joining("\n"));
        msg = getCommandDisplayName(this) + "\n" + msg;
        context.setActiveCommand(null);
        return new SendMessage(context.getChatId(), msg);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        throw new UnsupportedOperationException("Help command is not supported 'process' method");
    }

    private String getCommandDisplayName(Command command) {
        return command.commandName() + " -- " + command.commandDescription();
    }

}
