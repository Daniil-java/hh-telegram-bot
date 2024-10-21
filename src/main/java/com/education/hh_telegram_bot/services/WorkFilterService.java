package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.entities.hh.HhSimpleResponseDto;
import com.education.hh_telegram_bot.repositories.WorkFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkFilterService {
    private final WorkFilterRepository workFilterRepository;
    private final HhApiService hhApiService;

    public WorkFilter create(Long userId, String url) {
        Optional<WorkFilter> optionalWorkFilter =
                workFilterRepository.findByUserIdAndUrl(userId, url);
        if (optionalWorkFilter.isPresent()) {
            return optionalWorkFilter.get();
        }

        return workFilterRepository.save(
                new WorkFilter()
                        .setUser(new UserEntity().setId(userId))
                        .setUrl(url)
        );
    }

    public List<WorkFilter> getAllByUserId(long userId) {
        return workFilterRepository.findAllByUserId(userId);
    }

    public List<WorkFilter> saveAll(Long userId, List<String> urlList) {
        List<WorkFilter> list = new ArrayList<>();
        UserEntity user = new UserEntity().setId(userId);
        for (String url: urlList) {
            list.add(new WorkFilter()
                    .setUser(user)
                    .setUrl(url));
        }
        return workFilterRepository.saveAll(list);
    }

    public List<WorkFilter> getAll() {
        return workFilterRepository.findAll();
    }

    //Получение ДТО вакансий, по переденной ссылке
    public List<HhSimpleResponseDto> loadHhVacancies(WorkFilter workFilter) {
        return hhApiService.loadAndParseHhVacancies(workFilter);
    }
}
