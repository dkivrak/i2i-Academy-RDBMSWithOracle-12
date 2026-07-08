package com.i2i.rdbmsoracle.controller;

import com.i2i.rdbmsoracle.dto.BookDto;
import com.i2i.rdbmsoracle.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importBooks(@RequestBody String rawData) {
        try {
            bookService.importBooks(rawData);
            return ResponseEntity.ok("Books imported successfully.");
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Failed to import books: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
}