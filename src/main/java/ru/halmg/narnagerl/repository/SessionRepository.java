package ru.halmg.narnagerl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.halmg.narnagerl.service.command.SessionContext;

public interface SessionRepository extends JpaRepository<SessionContext, Long> {

    SessionContext findByChatId(Long id);
}
