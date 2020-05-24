package ru.halmg.narnagerl.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.halmg.narnagerl.repository.SessionRepository;
import ru.halmg.narnagerl.service.QuestionService;
import ru.halmg.narnagerl.service.button.AnswerButtonsService;
import ru.halmg.narnagerl.service.utils.TelegramUtils;

import java.util.HashMap;

@Component
public class CommandListener {

    private HelpCommand helpCommand;
    private StartQuizCommand startQuizCommand;
    private QuestionService questionService;
    private AnswerButtonsService answerButtonsService;
    private SessionRepository sessionRepository;
    private AddQuestionCommand addQuestionCommand;
    private AddTagCommand addTagCommand;
    private HashMap<CommandType, Command> activeCommandHashMap;
    private TelegramUtils telegramUtils;

    @Autowired
    public CommandListener(HelpCommand helpCommand,
                           StartQuizCommand startQuizCommand,
                           QuestionService questionService,
                           AnswerButtonsService answerButtonsService,
                           SessionRepository sessionRepository,
                           AddQuestionCommand addQuestionCommand,
                           AddTagCommand addTagCommand,
                           HashMap<CommandType, Command> activeCommandHashMap,
                           TelegramUtils telegramUtils) {
        this.helpCommand = helpCommand;
        this.startQuizCommand = startQuizCommand;
        this.questionService = questionService;
        this.answerButtonsService = answerButtonsService;
        this.sessionRepository = sessionRepository;
        this.addQuestionCommand = addQuestionCommand;
        this.addTagCommand = addTagCommand;
        this.activeCommandHashMap = activeCommandHashMap;
        this.telegramUtils = telegramUtils;
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
            case "/addQuestion":
                method = addQuestionCommand.execute(context);
                break;
            case "/addTag":
                method = addTagCommand.execute(context);
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
