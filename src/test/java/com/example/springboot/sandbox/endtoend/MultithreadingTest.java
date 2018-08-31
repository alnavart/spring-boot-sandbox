package com.example.springboot.sandbox.endtoend;

import com.example.springboot.sandbox.infrastructure.repository.springdata.Book;
import com.example.springboot.sandbox.infrastructure.repository.springdata.BookFixtureFactory;
import com.example.springboot.sandbox.infrastructure.repository.springdata.BookRepository;
import com.example.springboot.sandbox.infrastructure.repository.springdata.RevisionAssertions;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static com.example.springboot.sandbox.infrastructure.api.rest.springmvc.RequestScopeGeneralInfo.USERNAME_HEADER_LABEL;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultithreadingTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private BookRepository bookRepository;

    private final String aUserName = "alnavart";
    private final String noUserName = "";
    private RevisionAssertions revisionAssertions;
    private final Book aBook = BookFixtureFactory.aRandomBook();

    @Before
    public void setUp() {
        revisionAssertions = new RevisionAssertions(bookRepository);
    }

    @Test
    public void bookSavedAsyncNotPropagatesRequestInfo() {
        HttpHeaders httpHeaders = userNameHeaders(aUserName);
        ResponseEntity<Book> response = restTemplate.exchange("/api/books", HttpMethod.POST,
                new HttpEntity<>(aBook, httpHeaders), Book.class);
        Book savedBook = response.getBody();
        aBook.setId(Objects.requireNonNull(savedBook).getId());

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(savedBook, equalTo(aBook));
        assertRevisions(savedBook, noUserName, 1);
    }

    private void assertRevisions(Book book, String expectedUserName, int expectedRevisionsCount) {
        revisionAssertions.assertRevisions(book.getId(), expectedUserName, expectedRevisionsCount);
    }

    private HttpHeaders userNameHeaders(String userName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(USERNAME_HEADER_LABEL, userName);
        return httpHeaders;
    }
}
