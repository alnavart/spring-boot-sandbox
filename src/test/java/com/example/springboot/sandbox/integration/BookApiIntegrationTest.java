package com.example.springboot.sandbox.integration;


import com.example.springboot.sandbox.infrastructure.repository.springdata.Book;
import com.example.springboot.sandbox.infrastructure.repository.springdata.BookFixtureFactory;
import com.example.springboot.sandbox.infrastructure.repository.springdata.BookRepository;
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
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;
    private final Gson gson = new Gson();
    private final MediaType MEDIA_TYPE_JSON_UTF_8 = APPLICATION_JSON_UTF8;

    private final String userName = "alnavart";
    private Book aBook;
    private Book otherBook;
    private Book anotherBook;

    @Before
    public void setUp() {
        anotherBook = BookFixtureFactory.aRandomBook();
        aBook = bookRepository.save(BookFixtureFactory.aRandomBook());
        otherBook = bookRepository.save(BookFixtureFactory.aRandomBook());
    }

    @Test
    public void getsBook() throws Exception {
        this.mockMvc.perform(get("/api/books/{id}", aBook.getId())
                .header("From", userName)
                .accept(MEDIA_TYPE_JSON_UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(aBook.getId())))
                .andExpect(jsonPath("$.title", is(aBook.getTitle())))
                .andExpect(jsonPath("$.author", is(aBook.getAuthor())));
    }

    @Test
    public void postsBook() throws Exception {
        this.mockMvc.perform(post("/api/books/")
                .contentType(MEDIA_TYPE_JSON_UTF_8)
                .header(USERNAME_HEADER_LABEL, userName)
                .content(gson.toJson(anotherBook))
                .accept(MEDIA_TYPE_JSON_UTF_8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(greaterThan(otherBook.getId()))))
                .andExpect(jsonPath("$.title", is(anotherBook.getTitle())))
                .andExpect(jsonPath("$.author", is(anotherBook.getAuthor())));
    }


    @Test
    public void putsBook() throws Exception {
        this.mockMvc.perform(post("/api/books/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("From", userName)
                .content(gson.toJson(anotherBook))
                .accept(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(greaterThan(otherBook.getId()))))
                .andExpect(jsonPath("$.title", is(anotherBook.getTitle())))
                .andExpect(jsonPath("$.author", is(anotherBook.getAuthor())));

        anotherBook = bookRepository.findAllByAuthor(anotherBook.getAuthor()).get(0);
        anotherBook.setAuthor("Ana Rosa");

        this.mockMvc.perform(put("/api/books/{id}/", anotherBook.getId())
                .contentType(MEDIA_TYPE_JSON_UTF_8)
                .header(USERNAME_HEADER_LABEL, userName)
                .content(gson.toJson(anotherBook))
                .accept(MEDIA_TYPE_JSON_UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(anotherBook.getId())))
                .andExpect(jsonPath("$.title", is(anotherBook.getTitle())))
                .andExpect(jsonPath("$.author", is(anotherBook.getAuthor())));
    }
}
