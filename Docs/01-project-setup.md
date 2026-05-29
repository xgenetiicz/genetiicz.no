# 01  Project Setup

## What I did

Set up the initial project structure with Spring Boot, Maven, Docker Compose and PostgreSQL.

Created `compose.yaml` to run PostgreSQL in Docker so the database starts automatically with the application.
Created `UserEntity.java` & `ProjectEntity.java` so the database is created with the correct records using annotation @Entity and Lombok plugin annotations such as @Getter, @Setter.

Configured `application.yaml` with environment variables from the start - `${POSTGRES_DB}`, `${POSTGRES_USER}`, `${POSTGRES_PASSWORD}` - and a `.env` file to keep sensitive values out of version control.

## application.yaml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  config:
    import: optional:file:.env[.properties]
```

## compose.yaml

```yaml
services:
  postgres:
    image: 'postgres:latest'
    environment:
      - 'POSTGRES_DB=${POSTGRES_DB}'
      - 'POSTGRES_PASSWORD=${POSTGRES_PASSWORD}'
      - 'POSTGRES_USER=${POSTGRES_USER}'
    ports:
      - '5432:5432'
```

## UserEntity & ProjectEnity 
Annotations such as:  
```
@Setter
@Getter
@Entity(name = "nameforthetable_records")
```
for both and variables that represents columns with the correct datatype.

## What I learned

- Add correct dependencies into `pom.xml` so I could use Docker Compose, and also other dependencies.
- Spring Boot can auto-start Docker Compose services via `spring-boot-docker-compose` dependency.
- Environment variables should never be hardcoded - `.env` keeps credentials out of Git.
- `ddl-auto: create-drop` drops and recreates tables on every startup, useful during development.
