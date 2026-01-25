# Spring Boot Customer & Product Management API
This project provides a RESTful API for managing Customers and Products using Spring Boot 3.3.x.

## Technologies Used
- Java 17
- Spring Boot 3.3.x
- Spring MVC
- Hibernate / JPA
- SQL Database: MYSQL
- Swagger for API documentation
- Logback for logging
- Hazelcast for caching products
- Apache Kafka for event notifications
- JUnit 5 for unit tests
- GitHub Actions for CI/CD

## Architecture
Controller --> Service --> Repository --> Database
       |                   |
       |                   --> Kafka (for product creation event)
       |
       --> Filter for API Logging

Controller: Handles API endpoints for Customers and Products, do the request validation.
Service: Contains business logic.
Repository: JPA repositories to handle database operations.
Filter: Logs incoming requests and outgoing responses.
Kafka Producer: Sends notifications to customers when a new product is created.
Hazelcast Cache: Caches products by ID to reduce DB reads, customer data is not cached to ensure latest information.
Github Action: Unit tests are run automatically on every commit.

## How to Run
1. Start the database (MYSQL).
2. Start Kafka:
    cd C:\kafka
    .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties\
    cd C:\kafka
    .\bin\windows\kafka-server-start.bat .\config\server.properties
3. Run Spring Boot application:
    mvn spring-boot:run -Dspring-boot.run.profiles=test
4. Open Swagger UI to test APIs:
    http://localhost:8080/swagger-ui/index.html