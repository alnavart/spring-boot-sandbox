package com.example.springboot.sandbox.infrastructure.repository.springdata;

import com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestScopeGeneralInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;
    @MockBean
    RequestScopeGeneralInfo requestScopeGeneralInfo;

    private RevisionAssertions revisionAssertions;

    @Before
    public void setUp() {
        revisionAssertions = new RevisionAssertions(customerRepository);
    }

    @Test
    public void auditsWithEnvers() {
        String userName = setUserActionsExecutor();
        Customer bauaer = customerRepository.save(CustomerFixtureFactory.jack());
        Customer brian = customerRepository.save(CustomerFixtureFactory.chloe());
        Customer kim = customerRepository.save(CustomerFixtureFactory.kim());
        Customer palmer = customerRepository.save(CustomerFixtureFactory.david());
        Customer michelle = customerRepository.save(CustomerFixtureFactory.michelle());

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
        revisionAssertions.assertRevisions(customer.getId(), expectedUserName, expectedRevisionsCount);
    }

    private String setUserActionsExecutor() {
        String user = String.format("testUser_%s", (new Date()).getTime());
        given(this.requestScopeGeneralInfo.getUserName()).willReturn(user);
        return user;
    }
}