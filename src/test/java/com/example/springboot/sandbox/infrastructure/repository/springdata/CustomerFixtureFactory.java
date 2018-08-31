package com.example.springboot.sandbox.infrastructure.repository.springdata;

import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CustomerFixtureFactory {

    private static final int RANDOM_STRING_LENGHT = 12;

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

    private static String randomString() {
        byte[] array = new byte[RANDOM_STRING_LENGHT];
        new Random().nextBytes(array);
        return new String(array, UTF_8);
    }
}
