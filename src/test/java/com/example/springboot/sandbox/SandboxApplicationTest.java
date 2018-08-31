package com.example.springboot.sandbox;


import com.example.springboot.sandbox.infrastructure.repository.springdata.Customer;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerFixtureFactory;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRepository;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRevisionAssertions;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestScopeGeneralInfo.USERNAME_HEADER_LABEL;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SandboxApplicationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CustomerRepository customerRepository;

    private final String aUserName = "alnavart";
    private final String noUserName = "";
    private CustomerRevisionAssertions customerRevisionAssertions;
    private Customer bauaer;
    private Customer brian;
    private Customer kim;

    @Before
    public void setUp() {
        customerRevisionAssertions = new CustomerRevisionAssertions(customerRepository);
        kim = CustomerFixtureFactory.kim();
        bauaer = customerRepository.save(CustomerFixtureFactory.jack());
        brian = customerRepository.save(CustomerFixtureFactory.chloe());
    }

    @Test
    public void getsCustomer() {
        Customer actualCustomer =
                this.restTemplate.getForObject(String.format("/api/customers/%s", bauaer.getId()), Customer.class);

        assertThat(actualCustomer, equalTo(bauaer));
        assertRevisions(bauaer, noUserName, 1);
        assertRevisions(brian, noUserName, 1);
    }

    @Test
    public void postsCustomer() {
        HttpHeaders httpHeaders = userNameHeaders(aUserName);
        ResponseEntity<Customer> response = restTemplate.exchange("/api/customers", HttpMethod.POST,
                new HttpEntity<>(kim, httpHeaders), Customer.class);
        Customer savedKim = response.getBody();
        kim.setId(savedKim.getId());

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(savedKim, equalTo(kim));
        assertRevisions(bauaer, noUserName, 1);
        assertRevisions(brian, noUserName, 1);
        assertRevisions(savedKim, aUserName, 1);
    }

    @Test
    public void putsCustomer() {
        HttpHeaders httpHeaders = userNameHeaders(aUserName);
        ResponseEntity<Customer> postResponse = restTemplate.exchange("/api/customers", HttpMethod.POST,
                new HttpEntity<>(kim, httpHeaders), Customer.class);
        Customer savedKim = postResponse.getBody();
        kim.setId(savedKim.getId());
        kim.setFirstName("Manolo");
        ResponseEntity<Customer> putResponse = restTemplate.exchange(String.format("/api/customers/%s", kim.getId()),
                HttpMethod.PUT, new HttpEntity<>(kim, httpHeaders), Customer.class);

        assertThat(putResponse.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(putResponse.getBody(), equalTo(kim));
        assertRevisions(bauaer, noUserName, 1);
        assertRevisions(brian, noUserName, 1);
        assertRevisions(savedKim, aUserName, 2);
    }

    private void assertRevisions(Customer customer, String expectedUserName, int expectedRevisionsCount) {
        customerRevisionAssertions.assertRevisions(customer, expectedUserName, expectedRevisionsCount);
    }

    private HttpHeaders userNameHeaders(String userName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(USERNAME_HEADER_LABEL, userName);
        return httpHeaders;
    }
}
