package com.example.springboot.sandbox;


import com.example.springboot.sandbox.infrastructure.repository.springdata.Customer;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRepository;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRevisionAssertions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestScopeGeneralInfo.USERNAME_HEADER_LABEL;
import static com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerFixtureFactory.aRandomCustomer;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConcurrencyTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CustomerRepository customerRepository;

    private final String aUserName = "alnavart";
    private final String userNamePaco = "paco";
    private final String userNameAlf = "alf";
    private CustomerRevisionAssertions customerRevisionAssertions;

    @Before
    public void setUp() {
        customerRevisionAssertions = new CustomerRevisionAssertions(customerRepository);
    }

    @Test
    public void postsWithConcurrency() {
        List<CustomerCreation> customerCreations = new ArrayList<>();
        repeat(100, () -> customerCreations.add(randomCustomerCreation()));

        List<CustomerCreation> customersCreated = parallelPost(customerCreations);

        customersCreated.forEach(
                customerCreation -> assertRevisions(customerCreation.getCustomer(), customerCreation.getCreatorUsername(), 1));

    }

    private List<CustomerCreation> parallelPost(List<CustomerCreation> customerCreations) {
        List<CompletableFuture<CustomerCreation>> futures = customerCreations.stream()
                .map(this::postAsync)
                .collect(toList());
        return futures.stream().map(CompletableFuture::join).collect(toList());
    }

    private CompletableFuture<CustomerCreation> postAsync(CustomerCreation customerCreation) {
        CompletableFuture<CustomerCreation> future = CompletableFuture.supplyAsync(new Supplier<CustomerCreation>() {
            @Override
            public CustomerCreation get() {
                return post(customerCreation);
            }
        });

        return future;
    }

    private CustomerCreation post(CustomerCreation customerCreation) {
        HttpHeaders httpHeaders = userNameHeaders(customerCreation.getCreatorUsername());
        ResponseEntity<Customer> response = restTemplate.exchange("/api/customers", HttpMethod.POST,
                new HttpEntity<>(customerCreation.getCustomer(), httpHeaders), Customer.class);
        Customer savedCustomer = response.getBody();
        customerCreation.setCustomer(savedCustomer);
        return customerCreation;
    }

    private HttpHeaders userNameHeaders(String userName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(USERNAME_HEADER_LABEL, userName);
        return httpHeaders;
    }

    private void assertRevisions(Customer customer, String expectedUserName, int expectedRevisionsCount) {
        customerRevisionAssertions.assertRevisions(customer, expectedUserName, expectedRevisionsCount);
    }

    private CustomerCreation randomCustomerCreation() {
        return new CustomerCreation(aRandomCustomer(), randomCreator());
    }

    private String randomCreator() {
        List<String> usernames = asList(aUserName, userNamePaco, userNameAlf);
        Random randomizer = new Random();
        return usernames.get(randomizer.nextInt(usernames.size()));
    }

    private void repeat(int count, Runnable action) {
        IntStream.range(0, count).forEach(i -> action.run());
    }

    @Data
    @AllArgsConstructor
    private class CustomerCreation {
        Customer customer;
        String creatorUsername;

    }
}
