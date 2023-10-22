--liquibase formatted sql
--changeset mbelyaev:add_cats_table.sql

create table if not exists cats (
    id text primary key ,
    image_url text,
    image_width text,
    image_height text
);