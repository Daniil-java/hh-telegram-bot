package com.education.hh_telegram_bot.entities;

import com.education.hh_telegram_bot.entities.hh.HhResponseDto;
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
    private String employment;
    private String experience;
    private String keySkills;
    private String salary;
    private String schedule;
    @ManyToOne
    @JoinColumn(name="link_id")
    private Link link;
    @CreationTimestamp
    private LocalDateTime created;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(url).append("\n");
        builder.append(name).append("\n");
        builder.append(experience).append("\n");
        builder.append(employment).append("\n");
        builder.append(salary);
        return builder.toString();
    }

    public static Vacancy convertDtoToVacancy(HhResponseDto responseDto) {
        Vacancy vacancy = new Vacancy()
                .setName(responseDto.getName())
                .setUrl(responseDto.getResponseUrl())
                .setExperience(responseDto.getExperience().getName())
                .setEmployment(responseDto.getEmployment().getName());
        StringBuilder builder = new StringBuilder();
        if (responseDto.getKeySkills() != null) {
            for (HhResponseDto.Skill skill: responseDto.getKeySkills()) {
                builder.append(skill).append(" | ");
            }
            vacancy.setKeySkills(builder.toString());
        }

        return vacancy;
    }

}
