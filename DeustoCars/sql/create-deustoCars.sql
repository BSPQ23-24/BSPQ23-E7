/* DELETE 'deustoCarsDB' database if exists*/
DROP SCHEMA IF EXISTS deustoCarsDB;
/* DELETE 'admin' user if exists on the local server*/
DROP USER IF EXISTS 'spq'@'localhost';

/* CREATE 'deustoCarsDB' database*/
CREATE SCHEMA deustoCarsDB;
/* CREATE 'spq' user on the local server*/
CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';
/* GIVE 'spq' all permissions*/
GRANT ALL ON deustoCarsDB.* TO 'spq'@'localhost';
USE deustoCarsDB;
/* Create 'customers' */
CREATE TABLE customers (
    email VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL
);
/* Create 'vehicles' */
CREATE TABLE vehicles (
	number_plate VARCHAR(255) PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    ready_to_borrow BOOLEAN NOT NULL,
    on_repair BOOLEAN NOT NULL
);
