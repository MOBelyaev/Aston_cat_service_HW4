--liquibase formatted sql
--changeset mbelyaev:add_cats_table.sql

create table if not exists users (
    login text primary key,
    name text,
    password bytea
);