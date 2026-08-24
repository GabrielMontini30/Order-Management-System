# Order Manager System

REST API for an Order Management System built with Java and Spring Boot.

## About

Order Manager System is a REST API designed to manage customers, products and orders.

The application allows:

* User authentication
* Customer management
* Product management
* Order management
* Order status updates
* Stock control
* Customer-based order listing
* Role-based authorization
* JWT authentication
* Validation and exception handling

## Technologies

* Java 21
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* MySQL
* Hibernate
* MapStruct
* Lombok
* Bean Validation
* Swagger/OpenAPI
* JUnit 5
* Mockito
* MockMvc
* Maven

## Architecture

The project follows a layered architecture:

```text
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
Database
```

The application also uses:

* DTOs
* MapStruct
* Global Exception Handler
* JWT Authentication
* Role-based authorization
* Bean Validation

## Features

### Authentication

* User registration
* Login
* JWT generation
* JWT validation
* Role-based access control

### Customers

* Create customer
* Find customer by ID
* List customers
* Delete customer

### Products

* Create product
* Update product
* Find product by ID
* List products
* Delete product
* Stock management

### Orders

* Create order
* Find order by ID
* List all orders
* List orders by customer
* Update order status
* Validate product stock
* Automatically update product stock after order creation

## Business Rules

The application implements business rules such as:

* User email cannot be duplicated
* Customer must exist before creating an order
* Products must exist before being added to an order
* Orders cannot be created without items
* Item quantity must be valid
* Product stock is validated before creating an order
* Product stock is reduced after an order is created
* Order status changes follow the rules defined by the domain

## Security

Authentication is implemented using Spring Security and JWT.

The application uses:

* Password encryption
* JWT authentication
* Authentication filters
* User roles
* Protected endpoints

The authenticated user can be used by the application to identify the user responsible for an operation.

## Tests

The project contains:

* Service Tests
* Controller Tests
* JWT Tests
* Security Tests

Built using:

* JUnit 5
* Mockito
* AssertJ
* MockMvc

The tests cover:

* Business rules
* Successful operations
* Invalid requests
* Resource not found scenarios
* Stock validation
* Authentication
* JWT generation and validation
* HTTP responses

## Running

Clone the project:

```bash
git clone https://github.com/GabrielMontini30/Order-Management-System
```

Enter the project:

```bash
cd Order-Management-System
```

Run the application:

```bash
mvn spring-boot:run
```

## Database

MySQL

Configure the database in:

```text
application.yml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/order_manager
    username: root
    password: root
```

## API Documentation

Swagger/OpenAPI:

```text
http://localhost:8080/swagger-ui/index.html
```

The API documentation provides the available endpoints, request bodies, responses and HTTP status codes.

## Author

Gabriel Montini

LinkedIn:

https://www.linkedin.com/in/gabriel-montini

GitHub:

https://github.com/GabrielMontini30
