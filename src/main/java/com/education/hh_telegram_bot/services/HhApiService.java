package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.entities.hh.HhSimpleResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class HhApiService {

    //Загрузка страницы, с использованием Jsoup, и парсинг результатов в сущности
    public List<HhSimpleResponseDto> loadAndParseHhVacancies(WorkFilter workFilter) {
        String url = workFilter.getUrl();
        ArrayList<HhSimpleResponseDto> hhSimpleResponseDtos = new ArrayList<>();
        try {
            //Получение страницы
            Document document = Jsoup.connect(url).get();
            //Выборка необходимых элементов(вакансий) страницы
            Elements elements = document.select("a[data-qa='serp-item__title']");
            for (Element element: elements) {
                //Получение ссылки на вакансию
                String vacancyUrl = element.attr("href");
                //Валидация полученной ссылки
                if (!vacancyUrl.contains("click") && vacancyUrl.indexOf("?") > -1) {
                    //Создание ДТО вакансии и добавление в возвращаемый список
                    hhSimpleResponseDtos.add(new HhSimpleResponseDto()
                            .setHhId(getVacancyIdFromUrl(vacancyUrl))
                            .setUrl(vacancyUrl)
                    );
                }
            }
        } catch (IOException e) {
            log.error("HhApiService: Jsoup connection error!", e);
        }
        return hhSimpleResponseDtos;
    }


    //Получение id-вакансии из ссылка на вакансию
    private Long getVacancyIdFromUrl(String url) {
        return Long.valueOf(url.substring(
                        url.lastIndexOf("/") + 1, url.indexOf("?")));
    }


}
