--liquibase formatted sql
--changeset mbelyaev:add_breed_table.sql

create table if not exists cat_breed (
    cat_id text,
    breed_id text,
    foreign key (cat_id) references cats(id),
    foreign key (breed_id) references breed(id),
    primary key (cat_id, breed_id)
);
