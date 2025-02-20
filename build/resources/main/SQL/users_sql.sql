create table users(
                      user_id bigint primary key auto_increment,
                      email varchar(50) not null,
                      password varchar(255) not null,
                      username varchar(20) not null,
                      address varchar(255) not null,
                      phone varchar(20) not null,
                      gender varchar(5) not null,
                      role varchar(20) not null,
                      status varchar(20) not null,
                      created_at datetime not null,
                      deleted_at datetime
);