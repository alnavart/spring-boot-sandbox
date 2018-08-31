package com.example.springboot.sandbox.infrastructure.repository.springdata;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;


@Log4j2
public class CustomerRevisionAssertions {

    private final RevisionRepository customerRepository;
    private final Gson gson;

    public CustomerRevisionAssertions(RevisionRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.gson = new Gson();
    }

    public void assertRevisions(Object objectId, String expectedUserName, int expectedRevisionsCount) {
        @SuppressWarnings("unchecked")  //Be careful, type for id is not checked
        Revisions<Integer, Customer> actualRevisions = customerRepository.findRevisions(objectId);
        log.info(String.format("Revisions for id %s : %s", objectId, gson.toJson(actualRevisions)));
        List<Revision<Integer, Customer>> revisions = actualRevisions.getContent();
        assertEquals(expectedRevisionsCount, revisions.size());
        for (Revision<Integer, Customer> revision : revisions) {
            CustomRevisionEntity revEntity = revision.getMetadata().getDelegate();
            assertEquals(revEntity.getUsername(), expectedUserName);
        }

    }
}
