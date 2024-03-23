CREATE TABLE books
(
    id       uuid primary key default gen_random_uuid(),
    title    varchar(300) not null,
    author   varchar(300) not null,
    quantity integer      not null,
    isbn     varchar(15)  not null
);