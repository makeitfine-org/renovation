/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

create table if not exists person
(
    id                     serial primary key,
    first_name             varchar(255) not null,
    middle_name            varchar(255),
    last_name              varchar(255) not null,
    address                varchar(255) not null,

    -- technical fields (audit, tracking, etc.)
    row_created_date       timestamp,
    row_last_modified_date timestamp
);
