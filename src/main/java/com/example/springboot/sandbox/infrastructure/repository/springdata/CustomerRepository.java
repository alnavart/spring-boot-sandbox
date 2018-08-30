package com.example.springboot.sandbox.infrastructure.repository.springdata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

public interface CustomerRepository extends RevisionRepository<Customer, Integer, Integer>, CrudRepository<Customer, Integer> {
    List<Customer> findByLastName(String lastName);
}

