package ru.halmg.narnagerl.service;

import org.springframework.stereotype.Service;
import ru.halmg.narnagerl.repository.QuestionRepository;

@Service
public class QuestionService {

    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


}
