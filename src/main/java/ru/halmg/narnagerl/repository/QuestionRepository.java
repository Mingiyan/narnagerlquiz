package ru.halmg.narnagerl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.halmg.narnagerl.model.Question;
import ru.halmg.narnagerl.model.Tag;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findAllByTags(Tag tag);
}
