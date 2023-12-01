/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

create schema if not exists r2;

create table if not exists r2.company(
    id serial not null primary key,
    name varchar(255) not null,
    address varchar(255) not null
);

create table if not exists r2.app_user(
    id serial not null primary key,
    email varchar(255) not null unique,
    name varchar(255) not null,
    age int not null,
    company_id bigint not null references r2.company(id) on delete cascade
);
