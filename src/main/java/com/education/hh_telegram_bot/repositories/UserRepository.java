package com.education.hh_telegram_bot.repositories;

import com.education.hh_telegram_bot.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityByTelegramId(Long telegramId);
}