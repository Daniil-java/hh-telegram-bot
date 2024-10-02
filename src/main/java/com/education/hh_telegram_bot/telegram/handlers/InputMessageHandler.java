package com.education.hh_telegram_bot.telegram.handlers;

import com.education.hh_telegram_bot.entities.Link;
import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.Vacancy;
import com.education.hh_telegram_bot.services.LinkService;
import com.education.hh_telegram_bot.services.VacancyService;
import com.education.hh_telegram_bot.telegram.TelegramBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InputMessageHandler {
    @Autowired
    private LinkService linkService;
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    @Lazy
    private TelegramBot telegramBot;

    public void processInputMessage(Message message, UserEntity userEntity) {
        Long chatId = message.getChatId();
        String url = message.getText();
        if (isValidated(url)) {
            try {
                Link link = linkService.save(userEntity.getId(), url);
                List<String> vacancyUrlsList = parseVacanciesUrl(url);
                List<Vacancy> vacanciesList = vacancyService.saveAll(link.getId(), vacancyUrlsList);
                //TODO: Убрать заглушку
                telegramBot.sendMessage(SendMessage.builder()
                        .chatId(chatId)
                        .text(vacanciesList.get(0).toString())
                        .build());
//                for (Vacancy vacancy : vacanciesList) {
//                    telegramBot.sendMessage(SendMessage.builder()
//                            .chatId(chatId)
//                            .text(vacancy.toString())
//                            .build());
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isValidated(String text) {
        return text.startsWith("hh.ru/vacancies/")
                || text.startsWith("https://hh.ru/vacancies/");
    }

    private List<String> parseVacanciesUrl(String url) throws IOException {
        ArrayList<String> vacanciesUrlList = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("a[data-qa='serp-item__title']");
        for (Element element: elements) {
            vacanciesUrlList.add(element.attr("href"));
        }
        return vacanciesUrlList;
    }
}
