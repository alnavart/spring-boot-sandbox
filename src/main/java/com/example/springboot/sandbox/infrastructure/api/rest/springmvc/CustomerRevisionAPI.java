package com.example.springboot.sandbox.infrastructure.api.rest.springmvc;

import com.example.springboot.sandbox.infrastructure.repository.springdata.Customer;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revisions;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/customer-revision")
public class CustomerRevisionAPI {

    private static final Logger log = LoggerFactory.getLogger(CustomerRevisionAPI.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRevisionAPI(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{customerId}")
	@Transactional
    public Revisions<Integer, Customer> listCustomerRevisions(@PathVariable Integer customerId) {
        Revisions<Integer, Customer> revisions = customerRepository.findRevisions(customerId);
        log.info(String.format("Customer revisions found %s", revisions));
        return revisions;
    }
}
