package cmu.edu.controllers;//package cmu.edu.controllers;
//
//import cmu.edu.ds.Models.Books;
//import cmu.edu.client.BooksClient;
//import cmu.edu.ds.errors.CustomFeignException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.StreamUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//// Controller for Books endpoints
//@RestController
//@RequestMapping("/books")
//public class BooksController {
//    private final BooksClient booksClient;
//
//    public BooksController(BooksClient booksClient) {
//        this.booksClient = booksClient;
//    }
//
////        @GetMapping
////        public ResponseEntity<Object> getAllBooks() {
////            return ResponseEntity.ok(booksClient.getAllBooks());
////        }
//
//    @PostMapping
//    public ResponseEntity<Object> addBook(@RequestBody Books book) {
//        try {
//            Object result = booksClient.addBook(book);
//            return ResponseEntity.ok(result);
//        } catch (CustomFeignException e) {
//            try {
//                // Convert the response body to the appropriate format
////                String responseBody = StreamUtils.copyToString(e.getBody().asInputStream(), StandardCharsets.UTF_8);
//                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
//                return ResponseEntity.status(e.getStatus()).body(responseBody);
//            } catch (IOException ioException) {
//                // Fallback if we can't read the response body
//                return ResponseEntity.status(e.getStatus()).build();
//            }
//        }
//    }
//
//    @GetMapping("/{isbn}")
//    public ResponseEntity<Object> getBookByIsbn(@PathVariable String isbn) {
//        try {
//            Object result = booksClient.getBookByIsbn(isbn);
//            return ResponseEntity.ok(result);
//        } catch (CustomFeignException e) {
//            try {
//                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
//                return ResponseEntity.status(e.getStatus()).body(responseBody);
//            } catch (IOException ioException) {
//                return ResponseEntity.status(e.getStatus()).build();
//            }
//        }
//    }
//
//    @GetMapping("/isbn/{isbn}")
//    public ResponseEntity<Object> getBookByIsbnAlt(@PathVariable String isbn) {
//        try {
//            Object result = booksClient.getBookByIsbnAlt(isbn);
//            return ResponseEntity.ok(result);
//        } catch (CustomFeignException e) {
//            try {
//                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
//                return ResponseEntity.status(e.getStatus()).body(responseBody);
//            } catch (IOException ioException) {
//                return ResponseEntity.status(e.getStatus()).build();
//            }
//        }
//    }
//}
//


//package cmu.edu.ds.controllers;

import cmu.edu.ds.Models.Books;
import cmu.edu.client.BooksClient;
import cmu.edu.errors.CustomFeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
            return ResponseEntity.ok(result);
        } catch (CustomFeignException e) {
            try {
                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
                return ResponseEntity.status(e.getStatus()).body(responseBody);
            } catch (IOException ioException) {
                return ResponseEntity.status(e.getStatus()).build();
            }
        }
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Object> getBookByIsbn(
            @PathVariable String isbn,
            @RequestAttribute("clientType") String clientType) {
        try {
            Object result = booksClient.getBookByIsbn(isbn);
            return transformForMobile(result, clientType);
        } catch (CustomFeignException e) {
            try {
                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
                return ResponseEntity.status(e.getStatus()).body(responseBody);
            } catch (IOException ioException) {
                return ResponseEntity.status(e.getStatus()).build();
            }
        }
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Object> getBookByIsbnAlt(
            @PathVariable String isbn,
            @RequestAttribute("clientType") String clientType) {
        try {
            Object result = booksClient.getBookByIsbnAlt(isbn);
            return transformForMobile(result, clientType);
        } catch (CustomFeignException e) {
            try {
                String responseBody = StreamUtils.copyToString(e.getResponseBody(), StandardCharsets.UTF_8);
                return ResponseEntity.status(e.getStatus()).body(responseBody);
            } catch (IOException ioException) {
                return ResponseEntity.status(e.getStatus()).build();
            }
        }
    }

    private ResponseEntity<Object> transformForMobile(Object result, String clientType) {
        if (!(result instanceof Map) || clientType.equals("web")) {
            return ResponseEntity.ok(result);
        }

        // For mobile clients (iOS/Android), replace "non-fiction" with 3
        Map<String, Object> responseMap = (Map<String, Object>) result;
        if (responseMap.containsKey("genre") && "non-fiction".equals(responseMap.get("genre"))) {
            responseMap.put("genre", 3);
        }

        return ResponseEntity.ok(responseMap);
    }
}