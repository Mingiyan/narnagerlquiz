package ru.halmg.narnagerl.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.halmg.narnagerl.model.Answer;
import ru.halmg.narnagerl.model.Question;
import ru.halmg.narnagerl.model.Tag;
import ru.halmg.narnagerl.service.QuestionService;
import ru.halmg.narnagerl.service.utils.TelegramUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AddQuestionCommand implements Command {

    private QuestionService questionService;
    private TelegramUtils telegramUtils;

    @Autowired
    public AddQuestionCommand(QuestionService questionService, TelegramUtils telegramUtils) {
        this.questionService = questionService;
        this.telegramUtils = telegramUtils;
    }

    private int counter = 0;
    private Question questionAnswer;
    private List<Answer> answerList;
    private Set<Tag> tagSet;

    @Override
    public String commandDescription() {
        return "Добавить новый вопрос";
    }

    @Override
    public String commandName() {
        return "/addQuestion";
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public BotApiMethod execute(SessionContext context) {
        String msg;
        if (context.getActiveCommand() == CommandType.ADD_QUESTION) {
            context.setActiveCommand(null);
            msg = "Ввод вопроса закончен.";
        } else {
            context.setActiveCommand(CommandType.ADD_QUESTION);
            msg = "Введите вопрос:";
            questionAnswer = new Question();
            answerList = new ArrayList<>();
            tagSet = new HashSet<>();
            counter=0;
        }
        return new SendMessage(context.getChatId(), msg);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        String msg = telegramUtils.getMessageText(update);

        BotApiMethod method = null;

        if (counter < 1) {
            questionAnswer.setQuestion(msg);
            method = new SendMessage(context.getChatId(), "Введите ответ:");
            counter++;
        } else if (0 < counter && counter < 5) {
            Answer answer = new Answer();
            answer.setAnswer(msg);
            answerList.add(answer);
            if (counter == 4) {
                questionAnswer.setAnswers(answerList);
                method = new SendMessage(context.getChatId(), "Введите правильный вариант ответа цифрой от 1 до 4:");
            } else {
                method = new SendMessage(context.getChatId(), "Введите ответ:");
            }
            counter++;
        } else if (counter == 5) {
            questionAnswer.setCorrectAnswer(answerList.get(Integer.valueOf(msg) - 1));
            method = new SendMessage(context.getChatId(), "Введите теги через запятую:");
            counter++;
        } else if (counter == 6) {
            String[] arrayTags = msg.split(",");
            for (String tag : arrayTags) {
                tagSet.add(new Tag(tag));
            }
            questionAnswer.setTags(tagSet);
            questionService.saveQuestion(questionAnswer);
            method = execute(context);
        } else {
            context.setActiveCommand(null);
        }
        return method;
    }
}
