package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.WorkFilter;
import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.repositories.WorkFilterRepository;
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

    public WorkFilter save(Long userId, String url) {
        return workFilterRepository.save(new WorkFilter()
                .setUser(new UserEntity().setId(userId))
                .setUrl(url)
        );
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
}
