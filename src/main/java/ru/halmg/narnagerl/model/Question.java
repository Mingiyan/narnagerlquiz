package ru.halmg.narnagerl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question_tbl")
public class Question {

    @Id
    @GeneratedValue
    private Long questionId;

    @Column(name = "message")
    private String message;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers;

    @Column(name = "level", nullable = false)
    @Enumerated(EnumType.STRING)
    private Level level;
}
