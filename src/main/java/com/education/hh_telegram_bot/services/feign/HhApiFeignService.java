package com.education.hh_telegram_bot.services.feign;

import com.education.hh_telegram_bot.entities.hh.HhResponseDto;
import com.education.hh_telegram_bot.integrations.HhFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class HhApiFeignService {

    private final HhFeignClient hhFeignClient;

    public HhResponseDto getVacancyByHhId(Long vacancyId) {
        return hhFeignClient.getVacancyById(vacancyId);
    }


}
