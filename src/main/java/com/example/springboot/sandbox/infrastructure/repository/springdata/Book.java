package com.example.springboot.sandbox.infrastructure.repository.springdata;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Audited
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Integer id;
    String title;
    String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
