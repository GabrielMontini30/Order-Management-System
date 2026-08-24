# Order Manager System

API REST para um Order Management System desenvolvido com Java e Spring Boot.
## About

O Order Manager System é uma API REST projetada para gerenciar clientes, produtos e pedidos.

O aplicativo permite:
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

O projeto segue uma arquitetura em camadas:
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

O aplicativo também utiliza:

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

A aplicação implementa regras de negócio como:

* O e-mail do usuário não pode ser duplicado
* O cliente deve existir antes da criação de um pedido
* Os produtos devem existir antes de serem adicionados a um pedido
* Pedidos não podem ser criados sem itens
* A quantidade do item deve ser válida
* O estoque do produto é validado antes da criação de um pedido
* O estoque do produto é reduzido após a criação de um pedido
* As alterações de status do pedido seguem as regras definidas pelo domínio
## Security

A autenticação é implementada utilizando Spring Security e JWT.

O aplicativo utiliza:
* Password encryption
* JWT authentication
* Authentication filters
* User roles
* Protected endpoints

O usuário autenticado pode ser utilizado pela aplicação para identificar o usuário responsável por uma operação.
## Tests

O projeto contém:

* Service Tests
* Controller Tests
* JWT Tests
* Security Tests

Construído com:

* JUnit 5
* Mockito
* AssertJ
* MockMvc

O teste abrange:

* Business rules
* Successful operations
* Invalid requests
* Resource not found scenarios
* Stock validation
* Authentication
* JWT generation and validation
* HTTP responses

## Running

Clone o projeto:

```bash
git clone https://github.com/GabrielMontini30/Order-Management-System
```

Entre no projeto:

```bash
cd Order-Management-System
```

Execute a aplicação:

```bash
mvn spring-boot:run
```

## Database

MySQL

Configure o banco de dados em:

```text
application.yml
```

Exemplo:

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

A documentação da API fornece os endpoints disponíveis, os request bodies, os responses e HTTP status code.
## Author

Gabriel Montini

LinkedIn:

https://www.linkedin.com/in/gabriel-montini

GitHub:

https://github.com/GabrielMontini30
