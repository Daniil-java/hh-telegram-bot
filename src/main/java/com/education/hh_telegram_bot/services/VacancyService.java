package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.Link;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.repositories.VacancyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyService {
    private final HhApiFeignService hhApiFeignService;
    private final VacancyRepository vacancyRepository;

    public List<Vacancy> saveAll(Long linkId, List<String> urls) {
        return vacancyRepository.saveAll(parseVacanciesFromUrls(linkId, urls));
    }

    private List<Vacancy> parseVacanciesFromUrls(Long linkId, List<String> urls) {
        List<Vacancy> vacancyList = new ArrayList<>();
        for (String url: urls) {
            Vacancy vacancy = parseVacancyInfo(url);
            if (vacancy != null) {
                vacancyList.add(vacancy.setLink(new Link().setId(linkId)));
            }
        }
        return vacancyList;
    }

    private Vacancy parseVacancyInfo(String url) {
        //Заглушка
        if (url.contains("click")) return null;
        String id = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
        return hhApiFeignService.getVacancyById(id);
    }
}
