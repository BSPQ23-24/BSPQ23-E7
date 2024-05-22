# 🏁BSPQ23-E7: DeustoCars 🚗🛵🏎️

You can read the [DeustoCars Documentation](https://bspq23-24.github.io/BSPQ23-E7/doxygen) for a better definition of the functions.

You can see the [Test Report](https://bspq23-24.github.io/BSPQ23-E7/jacoco) to see the testing coverage of DeustoCars.

## Index
1. [Introduction](#introduction)
2. [Steps to run it](#run)
3. [Using Docker](#docker)

<a id="introduction"></a>
## Introduction

The DeustoCars App is a vehicle renting platform that allows dealership workers to manage vehicle lending, tracking, and retrieval, along with a basic dashboard to view statistical measurements for company analysts.

This project is deployed in Java using Maven. The App implements the REST pattern, separating the server and client functionality.

<a id="run"></a>
## Steps to run it
1. First, go to the `Deustocars` folder and compile the project to create the .class files and enhance the JDOs:
    ```sh
    mvn clean compile
    ```

2. Next, move to the `sql` folder and start the database:
    ```sh
    cd sql
    mysql -u root -p < create-deustoCars.sql
    ```
    (Note: You will be asked to input the MySQL root user's password)

3. Then, return to the `Deustocars` folder and create the DB schema:
    ```sh
    cd ..
    mvn datanucleus:schema-create
    ```

4. Now, populate the database:
    ```sh
    mysql -u root -p < populate.sql
    ```
    (Note: You will be asked to input the MySQL root user's password again)

5. Finally, start the server:
    ```sh
    mvn jetty:run
    ```

6. Optionally, you may run some tests to check the app is working properly and generate code coverage reports using JaCoCo:
    ```sh
    mvn test
    mvn jacoco:prepare-agent test jacoco:report
    ```
    You can check this works by entering the URL `localhost:8080`.

7. Now, in a _different command line window_, launch the client profile:
    ```sh
    mvn compile exec:java -Pclient
    ```
    Launching the server profile is not necessary for the application to work. However, you may launch the server manager for some real-time testing of the communications:
    ```sh
    mvn compile exec:java -Pserver
    ```

8. To generate Doxygen reports:
    ```sh
    mvn doxygen:report
    ```

9. To copy the generated HTML directory into the `docs` folder:
    ```sh
    mvn validate
    ```

10. To remove generated target files including the `docs` directory with HTML code:
    ```sh
    mvn clean
    ```

<a id="docker"></a>
## Using Docker

To run the DeustoCars app using Docker, follow these steps:

1. Ensure you have Docker and Docker Compose installed on your machine.

2. The `.env` file with the necessary environment variables is already created in the project.

3. Build and start the containers:
    ```sh
    docker-compose up --build
    ```

4. This command will build the Maven project, set up the MySQL database, and start the server. You can check the application by navigating to `http://localhost:9080` in your browser.

5. To stop the containers, use:
    ```sh
    docker-compose down
    ```

6. If you need to rebuild the containers after making changes to the code, use:
    ```sh
    docker-compose up --build
    ```

<code style="color : Cyan">Thanks for visiting our project. If you have any suggestions, please contact us.😊</code>
