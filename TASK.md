## Task: Building a RESTful Service with Spring Boot
### Objective:
Migrate a manual Spring configuration project to Spring Boot. The goal is to leverage auto-configuration, simplify dependency management, and master externalized application configuration.

### Requirements:

#### Project Setup: Create a new Spring Boot application using SpringApplication and the @SpringBootApplication annotation.

RESTful Endpoints: Implement a CatalogController with the following functionality:

GET /hello: Returns a simple text response.

GET /books/sample: Returns a JSON object (create a BookStatus POJO to represent the data).

GET /library-name: Returns a string value retrieved from the application properties.

### External Configuration:

Use application.properties to manage settings.

Inject the library.name property into your controller using the @Value annotation.

### Custom Server Settings:

Override the default server port to 8081 using the server.port property.

Verify the application starts and responds on the new port.

Clean Code: Ensure proper use of constructor-based dependency injection.

### Verification:

Confirm that JSON serialization works automatically (via Spring Boot's internal Jackson support).

Confirm the application no longer requires manual @PropertySource or complex context refresh logic.

### Deliverables:

A working Spring Boot application.

An application.properties file with the required configuration.

A REST controller providing the requested endpoints.
