package ru.halmg.narnagerl.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.halmg.narnagerl.model.Question;
import ru.halmg.narnagerl.repository.QuestionRepository;
import ru.halmg.narnagerl.service.command.QuizContext;

import java.util.List;
import java.util.Random;

@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    public Question startQuiz(QuizContext quizContext) {
        Question question = getRandomQuestion();
        quizContext.getAskedQuestions().add(question);
        return question;
    }

    public Question processQuiz(QuizContext quizContext, CallbackQuery callbackQuery) {
        Question lastQuestion = quizContext.getAskedQuestions().get(quizContext.getAskedQuestions().size() - 1);
        boolean isCorrectAnswer = lastQuestion.getCorrectAnswer().getAnswer().equalsIgnoreCase(callbackQuery.getData());
        if (isCorrectAnswer) {
            quizContext.setCorrectAnswers(quizContext.getCorrectAnswers() + 1);
        }

        if (quizContext.getAskedQuestions().size() == quizContext.getQuizSize()) {
            //todo very bad
            return null;
        } else {
            Question question = getRandomQuestion();
            quizContext.getAskedQuestions().add(question);
            return question;
        }
    }

    private Question getRandomQuestion() {
        Random random = new Random();
        List<Question> questionList = findRandomTenQuestion();
        int number = random.nextInt(questionList.size());
        Question question = questionList.get(number);
        questionList.remove(number);
        return question;
    }

    public List<Question> findRandomTenQuestion() {
        return questionRepository.findRandomQuestion();
    }
}
