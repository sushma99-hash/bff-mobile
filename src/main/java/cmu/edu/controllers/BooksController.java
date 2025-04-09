package cmu.edu.controllers;

import cmu.edu.client.BooksClient;
import cmu.edu.ds.Models.Books;
import cmu.edu.errors.CustomFeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

// Controller for Books endpoints
@RestController
@RequestMapping("/books")
public class BooksController {
    private final BooksClient booksClient;

    public BooksController(BooksClient booksClient) {
        this.booksClient = booksClient;
    }

    @PostMapping
    public ResponseEntity<Object> addBook(@RequestBody Books book) {
        try {
            Object result = booksClient.addBook(book);
//            return ResponseEntity.ok(result);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (CustomFeignException e) {
            return ResponseEntity.status(e.getStatus())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBody());
        }
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Object> getBookByIsbn(@PathVariable String isbn) {
        try {
            Object result = booksClient.getBookByIsbn(isbn);

            // Transform "non-fiction" genre to numeric 3 for mobile BFF
            if (result instanceof LinkedHashMap) {
                LinkedHashMap<String, Object> bookMap = (LinkedHashMap<String, Object>) result;
                if ("non-fiction".equals(bookMap.get("genre"))) {
                    bookMap.put("genre", 3);
                }
            }

            return ResponseEntity.ok(result);
        } catch (CustomFeignException e) {
            return ResponseEntity.status(e.getStatus())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBody());
        }
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Object> getBookByIsbnAlt(@PathVariable String isbn) {
        return getBookByIsbn(isbn);
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Object> updateBook(@PathVariable String isbn, @RequestBody Books book) {
        try {
            Object result = booksClient.updateBook(isbn, book);
            return ResponseEntity.ok(result);
        } catch (CustomFeignException e) {
            return ResponseEntity.status(e.getStatus())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBody());
        }
    }
}