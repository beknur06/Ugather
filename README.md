### README

# Ugather Application

## Overview
Ugather is a Spring Boot application designed to manage events. It provides functionalities to create, update, delete, and search for events. The application also includes user authentication and authorization using JWT tokens.

## Technologies Used
- Java
- Spring Boot
- Spring Security
- Maven
- JWT (JSON Web Tokens)
- Swagger for API documentation

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/your-repo/ugather.git
    cd ugather
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## API Endpoints

### EventController
- `GET /events/{id}`: Get event by ID
- `POST /events/filter`: Filter events
- `GET /events/search`: Search events
- `GET /events/trending`: Get trending events
- `GET /events/upcoming`: Get upcoming events
- `GET /events/today`: Get today's events
- `POST /events`: Create a new event
- `PUT /events/{id}`: Update an event
- `DELETE /events/{id}`: Delete an event
- `GET /events/user/{userId}`: Get events by user ID

### Security
- `POST /users/signin`: User sign-in
- `POST /users/signup`: User sign-up

## Security Configuration
The application uses JWT for securing endpoints. The security configuration is defined in `WebSecurityConfig.java`.

## Swagger Documentation
Swagger is used for API documentation. You can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## License
This project is licensed under the MIT License.