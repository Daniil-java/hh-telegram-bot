package com.education.hhtelegrambot.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "work_filters")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class WorkFilter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    @OneToMany(mappedBy = "workFilter")
    private Set<Vacancy> vacancies;
    private String url;
    @CreationTimestamp
    private LocalDateTime created;
}
