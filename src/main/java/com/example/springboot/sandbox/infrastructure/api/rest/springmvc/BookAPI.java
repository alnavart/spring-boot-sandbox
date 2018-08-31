package com.example.springboot.sandbox.infrastructure.api.rest.springmvc;

import com.example.springboot.sandbox.infrastructure.repository.springdata.Book;
import com.example.springboot.sandbox.infrastructure.repository.springdata.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookAPI {

    private final BookRepository bookRepository;

    @Autowired
    public BookAPI(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book)
    {
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book newBook, @PathVariable Integer id) {

        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setAuthor(newBook.getAuthor());
                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return bookRepository.save(newBook);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookRepository.deleteById(id);
    }
}
