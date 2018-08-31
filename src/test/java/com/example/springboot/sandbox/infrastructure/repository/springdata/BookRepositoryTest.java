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
public class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @MockBean
    RequestScopeGeneralInfo requestScopeGeneralInfo;

    private RevisionAssertions revisionAssertions;

    @Before
    public void setUp() {
        revisionAssertions = new RevisionAssertions(bookRepository);
    }

    @Test
    public void auditsWithEnvers() {
        String userName = setUserActionsExecutor();
        Book aBook = bookRepository.save(BookFixtureFactory.aRandomBook());
        Book otherBook = bookRepository.save(BookFixtureFactory.aRandomBook());
        Book anotherBook = bookRepository.save(BookFixtureFactory.aRandomBook());

        aBook.setAuthor("Ana Rosa");
        bookRepository.save(aBook);

        assertEquals(3L, bookRepository.count());
        assertRevisions(aBook, userName, 2);
        assertRevisions(otherBook, userName, 1);
        assertRevisions(anotherBook, userName, 1);
    }

    private void assertRevisions(Book book, String expectedUserName, int expectedRevisionsCount) {
        revisionAssertions.assertRevisions(book.getId(), expectedUserName, expectedRevisionsCount);
    }

    private String setUserActionsExecutor() {
        String user = String.format("testUser_%s", (new Date()).getTime());
        given(this.requestScopeGeneralInfo.getUserName()).willReturn(user);
        return user;
    }
}