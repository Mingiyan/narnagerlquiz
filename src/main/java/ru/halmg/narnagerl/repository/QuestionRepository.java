package ru.halmg.narnagerl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.halmg.narnagerl.model.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM question_tbl ORDER BY random() limit 10", nativeQuery = true)
    List<Question> findRandomQuestion();
}
