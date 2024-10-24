package com.education.hhtelegrambot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VacancyStatus {
    APPLIED,
    REJECTED,
    PENDING,
    NOTIFICATED,

    CREATED,
    PARSED,
    PROCESSED,
    NOTIFICATION_ERROR;
}
