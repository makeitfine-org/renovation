/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

create table if not exists "work"
(
    id       bigserial primary key,
    title    varchar(120) not null,
    "desc"   varchar(480),
    end_date date,
    price    float,
    pay_date date
);

create table if not exists material
(
    id           bigserial primary key,
    title        varchar(120) not null,
    "desc"       varchar(480),
    deliver_date date,
    price        float,
    pay_date     date
);
