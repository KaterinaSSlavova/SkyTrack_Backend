create table extras(
    id bigserial primary key,
    name varchar(255) unique,
    price decimal(10,2)
);

insert into extras (name, price)
values ('window', 10.00),
       ('extra_legroom', 25.00);

alter table booking
   add column is_archived boolean not null default false;

alter table external_flight
   add column external_id varchar(255)