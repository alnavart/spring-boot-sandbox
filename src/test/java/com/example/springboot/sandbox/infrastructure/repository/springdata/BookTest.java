package com.example.springboot.sandbox.infrastructure.repository.springdata;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class BookTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Book.class).verify();
    }

}