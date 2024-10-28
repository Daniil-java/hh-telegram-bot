package com.education.hhtelegrambot.entities;

import com.education.hhtelegrambot.dtos.hh.HhResponseDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long hhId;
    private String url;
    private String name;
    private String area;
    private String department;
    private String description;
    @Column(name = "generated_description")
    private String generatedDescription;
    private String employment;
    private String experience;
    private String keySkills;
    private String salary;
    private String schedule;
    @Column(name = "notification_attempt_count")
    private int notificationAttemptCount;
    @ManyToOne
    @JoinColumn(name="work_filter_id")
    private WorkFilter workFilter;
    @Enumerated(EnumType.STRING)
    private VacancyStatus status;
    @Column(name = "employer_description")
    private String employerDescription;
    @CreationTimestamp
    private LocalDateTime created;

    public static Vacancy convertDtoToVacancy(HhResponseDto responseDto) {
        Vacancy vacancy = new Vacancy()
                .setUrl(responseDto.getAlternateUrl())
                .setHhId(Long.valueOf(responseDto.getId()))
                .setName(responseDto.getName())
                .setExperience(responseDto.getExperience().getName())
                .setEmployment(responseDto.getEmployment().getName())
                .setDescription(responseDto.getDescription());
        StringBuilder builder = new StringBuilder();
        if (responseDto.getKeySkills() != null) {
            for (String skill: responseDto.getKeySkills()) {
                builder.append(skill).append(" | ");
            }
            vacancy.setKeySkills(builder.toString());
        }

        return vacancy;
    }

}
