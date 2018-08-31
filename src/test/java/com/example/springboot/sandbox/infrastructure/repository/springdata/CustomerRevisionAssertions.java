package com.example.springboot.sandbox.infrastructure.repository.springdata;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import java.util.List;

import static org.junit.Assert.assertEquals;


@Log4j2
public class CustomerRevisionAssertions {

    private final CustomerRepository customerRepository;
    private final Gson gson;

    public CustomerRevisionAssertions(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.gson = new Gson();
    }

    public void assertRevisions(Customer customer, String expectedUserName, int expectedRevisionsCount) {
        Revisions<Integer, Customer> actualRevisions = customerRepository.findRevisions(customer.getId());
        log.info(String.format("Revisions for %s : %s", customer, gson.toJson(actualRevisions)));
        List<Revision<Integer, Customer>> revisions = actualRevisions.getContent();
        assertEquals(expectedRevisionsCount, revisions.size());
        for (Revision<Integer, Customer> revision : revisions) {
            CustomRevisionEntity revEntity = revision.getMetadata().getDelegate();
            assertEquals(revEntity.getUsername(), expectedUserName);
        }

    }
}
