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

insert into "work" (title, "desc", end_date, price, pay_date)
values ('air condition installation', '', '2021-10-15', 2500, '2021-10-15'),
       ('pipe installation', 'Andery from Bila Cerkva did it', '2021-10-25', 8000, '2021-10-30'),
       ('plaster', 'Vasyl did it', '2021-11-10', null, null),
       ('tile sticker', '', '2021-12-01', 33000, '2021-12-05'),
       ('electrical wiring', '', '2021-12-10', 8000, null);

create table if not exists materials
(
    id           bigserial primary key,
    title        varchar(120) not null,
    "desc"       varchar(480),
    deliver_date date,
    price        float,
    pay_date     date
);

insert into materials (title, "desc", deliver_date, price, pay_date)
values ('plaster', 'plaster bags and other', '2021-10-15', 5000, '2021-10-20'),
       ('wires', '', '2021-10-20', null, null),
       ('bathroom furniture', 'Bought it in Lerua Merlen', '2021-11-10', 6000, '2021-11-08');
