package com.education.hh_telegram_bot.repositories;

import com.education.hh_telegram_bot.entities.WorkFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkFilterRepository extends JpaRepository<WorkFilter, Long> {

}
