package com.education.hh_telegram_bot.processors;


import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.services.UserService;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.services.WorkFilterService;
import com.education.hh_telegram_bot.utils.ThreadUtil;
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
            for (Vacancy vacancy: loadAndParseHhVacancies(workFilter)) {
                vacancyService.save(vacancy);
            }
            ThreadUtil.sleep(1000, "WorkFilterScheduler: thread sleep error");
        }
    }

    private List<Vacancy> loadAndParseHhVacancies(WorkFilter workFilter) {
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
            log.error("WorkFilterScheduleProcessor: Jsoup connection error!", e);
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
