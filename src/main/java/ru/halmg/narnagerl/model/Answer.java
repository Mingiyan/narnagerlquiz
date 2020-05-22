package ru.halmg.narnagerl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer_tbl")
public class Answer {

    @Id
    @GeneratedValue
    private Long answerId;

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    @Column(name = "correct")
    private boolean isCorrect;
}
