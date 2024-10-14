package com.education.hh_telegram_bot.services.feign;

import com.education.hh_telegram_bot.configurations.OpenAiKeyConfiguration;
import com.education.hh_telegram_bot.entities.openai.OpenAiChatCompletionRequest;
import com.education.hh_telegram_bot.entities.openai.OpenAiChatCompletionResponse;
import com.education.hh_telegram_bot.integrations.OpenAiFeignClient;
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
        String request = String.format("Выдели суть из следующего описания: %s", description);
        //Отправка запроса и получение ответа от ИИ
        OpenAiChatCompletionResponse response = openAiFeignClient.generate(
                        "Bearer " + openAiKeyConfiguration.getKey(),
                        OpenAiChatCompletionRequest.makeRequest(request)
                );

        return response.getChoices().get(0).getMessage().getContent();
    }
}
