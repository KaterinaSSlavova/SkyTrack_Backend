create table role(
    id bigserial primary key,
    role_name varchar(250) unique
);

insert into role(role_name)
values ('ADMIN'),
       ('PASSENGER');