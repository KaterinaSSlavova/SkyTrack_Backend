create table external_flight(
    id bigserial primary key,
    flight_number varchar(255),
    departure_iata_code char(3),
    arrival_iata_code char(3),
    departure_timezone varchar(255),
    arrival_timezone varchar(255),
    price decimal(10,2),
    currency varchar(3),
    departure_airport_id BIGINT,
    arrival_airport_id BIGINT,
    constraint fk_external_flight_departure_airport
        foreign key (departure_airport_id)
            references airport(id),

    constraint fk_external_flight_arrival_airport
        foreign key (arrival_airport_id)
            references airport(id)
);