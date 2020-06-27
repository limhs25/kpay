drop table if exists person;

drop sequence if exists global_seq;

create sequence global_seq start 1000;

create table person
(
    id integer primary key default nextval('global_seq'),
    first_name varchar(40) not null,
    last_name varchar(40) not null,
    date_of_birth date not null,
    email varchar(50) not null,
    auto_email_greetings bool default false not null
);

create unique index person_unique_email_index on person(email);
