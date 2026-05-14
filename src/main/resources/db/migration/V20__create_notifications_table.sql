create table notification(
    id bigserial primary key,
    user_id bigint not null,
    flight_id bigint not null,
    type varchar(50) not null,
    title varchar(255) not null,
    message Text not null,
    is_read boolean not null default false,
    created_at timestamp with time zone not null,
    constraint fk_notifications_user foreign key (user_id) references users(id) on delete cascade,
    constraint fk_notifications_flight foreign key (flight_id) references external_flight(id) on delete cascade
);

create index idx_notifications_user_id
    on notification(user_id);

create index idx_notifications_flight_id
    on notification(flight_id);