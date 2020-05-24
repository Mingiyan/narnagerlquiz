package ru.halmg.narnagerl.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.halmg.narnagerl.model.Question;
import ru.halmg.narnagerl.service.QuestionService;
import ru.halmg.narnagerl.service.button.AnswerButtonsService;
import ru.halmg.narnagerl.service.util.TelegramUtils;

@Service
@RequiredArgsConstructor
public class StartQuizCommand implements Command{



    private final QuestionService questionService;
    private final AnswerButtonsService answerButtonsService;

    @Autowired
    private TelegramUtils telegramUtils;

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
        QuizContext quizContext = new QuizContext();
        context.setQuizContext(quizContext);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startQuiz(quizContext);
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        return new SendMessage(context.getChatId(), question.getMessage()).setReplyMarkup(answerButtons);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        Message incomingMsg = telegramUtils.getMessage(update);

        BotApiMethod method;
        Question question = questionService.processQuiz(context.getQuizContext(), update.getCallbackQuery());
        if (question != null) {
            InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
            method = new SendMessage(context.getChatId(), question.getMessage()).setReplyMarkup(answerButtons);
        } else {
            SendMessage sendMessage = new SendMessage(incomingMsg.getChatId(),
                    "Правильных ответов " + context.getQuizContext().getCorrectAnswers() + "/" +
                            context.getQuizContext().getAskedQuestions().size());
            context.setQuizContext(null);
            context.setActiveCommand(null);
            method = sendMessage;
        }

        return method;
    }

    @Override
    public boolean isPublic() {
        return true;
    }
}
