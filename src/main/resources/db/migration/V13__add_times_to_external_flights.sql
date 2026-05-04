alter table external_flight
    add column departure_time timestamp with time zone;

alter table external_flight
    add column arrival_time timestamp with time zone;