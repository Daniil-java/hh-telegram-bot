--liquibase formatted sql

--changeset DanielK:4

alter table users
    add info text;

alter table vacancies
    add status text default 'PENDING';

alter table vacancies
    add employer_description text;
