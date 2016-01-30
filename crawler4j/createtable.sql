CREATE DATABASE IF NOT EXISTS crawldb;

USE crawldb;

CREATE TABLE IF NOT EXISTS data(
url varchar(1000) NOT NULL,
html longtext NOT NULL,
textfile mediumtext not null
);
