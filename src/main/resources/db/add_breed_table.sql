--liquibase formatted sql
--changeset mbelyaev:add_breed_table.sql

create table if not exists breed (
    id text primary key ,
    weight text,
    name text,
    temperament text,
    origin text,
    description text,
    lifeSpan text
);
