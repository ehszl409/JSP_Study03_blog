# JSP_Study03_blog
----------------------------------

- 12월 31일 수업

``` MySQL
create user 'bloguser'@'%' identified by 'bitc5600';
grant all privileges on *.* to 'bloguser'@'%';
create database blog;
use blog;

CREATE TABLE user(
    id int primary key auto_increment,
    username varchar(100) not null unique,
    password varchar(100) not null,
    email varchar(100) not null,
    address varchar(100),
    userRole varchar(20),
    createDate timestamp
)engine=InnoDB default charset=utf8;


CREATE TABLE board(
    id int primary key auto_increment,
    userId int,
    title varchar(100) not null,
    content longtext,
    readCount int default 0,
    createDate timestamp,
    foreign key (userId) references user (id)
)engine=InnoDB default charset=utf8;

CREATE TABLE reply(
    id int primary key auto_increment,
    userId int,
    boardId int,
    content varchar(300) not null,
    createDate timestamp,
    foreign key (userId) references user (id) on delete set null,
    foreign key (boardId) references board (id) on delete cascade
)engine=InnoDB default charset=utf8;

```
