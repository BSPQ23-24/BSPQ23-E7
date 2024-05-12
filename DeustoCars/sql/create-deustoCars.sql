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
CREATE TABLE customerjdo (
    email VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    dateOfBirth DATE NOT NULL
);
/* Create 'vehicles' */
CREATE TABLE vehiclejdo (
	numberPlate VARCHAR(255) PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    readyToBorrow BOOLEAN NOT NULL,
    onRepair BOOLEAN NOT NULL
);

CREATE TABLE renting (
    id INT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    licensePlate VARCHAR(255) NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    FOREIGN KEY (email) REFERENCES customerjdo(email),
    FOREIGN KEY (licensePlate) REFERENCES vehiclejdo(numberPlate)
);

