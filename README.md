# BSPQ23-E7: DeustoCars

## Introduction
The DeustoCars App is a vehicle renting platform that allows dealership workers to manage vehicle lending, tracking and retrieval, along with a basic dashboard to view statistical measurements for company analysts.

This project is deployed in Java using Maven. The App implements the REST pattern, separating the server and client functionality.

## Steps to run it
1 - First, compile the project to create the .class files:
```
mvn clean compile
```
2 - Next, start the database (TBD)

3 -  Then, start the server:
```
mvn jetty:run
```
You can check this works by entering the URL (Example: `localhost:8080`)

4 - Now, launch the client and server profiles:
```
mvn compile exec:java -Pclient
```
```
mvn compile exec:java -Pserver

```


