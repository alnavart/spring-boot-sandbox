package com.example.springboot.sandbox.infrastructure.api.rest.springmvc;

import com.example.springboot.sandbox.infrastructure.repository.springdata.Customer;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/customer")
public class CustomerAPI {

    private static final Logger log = LoggerFactory.getLogger(CustomerAPI.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerAPI(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{customerId}")
	@Transactional
    public Customer listCustomerRevisions(@PathVariable Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        log.info(String.format("Customer found %s", customer));
        return customer;
    }
}
