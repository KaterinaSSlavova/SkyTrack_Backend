alter table flight
add column is_archived boolean not null default false;

alter table airport
add column is_archived boolean not null default false;