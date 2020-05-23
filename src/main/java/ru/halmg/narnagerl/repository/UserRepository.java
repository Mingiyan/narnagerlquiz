package ru.halmg.narnagerl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.halmg.narnagerl.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
