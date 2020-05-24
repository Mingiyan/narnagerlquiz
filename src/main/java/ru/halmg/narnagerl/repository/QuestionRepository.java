package ru.halmg.narnagerl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.halmg.narnagerl.model.Question;

public interface QuestionRepository extends MongoRepository<Question, String> {}
