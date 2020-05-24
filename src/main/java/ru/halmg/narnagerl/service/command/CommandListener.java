package ru.halmg.narnagerl.service.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.halmg.narnagerl.model.Question;
import ru.halmg.narnagerl.repository.SessionRepository;
import ru.halmg.narnagerl.repository.UserRepository;
import ru.halmg.narnagerl.service.QuestionService;
import ru.halmg.narnagerl.service.button.AnswerButtonsService;
import ru.halmg.narnagerl.service.util.TelegramUtils;

import java.util.HashMap;

@Component
public class CommandListener {

    private HelpCommand helpCommand;
    private DropLevelCommand dropLevelCommand;
    private StartQuizCommand startQuizCommand;
    private QuestionService questionService;
    private AnswerButtonsService answerButtonsService;
    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private TelegramUtils telegramUtils;
    private HashMap<CommandType, Command> activeCommandHashMap;

    @Autowired
    public CommandListener(HelpCommand helpCommand,
                           TelegramUtils telegramUtils,
                           SessionRepository sessionRepository,
                           HashMap<CommandType, Command> activeCommandHashMap,
                           StartQuizCommand startQuizCommand,
                           DropLevelCommand dropLevelCommand,
                           QuestionService questionService,
                           AnswerButtonsService answerButtonsService,
                           UserRepository userRepository) {
        this.helpCommand = helpCommand;
        this.dropLevelCommand = dropLevelCommand;
        this.telegramUtils = telegramUtils;
        this.activeCommandHashMap = activeCommandHashMap;
        this.startQuizCommand = startQuizCommand;
        this.sessionRepository = sessionRepository;
        this.questionService = questionService;
        this.answerButtonsService = answerButtonsService;
        this.userRepository = userRepository;

    }

    public BotApiMethod getCommand(Update update) throws TelegramApiRequestException {
        Message incomingMsg = telegramUtils.getMessage(update);

        SessionContext context = sessionRepository.findByChatId(incomingMsg.getChatId());
        if (context == null) {
            context = new SessionContext();
            context.setChatId(incomingMsg.getChatId());
            sessionRepository.save(context);
        }

        BotApiMethod method;
        switch (incomingMsg.getText()) {
            case "/help":
                method = helpCommand.execute(context);
                break;
            case "/startQuiz":
                method = startQuizCommand.execute(context);
                break;
            default:
                if (context.getActiveCommand() != null) {
                    method = activeCommandHashMap.get(context.getActiveCommand()).process(context, update);
                } else {
                    method = helpCommand.execute(context);
                }
        }
        sessionRepository.save(context);
        return method;
    }
}
