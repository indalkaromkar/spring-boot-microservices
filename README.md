# Spring Boot Microservices

# ATTENTION: This repository is archived, you can find the source code in the new repository that includes much more concepts and upto date - https://github.com/indalkaromkar/spring-boot-3-microservices-course

This repository contains the latest source code of the spring-boot-microservices tutorial

## Architecture Overview

This microservices application consists of three main services:
- **Product Service** (Port 8080): Manages product catalog
- **Order Service** (Port 8081): Handles order processing
- **Inventory Service** (Port 8082): Manages stock levels

## End User Documentation

### What is this application?
A microservices-based e-commerce system that allows you to:
- Browse and manage products
- Place orders
- Check inventory availability

### How to use the application
1. **View Products**: Access http://localhost:8080/swagger-ui.html to browse available products
2. **Place Orders**: Use http://localhost:8081/swagger-ui.html to create and manage orders
3. **Check Inventory**: Visit http://localhost:8082/swagger-ui.html to view stock levels

### API Endpoints
- **GET /api/products** - List all products
- **POST /api/products** - Create new product
- **POST /api/orders** - Place new order
- **GET /api/inventory** - Check stock availability

## Developer Documentation

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker (optional)

### Project Structure
```
spring-boot-microservices/
├── product-service/     # Product management service
├── order-service/       # Order processing service
├── inventory-service/   # Inventory management service
└── docker-compose.yml   # Docker orchestration
```

### Technology Stack
- Spring Boot 3.x
- Spring Data JPA
- H2/MySQL Database
- Docker
- OpenAPI 3.0

### Development Setup
1. Clone the repository
2. Import as Maven project in your IDE
3. Run each service individually or use Docker Compose

### Testing
- Unit tests: `mvn test`
- Integration tests: `mvn verify`
- Skip tests: `mvn clean package -DskipTests`

## How to run the application using Docker

1. Run `mvn clean package -DskipTests` to build the applications and create the docker image locally.
2. Run `docker-compose up -d` to start the applications.

## How to run the application without Docker

1. Run `mvn clean verify -DskipTests` by going inside each folder to build the applications.
2. After that run `mvn spring-boot:run` by going inside each folder to start the applications.

## OpenAPI Documentation

After starting the services, you can access the OpenAPI documentation at:

- **Product Service**: http://localhost:8080/swagger-ui.html
- **Order Service**: http://localhost:8081/swagger-ui.html  
- **Inventory Service**: http://localhost:8082/swagger-ui.html

API specifications are also available in JSON format at:
- **Product Service**: http://localhost:8080/v3/api-docs
- **Order Service**: http://localhost:8081/v3/api-docs
- **Inventory Service**: http://localhost:8082/v3/api-docs

