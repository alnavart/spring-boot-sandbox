package com.example.springboot.sandbox.domain.test;

import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Randomizer {

    private static final int RANDOM_STRING_LENGTH = 12;

    public static String randomString() {
        byte[] array = new byte[RANDOM_STRING_LENGTH];
        new Random().nextBytes(array);
        return new String(array, UTF_8);
    }
}
