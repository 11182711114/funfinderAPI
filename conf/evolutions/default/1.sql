# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user (
  iduser                        integer auto_increment not null,
  firstname                     varchar(255),
  lastname                      varchar(255),
  age                           integer,
  email                         varchar(255),
  constraint pk_user primary key (iduser)
);


# --- !Downs

drop table if exists user;

