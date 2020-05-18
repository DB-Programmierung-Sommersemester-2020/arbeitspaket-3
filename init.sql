DROP DATABASE IF EXISTS usernotes;

CREATE DATABASE usernotes CHARACTER SET utf8mb4;

USE usernotes;

CREATE TABLE Users (id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50),
                    password VARCHAR(50),
                    email VARCHAR(50));

CREATE TABLE Notes (id INT PRIMARY KEY AUTO_INCREMENT,
                    subject VARCHAR(100),
                    content VARCHAR(1024),
                    date VARCHAR(100),
                    userId INT);

CREATE TABLE Passwords (id INT PRIMARY KEY,
                    pwdhash BLOB,
                    salt BLOB
                    );
