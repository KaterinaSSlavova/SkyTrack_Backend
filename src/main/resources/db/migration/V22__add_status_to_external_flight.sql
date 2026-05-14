alter table external_flight
add column status varchar(30) default 'SCHEDULED' not null;