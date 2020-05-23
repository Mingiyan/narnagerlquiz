package ru.halmg.narnagerl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.halmg.narnagerl.repository.AnswerRepository;

@Service
public class AnswerService {


    private AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }


}
