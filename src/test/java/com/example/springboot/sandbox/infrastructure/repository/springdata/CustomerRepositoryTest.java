package com.example.springboot.sandbox.infrastructure.repository.springdata;

import com.example.springboot.sandbox.commons.Commons;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void auditsWithEnvers() {
        String userName = setUserActionsExecutor();
        Customer bauaer = customerRepository.save(new Customer("Jack", "Bauer"));
        Customer brian = customerRepository.save(new Customer("Chloe", "O'Brian"));
        Customer kim = customerRepository.save(new Customer("Kim", "Bauer"));
        Customer palmer = customerRepository.save(new Customer("David", "Palmer"));
        Customer michelle = customerRepository.save(new Customer("Michelle", "Dessler"));

        bauaer.setLastName("Bauer 2");
        customerRepository.save(bauaer);

        assertEquals(5L, customerRepository.count());
        assertRevisions(bauaer, userName, 2);
        assertRevisions(brian, userName, 1);
        assertRevisions(kim, userName, 1);
        assertRevisions(palmer, userName, 1);
        assertRevisions(michelle, userName, 1);
    }

    private void assertRevisions(Customer customer, String expectedUserName, int expectedRevisionsCount) {
        Revisions<Integer, Customer> actualRevisions = customerRepository.findRevisions(customer.getId());
        List<Revision<Integer, Customer>> revisions = actualRevisions.getContent();
        assertEquals(expectedRevisionsCount, revisions.size());
        for (Revision<Integer, Customer> revision : revisions) {
            CustomerRevEntity revEntity = revision.getMetadata().getDelegate();
            assertEquals(revEntity.getUsername(), expectedUserName);
        }

    }

    private String setUserActionsExecutor() {
        String user = String.format("testUser_%s", (new Date()).getTime());
        Commons.USER.set(user);
        return user;
    }
}