package ru.halmg.narnagerl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.halmg.narnagerl.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
