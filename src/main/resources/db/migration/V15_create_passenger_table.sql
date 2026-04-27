create table passenger(
    id bigint primary key,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255),
    gender varchar(15),
    passport_number varchar(20),
    date_of_birth Date,
    passport_expiry Date,
    nationality varchar(255)
);

alter table booking
    add column passenger_id bigint;

alter table booking
    add constraint fk_booking_passenger foreign key (passenger_id) references passenger(id);