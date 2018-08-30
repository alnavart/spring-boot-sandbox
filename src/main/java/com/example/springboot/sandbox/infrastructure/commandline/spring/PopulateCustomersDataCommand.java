package com.example.springboot.sandbox.infrastructure.commandline.spring;

import com.example.springboot.sandbox.infrastructure.repository.springdata.Customer;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "populate.customers.autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PopulateCustomersDataCommand implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(PopulateCustomersDataCommand.class);

    private final CustomerRepository repository;

    @Autowired
    public PopulateCustomersDataCommand(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        Customer bauaer = repository.save(new Customer("Jack", "Bauer"));
        repository.save(new Customer("Chloe", "O'Brian"));
        repository.save(new Customer("Kim", "Bauer"));
        repository.save(new Customer("David", "Palmer"));
        repository.save(new Customer("Michelle", "Dessler"));

        //Modify to generate an audit entry
        bauaer.setLastName("Bauer 2");
        repository.save(bauaer);

        printAllUsers();
    }

    private void printAllUsers() {
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (Customer customer : repository.findAll()) {
            log.info(customer.toString());
        }
        log.info("");
    }
}
