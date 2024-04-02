/* DELETE 'deustoCarsDB' database if exists*/
DROP SCHEMA IF EXISTS deustoCarsDB;
/* DELETE 'admin' user if exists on the local server*/
DROP USER IF EXISTS 'admin'@'localhost';

/* CREATE 'deustoCarsDB' database*/
CREATE SCHEMA productsDB;
/* CREATE 'admin' user on the local server*/
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
/* GIVE 'admin' all permissions*/
GRANT ALL ON deustoCarsDB.* TO 'admin'@'localhost';
