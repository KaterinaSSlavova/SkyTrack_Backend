create table visited_country(
    id bigserial primary key,
    user_id bigint not null,
    country_code varchar(3) not null,
    country_name varchar(100) not null,
    constraint fk_visited_country_user foreign key (user_id) references users(id) on delete cascade,
    constraint uk_user_country unique (user_id, country_code)
);

