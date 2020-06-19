package ru.halmg.narnagerl.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> menuButtons = new ArrayList<>();
        menuButtons.add(new InlineKeyboardButton().setText("Нег тест").setCallbackData("/startQuiz"));

        List<InlineKeyboardButton> tagButtons1 = new ArrayList<>();
        tagButtons1.add(new InlineKeyboardButton().setText("Фильм").setCallbackData("/startQuizFilm"));
        tagButtons1.add(new InlineKeyboardButton().setText("Мультфильм").setCallbackData("/startQuizAnimation"));
        List<InlineKeyboardButton> tagButtons2 = new ArrayList<>();
        tagButtons2.add(new InlineKeyboardButton().setText("Көгҗм").setCallbackData("/startQuizMusic"));
        tagButtons2.add(new InlineKeyboardButton().setText("Эрүл-менд").setCallbackData("/startQuizMedicine"));
        List<InlineKeyboardButton> tagButtons3 = new ArrayList<>();
        tagButtons3.add(new InlineKeyboardButton().setText("Тодо халимаг").setCallbackData("/startTodo"));
        tagButtons3.add(new InlineKeyboardButton().setText("Наадк төрмүд").setCallbackData("/startOther"));
        buttons.add(menuButtons);
        buttons.add(tagButtons1);
        buttons.add(tagButtons2);
        buttons.add(tagButtons3);
        inlineKeyboardMarkup.setKeyboard(buttons);

        context.setActiveCommand(null);
        return new SendMessage(context.getChatId(), "Нег әңг эс гиҗ «Нег тест» гидг цуг төрмүдин тест суңһҗ автн.\n" +
                "Выберите одну из категорий или «Общий тест» - тест по всем темам.\n").setReplyMarkup(inlineKeyboardMarkup);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        throw new UnsupportedOperationException("Help command is not supported 'process' method");
    }

    private String getCommandDisplayName(Command command) {
        return command.commandName() + " -- " + command.commandDescription();
    }
}
