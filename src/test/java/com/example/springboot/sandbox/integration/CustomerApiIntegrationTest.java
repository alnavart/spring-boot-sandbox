package com.example.springboot.sandbox.integration;


import com.example.springboot.sandbox.infrastructure.repository.springdata.Customer;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerFixtureFactory;
import com.example.springboot.sandbox.infrastructure.repository.springdata.CustomerRepository;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestScopeGeneralInfo.USERNAME_HEADER_LABEL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;
    private final Gson gson = new Gson();

    private final String userName = "alnavart";
    private Customer bauaer;
    private Customer brian;
    private Customer kim;

    @Before
    public void setUp() {
        kim = CustomerFixtureFactory.kim();
        bauaer = customerRepository.save(CustomerFixtureFactory.jack());
        brian = customerRepository.save(CustomerFixtureFactory.chloe());
    }

    @Test
    public void getsCustomer() throws Exception {
        this.mockMvc.perform(get("/api/customers/{id}", bauaer.getId())
                .header("From", userName)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bauaer.getId())))
                .andExpect(jsonPath("$.firstName", is(bauaer.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(bauaer.getLastName())));
    }

    @Test
    public void postsCustomer() throws Exception {
        this.mockMvc.perform(post("/api/customers/")
                .contentType(MediaType.APPLICATION_JSON)
                .header(USERNAME_HEADER_LABEL, userName)
                .content(gson.toJson(kim))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(greaterThan(brian.getId()))))
                .andExpect(jsonPath("$.firstName", is(kim.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(kim.getLastName())));
    }


    @Test
    public void putsCustomer() throws Exception {
        this.mockMvc.perform(post("/api/customers/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("From", userName)
                .content(gson.toJson(kim))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(greaterThan(brian.getId()))))
                .andExpect(jsonPath("$.firstName", is(kim.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(kim.getLastName())));

        kim = customerRepository.findByLastName(kim.getLastName()).get(0);
        kim.setFirstName("Manolo");

        this.mockMvc.perform(put("/api/customers/{id}/", kim.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(USERNAME_HEADER_LABEL, userName)
                .content(gson.toJson(kim))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(kim.getId())))
                .andExpect(jsonPath("$.firstName", is(kim.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(kim.getLastName())));
    }
}
