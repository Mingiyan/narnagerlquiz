package ru.halmg.narnagerl.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.halmg.narnagerl.model.Tag;
import ru.halmg.narnagerl.service.TagService;
import ru.halmg.narnagerl.service.utils.TelegramUtils;

@Service
@RequiredArgsConstructor
public class AddTagCommand implements Command {

    private TagService tagService;
    private TelegramUtils telegramUtils;

    @Autowired
    public AddTagCommand(TagService tagService, TelegramUtils telegramUtils) {
        this.tagService = tagService;
        this.telegramUtils = telegramUtils;
    }

    @Override
    public String commandDescription() {
        return "Добавить новый тег";
    }

    @Override
    public String commandName() {
        return "/addTag";
    }

    @Override
    public BotApiMethod execute(SessionContext context) {
        context.setActiveCommand(CommandType.ADD_TAG);
        return new SendMessage(context.getChatId(), "Введите тег:");
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        String msg = telegramUtils.getMessageText(update);

        Tag tag = new Tag();
        tag.setName(msg);
        tagService.save(tag);
        context.setActiveCommand(null);
        return new SendMessage(context.getChatId(), "Ввод тега закончен.");
    }

    @Override
    public boolean isPublic() {
        return false;
    }
}
