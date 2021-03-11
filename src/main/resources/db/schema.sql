create table users
(
    id         bigserial constraint users_pk primary key,
    email      varchar(255)                 not null,
    first_name varchar(50)                  not null,
    last_name  varchar(100)                 not null,
    password   varchar(255)                 not null,
    role       varchar(20) default 'USER'   not null,
    status     varchar(20) default 'ACTIVE' not null
);

create unique index users_email_uindex
    on users (email);

