/* DELETE 'deustoCarsDB' database if exists*/
DROP SCHEMA IF EXISTS deustocarsdb;
/* DELETE 'admin' user if exists on the local server*/
DROP USER IF EXISTS 'spq'@'localhost';

/* CREATE 'deustoCarsDB' database*/
CREATE SCHEMA deustocarsdb;
/* CREATE 'spq' user on the local server*/
CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';
/* GIVE 'spq' all permissions*/
GRANT ALL ON deustocarsdb.* TO 'spq'@'localhost';
USE deustocarsdb;