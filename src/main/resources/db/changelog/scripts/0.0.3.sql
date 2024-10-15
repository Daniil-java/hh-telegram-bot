--liquibase formatted sql

--changeset DanielK:3
ALTER TABLE vacancies
    RENAME COLUMN is_sent TO notification_attempt_count;

ALTER TABLE vacancies
    ALTER COLUMN notification_attempt_count DROP DEFAULT;

ALTER TABLE vacancies
ALTER COLUMN notification_attempt_count TYPE int USING CASE
    WHEN notification_attempt_count
    THEN 9999 ELSE 0 END;

ALTER TABLE vacancies
    ALTER COLUMN notification_attempt_count SET DEFAULT 0;