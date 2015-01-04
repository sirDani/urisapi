# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table my_user (
  id                        bigint not null,
  nombre                    varchar(255),
  apellidos                 varchar(255),
  edad                      integer,
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_my_user primary key (id))
;

create table ruta (
  id                        bigint not null,
  ruta                      varchar(255),
  tag                       varchar(255),
  user_id                   bigint,
  constraint pk_ruta primary key (id))
;

create sequence my_user_seq;

create sequence ruta_seq;

alter table ruta add constraint fk_ruta_user_1 foreign key (user_id) references my_user (id) on delete restrict on update restrict;
create index ix_ruta_user_1 on ruta (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists my_user;

drop table if exists ruta;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists my_user_seq;

drop sequence if exists ruta_seq;

