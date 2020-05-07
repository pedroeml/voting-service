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

## Serve on Docker

```bash
# For creating the network
$ docker network create voting-mysql

# For creating the MySQL DB
$ docker container run --name mysqldb -ti -p 3306:3306 -e MYSQL_ROOT_HOST=% -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bootdb -d mysql

# For creating the RabbitMQ
$ docker run --name rabbitmq -ti -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=root -e RABBITMQ_DEFAULT_PASS=root --hostname my-rabbitmq -d rabbitmq:management-alpine

# For building the image:
$ docker image build -t voting-service .

# For running the app in the container
$ docker container run -ti -p 8080:8080 -d voting-service
```

Make requests to `http://localhost:8080/` or `http://127.0.0.1:8080/`.
