package com.example.springboot.sandbox.infrastructure.repository.springdata;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class CustomerTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Customer.class).verify();
    }

}