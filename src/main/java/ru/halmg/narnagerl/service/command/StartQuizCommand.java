package ru.halmg.narnagerl.service.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.halmg.narnagerl.model.Question;
import ru.halmg.narnagerl.model.Tag;
import ru.halmg.narnagerl.repository.SessionRepository;
import ru.halmg.narnagerl.service.button.AnswerButtonsService;
import ru.halmg.narnagerl.service.QuestionService;
import ru.halmg.narnagerl.service.utils.TelegramUtils;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StartQuizCommand implements Command {

    private final QuestionService questionService;
    private final AnswerButtonsService answerButtonsService;

    @Value("${BOT_TOKEN}")
    private String botToken;


    @Value("${BOT_BASE_URL}")
    private String botBaseUrl;

    @Autowired
    private TelegramUtils telegramUtils;

    @Override
    public String commandDescription() {
        return "Запустить quiz";
    }

    @Override
    public String commandName() {
        return "/startQuiz";
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public BotApiMethod execute(SessionContext context) {
        QuizContext quizContext = new QuizContext();
        context.setQuizContext(quizContext);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startQuiz(quizContext);
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        return new SendMessage(context.getChatId(), question.getQuestion()).setReplyMarkup(answerButtons);
    }

    public BotApiMethod executeWithTag(SessionContext context, String tag) {
        QuizContext quizContextTag = new QuizContext();
        context.setQuizContext(quizContextTag);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startWithTag(quizContextTag, new Tag(tag));
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        return new SendMessage(context.getChatId(), question.getQuestion()).setReplyMarkup(answerButtons);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        Message incomingMsg = telegramUtils.getMessage(update);

        BotApiMethod method;
        Question question = questionService.processQuiz(context.getQuizContext(), update.getCallbackQuery());


        if (question != null) {
            boolean isCorrect = update.getCallbackQuery().getData()
                    .equalsIgnoreCase(context.getQuizContext().getAskedQuestions()
                            .get(context.getQuizContext().getAskedQuestions().size() - 2).getCorrectAnswer().getAnswer());
            String mes;
            if (isCorrect) {
                mes = "\u2705 Чик!";
            } else {
                mes = "\u274c Уга!";
            }
            InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
            sendIsCorrect(context.getChatId(), mes);
            method = new SendMessage(context.getChatId(), question.getQuestion()).setReplyMarkup(answerButtons);
        } else {
            boolean isCorrect = update.getCallbackQuery().getData()
                    .equalsIgnoreCase(context.getQuizContext().getAskedQuestions()
                            .get(context.getQuizContext().getAskedQuestions().size() - 1).getCorrectAnswer().getAnswer());
            String mes;
            if (isCorrect) {
                mes = "\u2705 Чик!";
            } else {
                mes = "\u274c Уга!";
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> button = new ArrayList<>();
            InlineKeyboardButton buttonBack = new InlineKeyboardButton();
            buttonBack.setText("На главную").setCallbackData("/help");
            button.add(buttonBack);
            inlineKeyboardMarkup.setKeyboard(Collections.singletonList(button));
            sendIsCorrect(context.getChatId(), mes);
            SendMessage sendMessage = new SendMessage(incomingMsg.getChatId(),
                    "Правильных ответов " + context.getQuizContext().getCorrectAnswers() + "/" +
                            context.getQuizContext().getAskedQuestions().size()).setReplyMarkup(inlineKeyboardMarkup);
            context.setQuizContext(null);
            context.setActiveCommand(null);
            method = sendMessage;
        }

        return method;
    }

    @SneakyThrows
    private void sendIsCorrect(Long chatId, String message)  {
        TelegramBot.Builder bot = new TelegramBot.Builder(botToken);
        bot.apiUrl(botBaseUrl);
        TelegramBot bot1 = bot.build();
        bot1.execute(new com.pengrad.telegrambot.request.SendMessage(chatId, message));
        Thread.sleep(300);
    }
}
