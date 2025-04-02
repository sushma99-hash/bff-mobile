package cmu.edu.client;

import cmu.edu.ds.Models.Books;
//import configuration.FeignConfig;
//import models.Books;
import cmu.edu.configuration.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "A1", url = "internal-bookstore-dev-InternalALB-126543766.us-east-1.elb.amazonaws.com:3000/books", configuration = FeignConfig.class)
//@FeignClient(name = "A1", url = "http://localhost:3000/books", configuration = FeignConfig.class)
public interface BooksClient {
//        @GetMapping
//        Object getAllBooks();

    @PostMapping
    Object addBook(Books book);

    @GetMapping("/{isbn}")
    Object getBookByIsbn(@PathVariable("isbn") String isbn);

    @GetMapping("/isbn/{isbn}")
    Object getBookByIsbnAlt(@PathVariable("isbn") String isbn);

    @GetMapping("/status")
    Object getStatus();
}
