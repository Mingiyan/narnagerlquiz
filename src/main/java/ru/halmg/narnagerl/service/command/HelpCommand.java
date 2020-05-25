package ru.halmg.narnagerl.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<String> commands = allCommands.stream()
//                .filter(Command::isPublic)
//                .map(this::getCommandDisplayName)
//                .collect(Collectors.toList());
//        commands.add(0, getCommandDisplayName(this));
        List<InlineKeyboardButton> menuButtons = new ArrayList<>();
        menuButtons.add(new InlineKeyboardButton().setText("help").setCallbackData("help"));
        menuButtons.add(new InlineKeyboardButton().setText("startQuiz").setCallbackData("startQuiz"));

//                .map(command -> new InlineKeyboardButton().setText(command.replaceAll("/", ""))
//                        .setCallbackData(command))
//                .collect(Collectors.toList());
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(menuButtons));

        context.setActiveCommand(null);
        return new SendMessage(context.getChatId(), "Тут приветствие надо").setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        throw new UnsupportedOperationException("Help command is not supported 'process' method");
    }

    private String getCommandDisplayName(Command command) {
        return command.commandName() + " -- " + command.commandDescription();
    }
}
