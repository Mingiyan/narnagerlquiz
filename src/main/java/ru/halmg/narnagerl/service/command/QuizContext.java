package ru.halmg.narnagerl.service.command;

import lombok.Data;
import ru.halmg.narnagerl.model.Question;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizContext {
    private List<Question> askedQuestions = new ArrayList<>();
    private int correctAnswers;
    private int quizSize = 10;
}
