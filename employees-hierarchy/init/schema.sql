create database if not exists personia;

create user 'personia'@'%' identified by 'personia';
grant create, alter, index, lock tables, references, update, delete, drop, select, insert on `personia`.* to 'personia'@'%';
flush privileges;

use personia;

create table users (
  name varchar(200) not null,
  encrypted_password varchar(200) not null,
  primary key (name)
);

create table employee (
  id int not null auto_increment,
  name varchar(200) not null,
  supervisor_id int,
  primary key (id)
);
 
 
