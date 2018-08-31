package com.example.springboot.sandbox.infrastructure.repository.springdata;

import static com.example.springboot.sandbox.domain.test.Randomizer.randomString;

public class CustomerFixtureFactory {

    public static Customer kim() {
        return new Customer("Kim", "Bauer");
    }

    public static Customer jack() {
        return new Customer("Jack", "Bauer");
    }

    public static Customer chloe() {
        return new Customer("Chloe", "O'Brian");
    }

    public static Customer david() {
        return new Customer("David", "Palmer");
    }

    public static Customer michelle() {
        return new Customer("Michelle", "Dessler");
    }

    public static Customer aRandomCustomer() {
        return new Customer(randomString(), randomString());
    }

}
