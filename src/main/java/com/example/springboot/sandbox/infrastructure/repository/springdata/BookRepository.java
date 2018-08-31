package com.example.springboot.sandbox.infrastructure.repository.springdata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

public interface BookRepository extends RevisionRepository<Book, Integer, Integer>, CrudRepository<Book, Integer> {
    List<Book> findAllByAuthor(String author);
}

