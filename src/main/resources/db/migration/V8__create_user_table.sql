create table users(
    id bigserial primary key,
    picture varchar(255),
    first_name varchar(250),
    last_name varchar(250),
    birthdate DATE,
    email varchar(250) unique,
    password_hash varchar(255),
    role_id bigint not null,
    constraint fk_user_role foreign key (role_id) references role(id)
);