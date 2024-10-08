--liquibase formatted sql

--changeset DanielK:2

ALTER TABLE vacancies
    ADD COLUMN generated_description text,
    ADD COLUMN is_sent boolean DEFAULT false;