package com.example.springboot.sandbox.infrastructure.repository.springdata;

import static com.example.springboot.sandbox.domain.test.Randomizer.randomString;

public class BookFixtureFactory {

    public static Book aRandomBook() {
        return new Book(randomString(), randomString());
    }

}
