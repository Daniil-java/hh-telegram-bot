package com.education.hh_telegram_bot.repositories;

import com.education.hh_telegram_bot.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findAllByGeneratedDescriptionIsNull();

    List<Vacancy> findAllByNameIsNull();

    List<Vacancy> findAllByNotificationAttemptCountLessThan(int count);
    @Query("SELECT v FROM Vacancy v WHERE v.notificationAttemptCount < :count AND v.generatedDescription IS NOT NULL")
    List<Vacancy> findGeneratedVacanciesWithAttemptsLessThan (@Param("count") int count);

    Optional<Vacancy> findByHhIdAndWorkFilterId(long hhId, long workFilterId);
}
