package com.education.hh_telegram_bot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VacancyStatus {
    APPLIED,
    REJECTED,
    PENDING,

    CREATED,
    PARSED,
    PROCESSED,
    NOTIFICATION_ERROR;
}
