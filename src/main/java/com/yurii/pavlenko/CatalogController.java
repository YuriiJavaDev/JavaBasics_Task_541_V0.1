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
