package com.education.hhtelegrambot.services.feign;

import com.education.hhtelegrambot.configurations.OpenAiKeyConfiguration;
import com.education.hhtelegrambot.dtos.openai.OpenAiChatCompletionRequest;
import com.education.hhtelegrambot.dtos.openai.OpenAiChatCompletionResponse;
import com.education.hhtelegrambot.integrations.OpenAiFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiApiFeignService {
    private final OpenAiFeignClient openAiFeignClient;
    private final OpenAiKeyConfiguration openAiKeyConfiguration;

    public String generateDescription(String description) {
        //Создание текста запроса для обращение к OpenAI API
        String request = String.format(
                "Я отправляю тебе описание вакансии. " +
                "Сократи описание, передай основные моменты. " +
                "Составляй сообщение от лица компании ", description);
        //Отправка запроса и получение ответа от ИИ
        OpenAiChatCompletionResponse response = openAiFeignClient.generate(
                        "Bearer " + openAiKeyConfiguration.getKey(),
                        OpenAiChatCompletionRequest.makeRequest(request)
                );

        return response.getChoices().get(0).getMessage().getContent();
    }

    public String generateCoverLetter(String vacancyDescription, String userInfo, String companyDescription) {
        //Создание текста запроса для обращение к OpenAI API
        String request = String.format(
                "Помоги мне составить сопроводительное письмо для заявки на работу. " +
                        "Заявка найдена на сайте по поиску вакансий. " +
                        "Используй следующую информацию:\n" +
                        "\n" +
                        "1. Описание вакансии: %s\n" +
                        "2. Описание компании : %s\n" +
                        "3. Информация о соискателе: %s\n" +
                        "\n" +
                        "Требования к письму:\n" +
                        "- Письмо должно быть написано как реклама соискателя, подчеркивая совпадения навыков с требованиями вакансии.\n" +
                        "- Укажите, что я провел исследование компании и понимаю ее цели и ценности.\n" +
                        "- Письмо должно быть кратким, четким и профессиональным.\n" +
                        "- Необязательно использовать всю информацию о пользователе. " +
                        "Попробуй сгладить информацию пользователя, если это необходимо"
                , vacancyDescription, companyDescription, userInfo);
        //Отправка запроса и получение ответа от ИИ
        OpenAiChatCompletionResponse response = openAiFeignClient.generate(
                "Bearer " + openAiKeyConfiguration.getKey(),
                OpenAiChatCompletionRequest.makeRequest(request)
        );

        return response.getChoices().get(0).getMessage().getContent();
    }
}
