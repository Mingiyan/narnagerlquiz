package ru.halmg.narnagerl.service;

import org.springframework.stereotype.Service;
import ru.halmg.narnagerl.model.Question;
import ru.halmg.narnagerl.repository.QuestionRepository;
import ru.halmg.narnagerl.service.command.QuizContext;

import java.util.List;

@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


//    public Question startQuiz(QuizContext quizContext) {
//
//    }

    public List<Question> findRandomTenQuestion() {
        return questionRepository.findRandomQuestion();
    }
}
