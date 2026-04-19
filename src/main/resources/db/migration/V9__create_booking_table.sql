create table booking(
   id bigserial primary key,
   user_id bigint not null,
    flight_id bigint not null,
    base_price decimal(10,2),
    total_price decimal(10,2),
    booking_reference varchar(255),
   constraint fk_user_booking foreign key (user_id) references users(id),
   constraint fk_flight_booking foreign key (flight_id) references flight(id)
);