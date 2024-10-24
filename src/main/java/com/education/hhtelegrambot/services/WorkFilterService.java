package com.education.hhtelegrambot.services;

import com.education.hhtelegrambot.entities.WorkFilter;
import com.education.hhtelegrambot.entities.UserEntity;
import com.education.hhtelegrambot.entities.hh.HhSimpleResponseDto;
import com.education.hhtelegrambot.repositories.WorkFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkFilterService {
    private final WorkFilterRepository workFilterRepository;
    private final HhApiService hhApiService;

    public WorkFilter create(UserEntity userEntity, String url) {
        return workFilterRepository.findByUserAndUrl(userEntity, url)
                .orElseGet(() -> workFilterRepository.save(
                        new WorkFilter()
                                .setUser(userEntity)
                                .setUrl(url)
                        )
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
