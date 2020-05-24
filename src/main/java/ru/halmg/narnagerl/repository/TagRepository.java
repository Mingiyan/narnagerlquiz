package ru.halmg.narnagerl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.halmg.narnagerl.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {}
