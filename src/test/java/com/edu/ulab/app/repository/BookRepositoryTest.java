package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
@Rollback
@Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
})
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @Test
    @DisplayName("Сохранить книгу и юзера. Число select должно равняться 2")
    void insertBook_thenAssertDmlCount() {
        //Given

        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setUserId(savedPerson.getId());

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    @Test
    @DisplayName("Получить книгу по Id. Число select должно равняться 1")
    void getBookById_thenAssertDmlCount() {
        //Given
        long id = 2002l;

        //When
        Book book = bookRepository.findById(id).get();

        //Then
        assertThat(book.getAuthor()).isEqualTo("author");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    @Test
    @DisplayName("Получить все книги. Число select должно равняться 1")
    void getAllBooks_thenAssertDmlCount() {
        //Given
        List<Book> bookList = new ArrayList<>();
        //When
        for (Book book : bookRepository.findAll()) {
            bookList.add(book);
        }
        //Then
        assertThat(bookList.size()).isEqualTo(2);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    @Test
    @DisplayName("Обновить книгу. Число select должно равняться 2")
    void updateBook_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(10);
        person.setTitle("reader");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        long bookId = 2002L;

        Book book = new Book();
        book.setUserId(savedPerson.getId());
        book.setId(bookId);
        book.setPageCount(1500);
        book.setTitle("test");
        book.setAuthor("author test");
        //When
        Book savedBook = bookRepository.save(book);

        //Then
        assertThat(savedBook.getUserId()).isEqualTo(savedPerson.getId());
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    @Test
    @DisplayName("Удалить книгу по Id. Число select должно равняться 1")
    void deletePersonById_thenAssertDmlCount() {
        //Given
        long id = 2002l;

        //When
        bookRepository.deleteById(id);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
        assertThatThrownBy(() -> {
            bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("book with id " + id + " not found"));
        }).isInstanceOf(BookNotFoundException.class);
    }
    @Test
    @DisplayName("Удалить книги по id пользователя. Число select должно равняться 1")
    void deleteBooksByUserId_thenAssertDmlCount() {
        //Given
        long userId = 1001L;
        long bookId1 = 2002L;
        long bookId2 = 3003L;

        //When
        bookRepository.deleteBooksByUserId(userId);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
        assertThatThrownBy(() -> {
            bookRepository.findById(bookId1).orElseThrow(() -> new BookNotFoundException("book with id " + bookId1 + " not found"));
        }).isInstanceOf(BookNotFoundException.class);
        assertThatThrownBy(() -> {
            bookRepository.findById(bookId2).orElseThrow(() -> new BookNotFoundException("book with id " + bookId2 + " not found"));
        }).isInstanceOf(BookNotFoundException.class);
    }
    @Test
    @DisplayName("Получить книги по id пользователя. Число select должно равняться 1")
    void findBooksByUserId_thenAssertDmlCount() {
        //Given
        long userId = 1001l;
        //When
        List<Book> bookList = bookRepository.findBooksByUserId(userId);

        //Then
        assertThat(bookList.size()).isEqualTo(2);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }



    // update
    // get
    // get all
    // delete

    // * failed


    // example failed test
}
