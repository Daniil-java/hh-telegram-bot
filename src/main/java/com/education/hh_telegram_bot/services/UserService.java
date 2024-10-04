package com.education.hh_telegram_bot.services;

import com.education.hh_telegram_bot.entities.UserEntity;
import com.education.hh_telegram_bot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity getOrCreateUser(User userInfo) {
        long telegramId = userInfo.getId();
        Optional<UserEntity> userEntity = userRepository.findUserEntityByTelegramId(telegramId);
        if (userEntity.isPresent()) {
            return userEntity.get();
        } else {
            return userRepository.save(
                    new UserEntity()
                    .setTelegramId(telegramId)
                    .setChatId(userInfo.getId())
                    .setUsername(userInfo.getUserName())
                    .setFirstname(userInfo.getFirstName())
                    .setLastname(userInfo.getLastName())
                    .setLanguageCode(userInfo.getLanguageCode())
            );
        }
    }
}
