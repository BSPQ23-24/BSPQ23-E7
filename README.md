# 🏁BSPQ23-E7: DeustoCars 🚗🛵🏎️
## Index
1. [Introduction](#introduction)
2. [Steps to run it](#run)

 <a id="introduction"></a>
## Introduction

The DeustoCars App is a vehicle renting platform that allows dealership workers to manage vehicle lending, tracking and retrieval, along with a basic dashboard to view statistical measurements for company analysts.

This project is deployed in Java using Maven. The App implements the REST pattern, separating the server and client functionality.

<a id="run"></a>
## Steps to run it
1 - First, go to the `Deustocars` folder and compile the project to create the .class files and enhace the JDOs:
```
mvn clean compile
```
2 - Next, move to the `sql` folder and start the database:
```
cd sql
mysql -u root -p < create-deustoCars.sql
mysql -u root -p < populate.sql
```
(Note: You will be asked to input the mysql root password)

3 - Then, return to the `Deustocars` folder  and create the db schema:
```
cd ..
mvn datanucleus:schema-create
```

4 -  Finally, start the server:
```
mvn jetty:run
```
5 - Optionally, you may run some tests to check the app is working properly and generate code coverage reports using JaCoCo
```
mvn test
mvn jacoco:prepare-agent test jacoco:report
```
You can check this works by entering the URL `localhost:8080`.

6 - Now, in a <u>different command line window</u>, launch the client profile:
```
mvn compile exec:java -Pclient
```
Launching the server profile is not necessary for the application to work. However, you may launch the server manager for some real time testing of the communications:
```
mvn compile exec:java -Pserver
```

<code style="color : Cyan">Thanks for visiting our project. If you have any suggestion, please contact us.😊</code>

7- To generate doxygen reports
```
mvn doxygen:report
```

8- To copy generated html directory into docs folder
```
mvn validate
```

9- To remove generated target files including dir docs with html code
```
mvn clean
```

