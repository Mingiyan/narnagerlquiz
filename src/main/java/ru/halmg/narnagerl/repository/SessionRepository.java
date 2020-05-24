package ru.halmg.narnagerl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.halmg.narnagerl.service.command.SessionContext;

@Repository
public interface SessionRepository extends MongoRepository<SessionContext, String>{

    SessionContext findByChatId(Long chatId);
}
