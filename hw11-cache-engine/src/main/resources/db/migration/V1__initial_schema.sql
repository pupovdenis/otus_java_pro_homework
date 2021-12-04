-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence hibernate_sequence start with 1 increment by 1;

create table clients (
                         id int8 not null,
                         name varchar(255),
                         address_id int8,
                         primary key (id)
);

create table addresses (
                           id int8 not null,
                           street varchar(255),
                           primary key (id)
);

create table phones (
                        id int8 not null,
                        number varchar(255),
                        client_id int8,
                        primary key (id)
);

