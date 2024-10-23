package com.education.hh_telegram_bot.integrations;

import com.education.hh_telegram_bot.entities.hh.HhEmployerDto;
import com.education.hh_telegram_bot.entities.hh.HhResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        value = "hh-feign-client",
        url = "${integrations.hh-api.url}"
)
public interface HhFeignClient {

    @GetMapping("/vacancies/{vacancyId}")
    HhResponseDto getVacancyById(@PathVariable Long vacancyId);

    @GetMapping("/employers/{employerId}")
    HhEmployerDto getEmployerById(@PathVariable Long employerId);
}
