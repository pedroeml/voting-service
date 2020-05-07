![Java CI with Gradle](https://github.com/pedroeml/voting-service/workflows/Java%20CI%20with%20Gradle/badge.svg)

# voting-service
This is a simple Spring Boot server application developed with Spring Boot 2.2.6 and Java OpenJDK 14.0.1.

## Build

```bash
$ ./gradlew build
```

## Run the app

```bash
$ java -jar build/libs/voting-service-0.0.1-SNAPSHOT.jar
```

## Serve on Docker with docker-compose

```bash
# For deploying
$ docker-compose up

# For shutting down
$ docker-compose down
```

Make requests to `http://localhost:8080/` or `http://127.0.0.1:8080/`.

## Serve on Docker for local development

First you will need to edit the `application.properties` file to use localhost address instead. You just need to switch
some commented lines to look like the following:

```properties
# Local MySQL DB
spring.datasource.url=jdbc:mysql://localhost/bootdb?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false
#spring.datasource.url=jdbc:mysql://mysqldb/bootdb
...
# Local RabbitMQ
spring.rabbitmq.host=localhost
#spring.rabbitmq.host=myrabbitmq
```

Next create containers for MySQL DB and RabbitMQ, then you should be able to launch the application normally.

```bash
# For creating the MySQL DB
$ docker container run --name mysqldb -ti -p 3306:3306 -e MYSQL_ROOT_HOST=% -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bootdb -d mysql

# For creating the RabbitMQ
$ docker run --name rabbitmq -ti -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=root -e RABBITMQ_DEFAULT_PASS=root --hostname my-rabbitmq -d rabbitmq:management-alpine
```

Now you can check if everything is ok by HTTP GET `http://localhost:8080/actuator/health` or `http://127.0.0.1:8080/actuator/health`.
