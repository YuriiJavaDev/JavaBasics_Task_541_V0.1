## Spring Boot project launch and first REST controller.

## Lesson Objectives

1. Understand that Spring Boot doesn't replace everything you already know about Spring (beans, DI, @Component, @Profile, @Value, and so on), but adds autoconfiguration, a built-in server, and a convenient project starter.
2. Create a new project using Spring Initializr and understand what's inside.
3. Understand what starter dependencies are and why you no longer need to manually specify versions in pom.xml.
4. Understand the idea of auto-configuration using a concrete example—without feeling like "magic."
5. Write your first REST controller, start the server, and get a response through the browser.

**Relationship to the previous lesson:** Until now, you've been starting Spring manually—creating an AnnotationConfigApplicationContext , registering the @Configuration class, and deciding when to call refresh() . The application started, did something in the console, and exited. Today, for the first time, we're seeing a **server that doesn't shut down**—it starts and waits for HTTP requests until you stop it yourself.

---

## Problem Statement: Why Spring Boot and Where Does Tomcat Come From?

Let's put together two things that have been separated into different lessons so far—and clarify which of them you've seen in practice and which were just theory.

**1. When we were covering Java EE and Servlets**, Tomcat was mentioned only as a **name and analogy**—"a servlet container, like Tomcat," which, in theory, should listen on a port, manage the servlet lifecycle (init/service/destroy), and call doGet()/doPost() on every request. But you've never installed and run a real Tomcat—the servlet tasks explicitly asked you to design it "on paper, without actually deploying it to Tomcat." The actual, working code you wrote and ran was JDK HttpServer—no Tomcat, no container, just your bare hands. So your practical experience is with HttpServer, while Tomcat has been just a name and a comparison tool up until now.

2. Everything we did next—Spring Core, DI, beans, @Profile, @Value—happened without any server and without HTTP at all. A regular main() that printed something to the console and exited. Spring only handled one thing there: creating objects and passing them dependencies. It had nothing to do with the network, ports, or incoming requests.

So, you're stuck: you can write an HTTP server by hand (HttpServer), but without DI and a convenient project structure. And you can use Spring DI, but without HTTP at all. If you were to put it all together manually, as it was historically done (before Spring Boot), you'd have to install a real Tomcat, manually configure a special servlet called DispatcherServlet in it (this is a regular Spring servlet that accepts HTTP requests just like HelloServlet in the Java EE lesson, but passes them on to the appropriate Spring bean instead of doGet()), manually set up a Spring context within this servlet, compile everything into a .war, and deploy it to the installed Tomcat. That's a lot of manual stitching—and that's exactly what Spring Boot does for you.

**What Spring Boot ultimately gives you:**
- Instead of you installing and configuring Tomcat yourself, Spring Boot includes it **as a regular project library** (you already saw this in Part 2.3 — `spring-boot-starter-web` includes Tomcat as a dependency). This is called "embedded Tomcat": the same Tomcat, with the same task (listening on a port, accepting connections, passing the request on), but now it starts automatically, inside your `.jar`, when you call `SpringApplication.run(...)` in your `main()` — that is, this is the first time you'll actually see Tomcat in action, not just hear its name.
- Spring creates and configures the `DispatcherServlet` automatically via auto-configuration (Part 4) — no `web.xml` or manual registration is required, either now or later.
  Instead of `doGet()`/`doPost()`, you write a regular Spring bean (`@RestController`) with a method marked `@GetMapping`—a similar principle of "the platform calls your method automatically" that was previously encountered in servlets, only here it actually works, and through Spring, not directly through the Tomcat API.

To summarize: Spring Boot isn't "a new framework to replace the old one," but a layer that connects what you know about Spring DI with a server that accepts HTTP requests—without requiring you to separately learn how to install and manually configure Tomcat. Later in this lesson, we'll walk through exactly how this connection occurs.

--

# Part 1. What's Remaining and What's Added

Everything you've learned before works **unchanged** in Spring Boot:

- `@Component`, `@Service`, `@Repository` — stereotypes and component scanning;
- constructor-based dependency injection;
- `@Qualifier`, `@Primary` — choosing between multiple implementations;
- `@Profile` — different beans for different environments;
- `@Value` and `Environment` — configuration reading.

Spring Boot adds four new features, and that's what we'll cover today:

| What's Added | What Does It Give |
| --- | --- |
| Initializr + ready-made project structure | no need to manually build `pom.xml`, folder structure, dependencies |
| Starter dependencies | one `spring-boot-starter-web` instead of finding a dozen compatible library versions |
| Auto-configuration | Many beans (for example, a web server) are created automatically if they see the necessary libraries on the classpath |
| Embedded Tomcat | The application itself is a server, no separate Tomcat installation required |

