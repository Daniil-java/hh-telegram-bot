--liquibase formatted sql

--changeset DanielK:1

CREATE TABLE IF NOT EXISTS users
(
    id              serial PRIMARY KEY,
    telegram_id     bigint NOT NULL UNIQUE,
    chat_id         bigint NOT NULL UNIQUE,
    username        text,
    firstname       text,
    lastname        text,
    language_code   text,
    created         timestamp
);

CREATE TABLE IF NOT EXISTS links
(
    id              serial PRIMARY KEY,
    user_id         bigint NOT NULL,
    url             text,
    created         timestamp,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS vacancies
(
    id              serial PRIMARY KEY,
    hh_id           bigint,
    url             text,
    name            text,
    area            text,
    department      text,
    description     text,
    employment      text,
    experience      text,
    key_skills      text,
    salary          text,
    schedule        text,
    link_id         bigint NOT NULL,
    created         timestamp,
    CONSTRAINT fk_link FOREIGN KEY (link_id) REFERENCES links(id)
    );

CREATE INDEX telegram_id ON users(telegram_id);