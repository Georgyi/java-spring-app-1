package com.example.jobparser.database.repository;

import com.example.jobparser.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    boolean existsUserByTelegramId(Long telegramId);
}
