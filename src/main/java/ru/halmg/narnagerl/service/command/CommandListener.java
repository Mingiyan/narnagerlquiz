package ru.halmg.narnagerl.service.command;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Component
public class CommandListener {

    private HelpCommand helpCommand;
    private StartQuizCommand startQuizCommand;
    private QuestionService questionService;
    private AnswerButtonsService answerButtonsService;
    private SessionRepository sessionRepository;


    @Autowired
    public CommandListener(HelpCommand helpCommand,
                           StartQuizCommand startQuizCommand,
                           QuestionService questionService,
                           AnswerButtonsService answerButtonsService,
                           SessionRepository sessionRepository) {
        this.helpCommand = helpCommand;
        this.startQuizCommand = startQuizCommand;
        this.questionService = questionService;
        this.answerButtonsService = answerButtonsService;
        this.sessionRepository = sessionRepository;

    }

    public BotApiMethod getCommand(Update update) throws TelegramApiRequestException {
        Message incomingMsg;

        if (!update.hasMessage()) {
            incomingMsg = update.getCallbackQuery().getMessage();
        } else {
            incomingMsg = update.getMessage();
        }

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
                context.setAnswerActive(false);
                method = addQuestionCommand.execute(context);
                break;
            case "/addTag":
                context.setTagActive(false);
                method = addTagCommand.execute(context);
                break;
            default:
                if (context.isQuizActive() && !context.isAnswerActive()) {
                    Question question = questionService.processQuiz(context.getQuizContext(), update.getCallbackQuery());
                    if (question != null) {
                        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
                        method = new SendMessage(context.getChatId(), question.getQuestion()).setReplyMarkup(answerButtons);
                    } else {
                        SendMessage sendMessage = new SendMessage(incomingMsg.getChatId(),
                                "Правильных ответов " + context.getQuizContext().getCorrectAnswers() + "/" +
                                        context.getQuizContext().getAskedQuestions().size());
                        context.setQuizActive(false);
                        context.setQuizContext(null);
                        method = sendMessage;
                    }
                } else if (!context.isQuizActive() && context.isAnswerActive()) {
                    method = addQuestionCommand.process(context, incomingMsg.getText());
                } else if (context.isTagActive() && !context.isQuizActive()) {
                    method = addTagCommand.process(context, incomingMsg.getText());
                }
                else {
                    method = helpCommand.execute(context);
                }
        }
        sessionRepository.save(context);
        return method;
    }
}
