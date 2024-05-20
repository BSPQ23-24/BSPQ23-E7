FROM maven:3.8.1-openjdk-11

RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://deb.nodesource.com/setup_14.x | bash - && \
    apt-get install -y nodejs

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean compile

RUN service mysql start && \
    mysql -u root -p"$MYSQL_ROOT_PASSWORD" < sql/create-deustoCars.sql && \
    mvn datanucleus:schema-create && \
    mysql -u root -p"$MYSQL_ROOT_PASSWORD" < sql/populate.sql

CMD ["mvn", "jetty:run"]
