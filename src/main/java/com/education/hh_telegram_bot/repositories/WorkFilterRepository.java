package com.education.hh_telegram_bot.repositories;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.WorkFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkFilterRepository extends JpaRepository<WorkFilter, Long> {
    List<WorkFilter> findAllByUserId(long userId);

    Optional<WorkFilter> findByUserIdAndUrl(Long userId, String url);
    Optional<WorkFilter> findByUserAndUrl(UserEntity userEntity, String url);
}
