-- create table users
-- (
--     username varchar_ignorecase(50)  not null,
--     password varchar_ignorecase(500) not null,
--     enabled  boolean                 not null default true,
--     primary key (username)
-- );
--
-- create table authorities
-- (
--     username  varchar_ignorecase(50) not null,
--     authority varchar_ignorecase(50) not null,
--     constraint fk_authorities_users foreign key (username) references users (username)
-- );
-- create unique index ix_auth_username on authorities (username, authority);



create table Users
(
    id       integer generated by default as identity,
    username varchar_ignorecase(50)  unique not null,
    password varchar_ignorecase(500) not null,
    enabled  boolean                 not null default true,
    primary key (id)
);

create table Authorities
(
    id        integer generated by default as identity,
    username  varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    primary key (id),
    constraint fk_authorities_users foreign key (id) references Users (id)
);
create unique index ix_auth_username on Authorities (username, authority);


create table UsersDetails
(
    id        integer generated by default as identity,
    firstName varchar(200) not null,
    lastName  varchar(200) not null,
    birthDate date         not null,
    email     varchar_ignorecase(500) not null,
    telephone varchar_ignorecase(500) not null,
    primary key (id)
);

create table Paintings
(
    id          integer generated by default as identity,
    title       varchar(200)  not null,
    published   date          not null,
    imageLink   varchar(700)  not null,
    size        varchar(255)  not null,
    material    varchar(255)  not null,
    description varchar(2000) not null,
    price       decimal(7, 2) not null,
    sold        boolean       not null default true,
    primary key (id)
);

create table Categories
(
    id   integer generated by default as identity,
    name varchar(255) unique,
    primary key (id)
);

create table Authors
(
    id        integer generated by default as identity,
    pseudonym varchar(255)  not null,
    firstName varchar(255)  not null,
    surname   varchar(255)  not null,
    birthDate date          not null,
    awards    varchar(2000) not null,
    imageLink varchar(700)  not null,
    primary key (id)
);

create table Comments
(
    id      integer generated by default as identity,
    content varchar(1000) not null,
    primary key (id)
);

create table Bets
(
    id          integer generated by default as identity,
    createdDate datetime      not null,
    amountMoney decimal(7, 2) not null,
    active      boolean       not null default true,
    primary key (id)
);

create table Auctions
(
    id            integer generated by default as identity,
    titleAuction  varchar(255),
    startDate     datetime,
    endDate       datetime,
    startingPrice decimal(7, 2),
    currentBet  decimal(7, 2),
    active        boolean default true,
    primary key (id)
);

