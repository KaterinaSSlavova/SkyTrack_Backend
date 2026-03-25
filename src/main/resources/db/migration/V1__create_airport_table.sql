create table airport(
    id bigserial primary key,
    iata_code char(3) not null unique,
    name varchar(250),
    city varchar(250),
    country varchar(250)
);