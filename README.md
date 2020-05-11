![Java CI with Gradle](https://github.com/pedroeml/voting-service/workflows/Java%20CI%20with%20Gradle/badge.svg)

[![API Doc in Postman](https://run.pstmn.io/button.svg)](https://documenter.getpostman.com/view/6554691/SzmfZJ1n?version=latest)

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

## Architecture

It's feature-first based, which means the most external packages are related to the application features. They
act like boxes to avoid mixing code accross business or domain-specific topics. These packages may provide a "provider 
class" (like a Facade) to use inside other packages via dependency injection. Each package may be divided into more specific ones like:

- `contract`: provides REST controller classes. This package may contain version packages inside it.
- `dao`: provides access methods to persistent data. This package contains the respective type-object class also.
- `dto`: contains type-object classes for returning as response in the controller classes.
- `exception`: contains domain-specific runtime exception classes.
- `integration`: contains type-object classes for receiving as request in the controller classes or as response from
external services.
- `mapper`: provides mapper class for mapping type-object classes among packages.
- `model`: contains type-object classes for most general use accross packages.
- `service`: provides business or domain-specific classes.

## Improvements

There is plenty of space to use concurreny (multithreading) to perform some independent searching, mapping and filtering, 
but it'd make the code a little harder to read and to debug which wouldn't be a good trade for such small web service
application. Something that would also be nice to have is to having more complex SQL queries. That would decrease the 
application processing and memory usage, but it'd add more complexity to the application and it wouldn't take advantage
of some code reusability.

## Limitations

Currently only closing sessions on the next minute the Scheduled class is running are monitored to send result messages 
to the queue. It may be a way to reduce memory usage by querying only the closing sessions on the next minute, but the 
previous closed sessions won't be captured. To prevent that it'd be required to create a results table to store for
every session result after its closing time. That could possibly explode memory consumption. The only way to prevent
that would be paginating the select queries.

### TODO

- Logs (slf4j)
- Tests (JUnit & Mockito)
- API Documentation (Swagger)
    - A draft of it you can check on the published Postman collection
