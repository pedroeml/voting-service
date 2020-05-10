![Java CI with Gradle](https://github.com/pedroeml/voting-service/workflows/Java%20CI%20with%20Gradle/badge.svg)

# voting-service
This is a Spring Boot server application developed with Spring Boot 2.2.6 and Java OpenJDK 14.0.1.

## Requirements

- Java OpenJDK 14.0.1
- Gradle 6.3
- Docker
- MySQL
- RabbitMQ

## Environment

For local development you'll need to set some environment variables. Change their values according to your MySQL and RabbitMQ setup.

```bash
$ export SPRING_DATASOURCE_URL="jdbc:mysql://localhost/bootdb?createDatabaseIfNotExist=true&autoReconnect=true"
$ export SPRING_DATASOURCE_USERNAME=root
$ export SPRING_DATASOURCE_PASSWORD=root
$ export SPRING_DATASOURCE_PLATFORM=mysql
$ export SPRING_RABBITMQ_HOST=localhost
$ export SPRING_RABBITMQ_PORT=5672
$ export SPRING_RABBITMQ_USERNAME=root
$ export SPRING_RABBITMQ_PASSWORD=root
$ export REST_USER_INFO_URL=https://user-info.herokuapp.com
$ export EXCHANGE_VOTING_RESULT_NAME=VotingResultsExchange
$ export QUEUE_VOTING_RESULT_NAME=VotingResultsQueue
```

### Serve MySQL on Docker for local development

```bash
$ docker container run --name mysqldb -ti -p 3306:3306 -e MYSQL_ROOT_HOST=% -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bootdb -d mysql
```

### Serve RabbitMQ on Docker for local development

```bash
$ docker run --name rabbitmq -ti -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=root -e RABBITMQ_DEFAULT_PASS=root --hostname my-rabbitmq -d rabbitmq:management-alpine
```

## Build

```bash
$ ./gradlew build
```

## Run the app

```bash
$ java -jar build/libs/voting-service-0.0.1-SNAPSHOT.jar
```

## Serve everything on Docker with docker-compose

```bash
# For deploying
$ docker-compose up

# For shutting down
$ docker-compose down
```

Make requests to `http://localhost:8080/` or `http://127.0.0.1:8080/`.

If some JDBC connection failure logs are displayed, the app container will restart by itself and it'll try to reconnect
again. It happens because the MySQL container will take a minute or two until it's ready. So no need to worry because
eventually the app will connect to the MySQL container successfully.
