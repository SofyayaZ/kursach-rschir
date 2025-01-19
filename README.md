# TravelMap

Веб-приложение для путешественников с картами и комментариями.

## Установка

1. Клонируйте репозиторий:

  ``` bash
  git clone https://github.com/SofyayaZ/kursach-rschir.git
```

2. Настройте базу данных:
   
   ``` sql
   create database i_love_tea;
   use i_love_tea;
   create table user (
     id bigint not null auto_increment,
     username varchar(100) not null unique,
     password varchar(50) not null,
     primary key(id)
   );

   create table place (
     id bigint not null auto_increment,
     placename varchar(100) not null,
     lon double not null,
     lat double not null,
     primary key(id)
   );

   create table comment (
     id bigint not null auto_increment,
     user_id bigint not null,
     place_id bigint not null,
     text varchar(300) not null,
     primary key(id),
     foreign key(user_id) references user(id),
     foreign key(place_id) references place(id)
   );
   
3. Откройте проект и настройте файл application.properties
   
   ``` properties
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.datasource.url=jdbc:mysql://localhost:3306/i_love_tea
   spring.datasource.username=your_username
   spring.datasource.password=your_password

## Запуск приложения

Откройте проект в IDE, например, в Intellij IDEA Community Edition 2024.2.4  
Запустите проект (класс KursachApplication.java)

Откройте браузер и перейдите по адресу http://localhost:8080
