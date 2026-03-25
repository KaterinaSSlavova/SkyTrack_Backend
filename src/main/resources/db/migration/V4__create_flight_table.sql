create table flight(
    id bigserial primary key,
    flight_number varchar(20) not null unique,
    dep_airport_id bigint not null,
    arr_airport_id bigint not null,
    dep_time_utc timestamptz not null,
    arr_time_utc timestamptz not null,
    gate varchar(250) not null,
    terminal varchar(250) not null,
    capacity int not null,
    price decimal(10,2) not null,
    flight_status_id bigint not null,
    created_at timestamptz not null default now(),
    constraint fk_dep_airport foreign key (dep_airport_id) references airport(id),
    constraint fk_arr_airport foreign key (arr_airport_id) references airport(id),
    constraint fk_flight_status foreign key (flight_status_id) references flight_status(id)
);