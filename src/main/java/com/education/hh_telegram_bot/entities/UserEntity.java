package com.education.hh_telegram_bot.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Long telegramId;
    private Long chatId;
    private String firstname;
    private String lastname;
    private String languageCode;
    @OneToMany(mappedBy="user")
    private Set<WorkFilter> workFilters;
    private String info;
    @CreationTimestamp
    private LocalDateTime created;

}
