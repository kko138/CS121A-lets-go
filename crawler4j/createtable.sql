CREATE DATABASE IF NOT EXISTS crawlData;

USE crawlData;

CREATE TABLE IF NOT EXISTS data(
url varchar(1000) NOT NULL,
html text NOT NULL,
textfile text not null
);
