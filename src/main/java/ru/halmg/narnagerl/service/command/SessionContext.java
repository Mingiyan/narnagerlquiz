package ru.halmg.narnagerl.service.command;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SessionContext {
    @Id
    private String id;
    private Long chatId;
    private boolean isQuizActive;
    private boolean isAnswerActive;
    private boolean isTagActive;
    private QuizContext quizContext;
}