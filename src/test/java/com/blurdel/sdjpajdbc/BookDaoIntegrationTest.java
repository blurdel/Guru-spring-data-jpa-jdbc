package com.blurdel.sdjpajdbc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.blurdel.sdjpajdbc.dao.BookDao;
import com.blurdel.sdjpajdbc.dao.BookDaoImpl;
import com.blurdel.sdjpajdbc.domain.Book;

@ActiveProfiles("mysql")
@DataJpaTest
//@ComponentScan(basePackages = {"com.blurdel.sdjpajdbc.dao"})
@Import(BookDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoIntegrationTest {

	@Autowired
	BookDao bookDao;
	
	
	@Test
    void testDeleteBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(3L);
        Book saved = bookDao.saveNew(book);

        bookDao.delete(saved.getId());

        Book deleted = bookDao.getById(saved.getId());

        assertThat(deleted).isNull();
    }

    @Test
    void updateBookTest() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(3L);
        Book saved = bookDao.saveNew(book);

        saved.setTitle("New Book");
        bookDao.update(saved);

        Book fetched = bookDao.getById(saved.getId());

        assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void testSaveBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthorId(3L);
        Book saved = bookDao.saveNew(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void testGetBookByName() {
        Book book = bookDao.getByTitle("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testGetBook() {
        Book book = bookDao.getById(3L);

        assertThat(book.getId()).isNotNull();
    }
    
}
