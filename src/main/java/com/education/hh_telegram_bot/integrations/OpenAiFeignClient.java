package com.education.hh_telegram_bot.integrations;

import com.education.hh_telegram_bot.entities.openai.OpenAiChatCompletionRequest;
import com.education.hh_telegram_bot.entities.openai.OpenAiChatCompletionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
@FeignClient(
        value = "open-ai-feign-client",
        url = "${integrations.openai-api.url}"
)
public interface OpenAiFeignClient {
    @PostMapping("chat/completions")
    OpenAiChatCompletionResponse generate(@RequestHeader("Authorization") String key,
                                          @RequestBody OpenAiChatCompletionRequest request);
}
