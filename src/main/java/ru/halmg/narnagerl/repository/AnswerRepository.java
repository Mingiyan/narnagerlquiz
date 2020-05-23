package ru.halmg.narnagerl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.halmg.narnagerl.model.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
