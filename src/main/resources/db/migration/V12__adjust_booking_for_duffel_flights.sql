alter table booking
drop constraint if exists fk_flight_booking;

alter table booking
drop column flight_id;

alter table booking
    add column external_flight_id BIGINT not null;

alter table booking
    add column currency varchar(3);

alter table booking
    add constraint fk_booking_external_flight
        foreign key (external_flight_id)
            references external_flight(id);

alter table seat
alter column seat_number type varchar(10)
    using seat_number::varchar;