package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.Link;
import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.repositories.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkService {
    private final LinkRepository linkRepository;

    public Link save(Long userId, String url) {
        return linkRepository.save(new Link()
                .setUser(new UserEntity().setId(userId))
                .setUrl(url)
        );
    }

    public List<Link> saveAll(Long userId, List<String> urlList) {
        List<Link> list = new ArrayList<>();
        UserEntity user = new UserEntity().setId(userId);
        for (String url: urlList) {
            list.add(new Link()
                    .setUser(user)
                    .setUrl(url));
        }
        return linkRepository.saveAll(list);
    }
}
