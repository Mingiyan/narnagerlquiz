package ru.halmg.narnagerl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    private String id;

    private String question;

    private List<Answer> answers;

    private Answer correctAnswer;

    private Set<Tag> tags;
}
