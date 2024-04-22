# BSPQ23-E7: DeustoCars
## Index
1. [Introduction](#introduction)
2. [Steps to run it](#run)

 <a id="introduction"></a>
## Introduction

The DeustoCars App is a vehicle renting platform that allows dealership workers to manage vehicle lending, tracking and retrieval, along with a basic dashboard to view statistical measurements for company analysts.

This project is deployed in Java using Maven. The App implements the REST pattern, separating the server and client functionality.

<a id="run"></a>
## Steps to run it
1 - First, compile the project to create the .class files:
```
mvn clean compile
```
2 - Next, start the database (TBD)
```
mvn datanucleus:schema-create
```

3 -  Then, start the server:
```
mvn jetty:run
```
You can check this works by entering the URL (Example: `localhost:8080`)

4 - Now, in a different command line window, launch the client and server profiles:
```
mvn compile exec:java -Pclient
```
Launching the server profile is not necessary for the application to work. You may launch the server manager for some real time testing of the communications.
```
mvn compile exec:java -Pserver

```

5 - Finally, you can run some tests to check the app is working properly

