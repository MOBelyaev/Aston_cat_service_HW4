--liquibase formatted sql
--changeset mbelyaev:add_cats_table.sql

create table if not exists likes(

    id uuid primary key,
    user_id text,
    cat_id text,

    foreign key (user_id) references users(login),
    foreign key (cat_id) references cats(id)
);