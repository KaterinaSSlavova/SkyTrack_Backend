create table flight_status(
    id bigserial primary key,
    name varchar(250) not null unique
);

insert into flight_status(name)
values ('SCHEDULED'),
       ('DELAYED'),
       ('CANCELLED'),
       ('DEPARTED'),
       ('LANDED'),
       ('BOARDING');