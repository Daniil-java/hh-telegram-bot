package com.education.hh_telegram_bot.repositories;

import com.education.hh_telegram_bot.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findAllByGeneratedDescriptionIsNull();

    List<Vacancy> findAllByNameIsNull();

    List<Vacancy> findAllByIsSentFalseAndWorkFilterId(long workFilterId);
}
