package ru.halmg.narnagerl.service.command;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SessionContext {
    @Id
    private String id;
    private Long chatId;
    private CommandType activeCommand;
    private QuizContext quizContext;
}
