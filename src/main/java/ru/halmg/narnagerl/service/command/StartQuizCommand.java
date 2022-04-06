package ru.halmg.narnagerl.service.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
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
import ru.halmg.narnagerl.service.QuestionService;
import ru.halmg.narnagerl.service.button.AnswerButtonsService;
import ru.halmg.narnagerl.service.utils.TelegramUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        return new SendMessage(context.getChatId(), "1. " + question.getQuestion()).setReplyMarkup(answerButtons);
    }

    public BotApiMethod executeWithTag(SessionContext context, String tag) {
        QuizContext quizContextTag = new QuizContext();
        context.setQuizContext(quizContextTag);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startWithTag(quizContextTag, new Tag(tag));
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        return new SendMessage(context.getChatId(), "1. " + question.getQuestion()).setReplyMarkup(answerButtons);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        Message incomingMsg = telegramUtils.getMessage(update);

        BotApiMethod method;
        Question question = questionService.processQuiz(context.getQuizContext(), update.getCallbackQuery());


        if (question != null) {
            String correctAnswer = context.getQuizContext().getAskedQuestions()
                    .get(context.getQuizContext().getAskedQuestions().size() - 2).getCorrectAnswer().getAnswer();
            boolean isCorrect = update.getCallbackQuery().getData()
                    .equalsIgnoreCase(correctAnswer);
            String mes;
            if (isCorrect) {
                mes = "\u2705 Чик!";
            } else {
                mes = "\u274c Уга!";
            }
            InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
            sendIsCorrect(context.getChatId(), mes + "\n<b>" + correctAnswer + "</b>");
            method = new SendMessage(context.getChatId(), context.getQuizContext().getAskedQuestions().size()+ ". " + question.getQuestion()).setReplyMarkup(answerButtons);
        } else {
            String correctAnswer = context.getQuizContext().getAskedQuestions()
                    .get(context.getQuizContext().getAskedQuestions().size() - 1).getCorrectAnswer().getAnswer();
            boolean isCorrect = update.getCallbackQuery().getData()
                    .equalsIgnoreCase(correctAnswer);
            String mes;
            if (isCorrect) {
                mes = "\u2705 Чик!";
            } else {
                mes = "\u274c Уга!";
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> button = new ArrayList<>();
            InlineKeyboardButton buttonBack = new InlineKeyboardButton();
            buttonBack.setText("Ардагшан").setCallbackData("/help");
            button.add(buttonBack);
            inlineKeyboardMarkup.setKeyboard(Collections.singletonList(button));
            sendIsCorrect(context.getChatId(), mes + "\n<b>" + correctAnswer + "</b>");
            SendMessage sendMessage = new SendMessage(incomingMsg.getChatId(),
                    "Чик хәрү " + context.getQuizContext().getCorrectAnswers() + "/" +
                            context.getQuizContext().getAskedQuestions().size() + "\n\n" + getRandomMessage()).setReplyMarkup(inlineKeyboardMarkup);
            context.setQuizContext(null);
            context.setActiveCommand(null);
            method = sendMessage;
        }

        return method;
    }

    @SneakyThrows
    private void sendIsCorrect(Long chatId, String message)  {
        TelegramBot.Builder botBuilder = new TelegramBot.Builder(botToken);
        botBuilder.apiUrl(botBaseUrl);
        TelegramBot bot = botBuilder.build();
        com.pengrad.telegrambot.request.SendMessage request = new com.pengrad.telegrambot.request.SendMessage(chatId, message);
        request.parseMode(ParseMode.HTML);
        bot.execute(request);
        Thread.sleep(300);
    }

    private String getRandomMessage() {
        String[] messages = {"Сурврмуд болн седвәр бәәхлә, NarnaGerl гидг Instagram-д орад бичтн, буйн болтха!\n" +
                "Если у вас есть вопросы и предложения, пишите в Instagram, пожалуйста!\n https://www.instagram.com/narnagerl/",
        "Энд бәәсн цуг үгмүд мана NarnaGerl гидг Youtube болн Instagram-халхт олҗ чадхвт! Маниг дахтн!\n" +
                "Все слова из тестов в Телеграм вы сможете найти на наших страницах в Instagram и Youtube!\n Подписывайтесь на нас!\n " +
                "https://www.instagram.com/narnagerl/\n https://www.youtube.com/channel/UCYXm3kbhD68c7d55pR4mX2Q"};

        return messages[new Random().nextInt(messages.length)];
    }
}
