package com.example.springboot.sandbox.infrastructure.api.rest.springmvc;

import com.example.springboot.sandbox.infrastructure.repository.springdata.Customer;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerAPI(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{id}")
    public Customer getCustomers(@PathVariable Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer)
    {
        return new ResponseEntity<>(customerRepository.save(customer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@RequestBody Customer newCustomer, @PathVariable Integer id) {

        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(newCustomer.getFirstName());
                    customer.setLastName(newCustomer.getLastName());
                    return customerRepository.save(customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    return customerRepository.save(newCustomer);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        customerRepository.deleteById(id);
    }
}
