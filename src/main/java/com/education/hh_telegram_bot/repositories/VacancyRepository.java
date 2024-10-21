package com.education.hh_telegram_bot.repositories;

import com.education.hh_telegram_bot.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findAllByGeneratedDescriptionIsNullAndDescriptionIsNotNull();

    List<Vacancy> findAllByNameIsNull();

    List<Vacancy> findAllByNotificationAttemptCountLessThan(int count);
    @Query("SELECT v FROM Vacancy v WHERE v.notificationAttemptCount < :count AND v.generatedDescription IS NOT NULL")
    List<Vacancy> findGeneratedVacanciesWithAttemptsLessThan (@Param("count") int count);

    Optional<Vacancy> findByHhIdAndWorkFilterId(long hhId, long workFilterId);

    Optional<Vacancy> findByHhIdAndDescriptionNotNull(long hhId);

    Optional<Vacancy> findByIdAndDescriptionNotNull(long vacancyId);

    @Modifying
    @Transactional
    @Query("UPDATE Vacancy v SET v.status = :status WHERE v.id = :id")
    void updateStatusByHhId(@Param("id") Long hhId, @Param("status") String status);
}
