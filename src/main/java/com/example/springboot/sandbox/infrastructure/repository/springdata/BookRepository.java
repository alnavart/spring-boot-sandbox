package com.example.springboot.sandbox.infrastructure.repository.springdata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface BookRepository extends RevisionRepository<Book, Integer, Integer>, CrudRepository<Book, Integer> {
}

