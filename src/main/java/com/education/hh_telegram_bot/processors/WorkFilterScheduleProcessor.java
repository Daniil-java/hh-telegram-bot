package com.education.hh_telegram_bot.processors;


import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.services.WorkFilterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class WorkFilterScheduleProcessor implements ScheduleProcessor{

    private final WorkFilterService workFilterService;
    private final UserService userService;
    private final VacancyService vacancyService;
    private final int MAX_EXCEPTION = 3;

    @Override
    public void process() {
        //TODO: Проверка на неактивные ссылки
        List<UserEntity> userEntityList = userService.getAllUsers();
        List<WorkFilter> workFilterList = new ArrayList<>();
        for (UserEntity userEntity: userEntityList) {
            long userId = userEntity.getId();
            workFilterList.addAll(workFilterService.getAllByUserId(userId));
        }
        List<Vacancy> vacancyList = new ArrayList<>();
        for (WorkFilter workFilter: workFilterList) {
            vacancyList.addAll(parseVacanciesUrl(workFilter));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(getClass().getSimpleName(), e);
            }
        }
        int countException = 0;
        for (Vacancy vacancy: vacancyList) {
            if (countException > MAX_EXCEPTION) {
                log.error(getClass().getSimpleName() + " terminated due to errors");
                break;
            }
            try {
                vacancyService.save(vacancy);
            } catch (Exception e) {
                log.error(getClass().getSimpleName(), e);
                countException++;
            }
        }
    }

    private List<Vacancy> parseVacanciesUrl(WorkFilter workFilter) {
        String url = workFilter.getUrl();
        ArrayList<Vacancy> vacanciesUrlList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("a[data-qa='serp-item__title']");
            for (Element element: elements) {
                String vacancyUrl = element.attr("href");
                log.info(vacancyUrl);
                if (!vacancyUrl.contains("click") && vacancyUrl.indexOf("?") > -1) {
                    vacanciesUrlList.add(new Vacancy()
                            .setUrl(vacancyUrl)
                            .setWorkFilter(workFilter)
                            .setHhId(Long.valueOf(getVacancyIdFromUrl(vacancyUrl)))
                    );
                }
            }
            return vacanciesUrlList;
        } catch (IOException e) {
            log.error(getClass().getSimpleName(), e);
        }
        return vacanciesUrlList;
    }

    private String getVacancyIdFromUrl(String url) {
        if (url.contains("click")) return null;
        return url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getName();
    }

}
