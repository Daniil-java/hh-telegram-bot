package com.education.hh_telegram_bot.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "links")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    @OneToMany(mappedBy = "link")
    private Set<Vacancy> vacancies;
    private String url;
    @CreationTimestamp
    private LocalDateTime created;
}
