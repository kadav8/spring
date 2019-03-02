drop table if exists reservations;
drop table if exists customers;

create table customers (
  id    bigint(10) auto_increment not null primary key,
  name  varchar(255)              not null,
  email varchar(255)              null
);

create table reservations (
  id          bigint(10) auto_increment not null primary key,
  sku         varchar(255)              not null,
  customers   bigint                    not null references customers (id)
);