---

# Part 2. Creating a Project with Spring Initializr

## 2.1 Creation Steps

Spring Initializr is a web service that generates a ready-made Boot project skeleton based on the selected parameters. Address: `https://start.spring.io`.

1. Open `https://start.spring.io` in your browser.
2. Fill in the fields:
- **Project:** `Maven` (the same build tool as in previous projects—we'll continue to work with it, not Gradle).
- **Language:** `Java`.
- Spring Boot: Select the newest version without the SNAPSHOT/M1/M2/RC tags (these are unstable pre-release versions—you need the regular stable version).
- Group: org.example (as in previous projects).
- Artifact: library-rest-demo.
- Leave Name the same as Artifact.
- Packaging: Jar.
- Java: 21.
3. In the Dependencies section, click Add Dependencies and find Spring Web. Add it. Don't add anything else for now.
4. Click Generate—a .zip archive will be downloaded.
5. Unzip the archive to a convenient folder (for example, next to other course projects).
6. In IntelliJ: `File` → `Open...` → select the unzipped folder (the one with `pom.xml`) → open as a Maven project. Give IntelliJ time to download the dependencies (there will be a progress bar at the bottom).

After generation, the final package in the code will be named `org.example.libraryrestdemo` (Initializr removes the hyphens from the artifact name) – this is normal, not an error.

## 2.2 What's inside: pom.xml

The opened `pom.xml` will look something like this (this is not an exact copy—your versions may differ, but the structure is the same):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
<modelVersion>4.0.0</modelVersion>

<parent>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-parent</artifactId>
<version>3.4.0</version>
<relativePath/>
</parent>

<groupId>org.example</groupId>
<artifactId>library-rest-demo</artifactId> 
<version>0.0.1-SNAPSHOT</version> 
<name>library-rest-demo</name> 

<properties> 
<java.version>21</java.version> 
</properties> 

<dependencies> 
<dependency> 
<groupId>org.springframework.boot</groupId> 
<artifactId>spring-boot-starter-web</artifactId> 
</dependency> 
<dependency> 
<groupId>org.springframework.boot</groupId> 
<artifactId>spring-boot-starter-test</artifactId> 
<scope>test</scope> 
</dependency> 
</dependencies> 

<build> 
<plugins> 
<plugin> 
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
</plugins>
</build>
</project>
```

Compare with the `pom.xml` from the previous project, where the dependency looked like this:

```xml
<dependency>
<groupId>org.springframework</groupId>
<artifactId>spring-context</artifactId>
<version>6.1.0</version>
</dependency>
```

There, you wrote `<version>6.1.0</version>` yourself and were responsible for ensuring that this version was compatible with everything else. In the new `pom.xml`, `spring-boot-starter-web` has no version at all – it's defined by `<parent>spring-boot-starter-parent</parent>`. This is called **dependency management**: the parent POM maintains a table of "which versions of what are compatible with each other," and you only need to specify the Spring Boot version once in the `<parent>`, and the versions of all other Spring dependencies are selected automatically.

## 2.3 What is a starter

`spring-boot-starter-web` is not a single library, but a **package of dependencies** assembled for a single task ("web application"). If you open the ʻExternal Libraries` tab in IntelliJ after importing, you'll see that this single line in `pom.xml` has pulled in several libraries at once: Spring MVC, embedded Tomcat, and Jackson (a library for JSON). Without a starter, you would have to find and select compatible versions of each of them yourself—the same thing you saw in Part 2.2.

---

# Part 3. Anatomy of a Boot Application

## 3.1 Main Class

In the `org.example.libraryrestdemo` package, Initializr created the `LibraryRestDemoApplication.java` file:

```java
package org.example.libraryrestdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryRestDemoApplication {

public static void main(String[] args) {
SpringApplication.run(LibraryRestDemoApplication.class, args);
}
}
```

Breaking:

- `SpringApplication.run(LibraryRestDemoApplication.class, args)` is a static method that replaces everything you previously wrote manually (`new AnnotationConfigApplicationContext()` → `register()` → `refresh()`). It creates a container, starts bean assembly, and, if a web starter is on the classpath (as is currently the case), starts the embedded server.
- **The main difference from all previous lessons:** Previously, `main()` executed a few lines and then returned—the process was finished. Now `SpringApplication.run(...)` starts the server and doesn't give up control—the process continues running and listening on the port until you stop it manually (in IntelliJ, the red ⏹ `Stop` button next to the start button).
- `@SpringBootApplication` is a single annotation that actually combines three:
- `@SpringBootConfiguration` is practically the same as the familiar `@Configuration` (the class can contain `@Bean` methods);
- `@ComponentScan` scans beans, **without** explicitly specifying `basePackages`, as you did before. The scan starts from the package of this class and goes into all nested packages. Therefore, the main application class should be in the "root" package, above or at the same level as all your other classes;
- `@EnableAutoConfiguration` — enables auto-configuration (part 4).

## 3.2 application.properties

There's already an empty `application.properties` file in `src/main/resources`. Previously, for Spring to see such a file, it was necessary to explicitly include it via `@PropertySource` on the `@Configuration` class. In Boot, auto-configuration does this automatically: the `application.properties` file in `src/main/resources` is included automatically, without a single annotation on your part.

**An important disclaimer about encoding:** the Cyrillic issue from the previous lesson in Boot **is not automatically resolved** — `.properties` files are read in `ISO-8859-1` by default (this is a behavior of the `java.util.Properties` class from the Java standard library, not a feature of any specific file inclusion method), and this applies equally to both Spring Core and Spring Boot. What changes in Boot isn't the encoding, but rather the fact that you don't need to write `@PropertySource` manually. If `application.properties` contains Cyrillic characters, you'll see the same gibberish as before.

## 3.3 How to actually fix Cyrillic encoding

Since the problem is caused by the behavior of `java.util.Properties` (it reads the file bytes as `ISO-8859-1`) and not something specific to Spring, the following helps:

**— use `application.yaml` instead of `.properties`.** YAML is read not through `java.util.Properties`, but through the YAML parser, which by default works in `UTF-8`, without this problem at all. We'll cover this format in detail in a future lesson. If encoding is a problem right now, you can switch to it sooner: rename the file to `application.yaml` and rewrite the contents in YAML syntax (`library:` on one line, `name: City Library` on the next, indented).

---

# Part 4. Auto-configuration Without Mysticism

## 4.1 Concept

Auto-configuration is a set of regular @Configuration classes that are **already written by Spring developers** and included within the starter dependencies. They are marked with conditional annotations like "create this bean **only if** such-and-such class exists on the classpath, and **only if** the user hasn't yet created their own bean of this type." There's no magic involved—it's code that follows the same @Configuration/@Bean rules you already know, but this code isn't written by you; it's included with the starter.

A concrete example of what happened "automatically": because of `spring-boot-starter-web` on the classpath, auto-configuration saw Tomcat and Spring MVC and automatically created: an embedded Tomcat server, a `DispatcherServlet` (this is the object that receives incoming HTTP requests and decides which of your methods to call), and a default error page (Whitelabel Error Page) - you'll see it in Part 5 when you run the application without a single controller.

## 4.2 Where this can be seen in practice

When you run `LibraryRestDemoApplication`, lines like this will appear in the console:

```
Tomcat initialized with port 8080 (http)
Tomcat started on port(s): 8080 (http) with context path ''
Started LibraryRestDemoApplication in 1.2 seconds
```

This is the result of auto-configuration: not a single line of code on your part was required to set up a web server on port 8080. By the way, you can change the port with a single line in `application.properties` – auto-configuration will read this value automatically:

```
server.port=8081
```

---

# Part 5. The First REST Controller

## 5.1 @RestController and @GetMapping

Create a new `HelloController` class In the same package as the main application class (or in its subpackage—otherwise, component scanning won't find it; see Section 3.1):

```java
package org.example.libraryrestdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

@GetMapping("/hello")
public String hello() {
return "Hello, Spring Boot!";
}
}
```

Parsing:

- `@RestController` is a stereotype (like `@Service`/`@Repository`), meaning this class is a bean, and Spring will find it via component scanning just like any `@Component`. An additional meaning of this annotation: everything returned by methods within the class is sent directly to the HTTP response body, rather than being used to locate an HTML page (this is the behavior of @RestController , unlike regular @Controller , which, under the hood, expects the method to return the name of the page to display).
- @GetMapping("/hello") — says DispatcherServlet (part 4.1): "If an HTTP GET request arrives at the path '/hello', call this method."
- The method returns a String — this means the response body will be exactly this text, without any wrapping.

## 5.2 Running and Testing

1. Run LibraryRestDemoApplication (press the ▶ button on main , as usual). This time, the process will not terminate — that's correct, the server continues to run. 2. Open `http://localhost:8080/hello` in your browser. A page with the text `Hello, Spring Boot!` should appear.
3. To stop the server, click ⏹ `Stop` in IntelliJ. Without this, port 8080 will remain in use, and restarting it may return a "port already in use" error.

## 5.3 What if I return an object instead of a string?

If a method returns a regular class (without any special annotations) rather than a `String`, Spring (thanks to the Jackson library, which comes with the `spring-boot-starter-web` starter – see section 2.3) will automatically convert the object to JSON.

```java
package org.example.libraryrestdemo;

public class StatusResponse {
private String message;
private boolean ok;

public StatusResponse(String message, boolean ok) { 
this.message = message; 
this.ok = ok; 
} 

public String getMessage() { 
return message; 
} 

public boolean isOk() { 
return ok; 
}
}
```

```java
package org.example.libraryrestdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController { 

@GetMapping("/hello") 
public String hello() { 
return "Hello, Spring Boot!"; 
}

@GetMapping("/status")
public StatusResponse status() {
return new StatusResponse("Server is running", true);
}
}
```

Open `http://localhost:8080/status` - instead of text, you'll see JSON: `{"message":"Server is running","ok":true}`. Converting a `StatusResponse` object into such a string is called **serialization**, and when working with `@RestController`, this happens automatically. We'll return to this in more detail in a future lesson. For now, it's enough to know that this behavior works out of the box and that the field names in the JSON are taken from the get method names (`getMessage` → `message`, `isOk` → `ok`).

--

# Lab — "Book Catalog REST API"

**Goal:** Create a real Spring Boot project from scratch using Initializr and write a few working REST endpoints using only what was covered in this lesson (@RestController, @GetMapping, auto-wiring of application.properties, @Value).

---

## Level 1 — Creating a Project

Create a new project using https://start.spring.io following the steps from Part 2.1:
- Group: org.example, Artifact: library-catalog-api, Java: 21, Dependency: Spring Web only.

Import it into IntelliJ and run the main class (LibraryCatalogApiApplication). The console should display lines about Tomcat started on port(s): 8080.

Open http://localhost:8080/ in your browser (without any path). You'll see a Whitelabel Error Page with the code 404.

Hint: This is expected behavior, not an error—the server is running (as seen in the console), but no controllers are currently handling the / path. This page itself is the result of the same auto-configuration from section 4.1.*

Question: Why is the error 404 Not Found and not, for example, 500 Internal Server Error?

---

## Level 2 — First Endpoint

Create a @RestController class named CatalogController in the root package of your project (the same one as the main class) with one method:

``java
@GetMapping("/hello")
```

which returns the string `"Library Catalog API"`.

Restart the application (first ⏹ `Stop`, then ▶ restart — Boot doesn't pick up code changes on the fly) and check `http://localhost:8080/hello`.

*Hint: Use the exact pattern from Section 5.1 — stereotype on the class, `@GetMapping` on the method.*

---

## Level 3 — Multiple Endpoints and a JSON Response

1. Create a `BookStatus` class (a regular class, without annotations) with `String title`, `boolean available` fields, a constructor, and get methods — following the `StatusResponse` model from Section 5.3.
2. Add the following method to `CatalogController`:

```java
@GetMapping("/books/sample")
```

which returns `new BookStatus("Harry Potter", false)`. 3. Restart the application and open `http://localhost:8080/books/sample` — make sure you see JSON, not plain text.

*Hint: The field names in the JSON will be `title` and `available` — they are taken from the getter method names (`getTitle`, `isAvailable`), not from the field names directly (although in this case they are the same).*

**Question:** What will the browser display on `http://localhost:8080/hello` and on `http://localhost:8080/books/sample`? How will the response (content type) visually differ?

---

## Level 4 — Configuration without @PropertySource

1. In `src/main/resources/application.properties`, add the line:

```
library.name=City Library
```

2. In `CatalogController`, add the `@Value("${library.name}") String libraryName` dependency via the constructor (as in section 3.3 of one of the previous lessons — the `@Value` syntax doesn't change in Boot).
3. Add the `@GetMapping("/library-name")` method, returning `libraryName`.
4. Restart and check `http://localhost:8080/library-name`.

*Hint: the difference from the previous lesson is that `@PropertySource` is not needed here; Boot includes it automatically. But Cyrillic may still come out as gibberish—the reason is the same as before (ISO-8859-1 is the default for .properties files), and it's not affected by Boot. If you see garbled text, this is expected; fixes are discussed in Section 3.3.*

---

## Level 5 (bonus) — Changing the port via configuration

Add the following line to 'application.properties':

```
server.port=8081
```

Restart the application and verify that the console now shows 'Tomcat started on port(s): 8081', and that the old addresses ('http://localhost:8080/...') no longer respond—you need to open 'http://localhost:8081/...'.

*Hint: You didn't write a single line of Java code—only the value in the configuration changed, and auto-configuration picked it up on the next run.*

---

## Final Checklist

- [ ] A new Boot project was created using Initializr, launched, and listened to a port.
- [ ] A `CatalogController` with at least three `@GetMapping` methods.
- [ ] At least one endpoint returns plain text, and at least one returns a JSON object.
- [ ] The value from `application.properties` is read via `@Value` without `@PropertySource`.
- [ ] The server port is changed via `application.properties` without changing any code.

---
