# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table task (
  id                        bigint not null,
  label                     varchar(255),
  id_user_email             varchar(255),
  constraint pk_task primary key (id))
;

create table account (
  email                     varchar(255) not null,
  password                  varchar(255),
  role                      integer,
  constraint pk_account primary key (email))
;

create sequence task_seq;

create sequence account_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists task;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists task_seq;

drop sequence if exists account_seq;

