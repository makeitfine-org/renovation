/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

insert into r2.company(name, address) values
    ('Company 1', 'Address 1'),
    ('Company 2', 'Address 2'),
    ('Company 3', 'Address 3');
insert into r2.app_user(email, name, age, company_id) values
    ('email-1@codersee.com', 'John', 23, 1),
    ('email-2@codersee.com', 'Adam', 33, 1),
    ('email-3@codersee.com', 'Maria', 40, 2),
    ('email-4@codersee.com', 'James', 39, 3),
    ('email-5@codersee.com', 'Robert', 41, 3),
    ('email-6@codersee.com', 'Piotr', 28, 3);
