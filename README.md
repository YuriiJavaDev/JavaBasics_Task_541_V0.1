## Library Catalog REST API (JavaBasics_Task_541_V0.1)
### 📖 Description
This project introduces Spring Boot and its auto-configuration capabilities. It demonstrates how to build a RESTful service, handle JSON responses, and manage application settings externally via application.properties without manual configuration files.

### 📋 Requirements Compliance
Spring Boot Integration: Project bootstrapped and running via SpringApplication.

REST Controller: Implemented CatalogController with multiple endpoints (/hello, /books/sample, /library-name).

Data Serialization: Automated JSON mapping using the BookStatus POJO.

External Configuration: Successfully injected library.name using @Value.

Custom Environment: Configured server port (8081) via application.properties.

### 🚀 Architectural Stack
Framework: Spring Boot 3.x

Language: Java 23

Build Tool: Maven

### 🏗️ Implementation Details
CatalogController: Handles incoming HTTP GET requests and manages dependencies.

BookStatus: A simple data model automatically serialized to JSON by Spring Boot's Jackson library.

application.properties: Centralized configuration file for environment settings (port, library metadata).

### 🎯 Expected result
Application starts on http://localhost:8081.

/hello returns "Library Catalog API!".

/books/sample returns {"title": "Harry Potter", "available": false}.

/library-name returns the value defined in properties file ("City Library").
### Project Structure:

    JavaBasics_Task_541/
    ├─ src/main/
    │      ├────────────── java/com/yurii/pavlenko/
    │      └─ resources/                  ├── Application.java
    │         └─ application.properties   ├── CatalogController.java
    │                                     └── BookStatus.java
    ├── pom.xml
    ├── LICENSE
    ├── TASK.md
    ├── THEORY.md
    └── README.md

## 💻 Code Example

```java
package com.yurii.pavlenko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
```java
package com.yurii.pavlenko;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    private final String libraryName;

    public CatalogController(
            @Value("${library.name}") String libraryName){
        this.libraryName = libraryName;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Library Catalog API!";
    }

    @GetMapping("/books/sample")
    public BookStatus status() {
        return new BookStatus("Harry Potter", false);
    }

    @GetMapping("/library-name")
    public String libraryName() {
        return libraryName;
    }
}

```

### ⚖️ License
This project is licensed under the **MIT License**.

Copyright (c) 2026 Yurii Pavlenko

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files...

License: [MIT](LICENSE)
