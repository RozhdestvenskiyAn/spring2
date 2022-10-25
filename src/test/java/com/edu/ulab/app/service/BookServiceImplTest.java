package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setUserId(person.getId());

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setUserId(person.getId());

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление книги. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        Person person = new Person();
        person.setId(2L);

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setUserId(person.getId());
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        Book gotBook = new Book();
        gotBook.setPageCount(2000);
        gotBook.setTitle("title");
        gotBook.setAuthor("author");
        gotBook.setUserId(1L);
        gotBook.setId(bookDto.getId());

        Book updatedBook = new Book();
        updatedBook.setId(gotBook.getId());
        updatedBook.setPageCount(bookDto.getPageCount());
        updatedBook.setTitle(bookDto.getTitle());
        updatedBook.setAuthor(bookDto.getAuthor());
        updatedBook.setUserId(bookDto.getUserId());

        BookDto result = new BookDto();
        result.setId(updatedBook.getId());
        result.setUserId(updatedBook.getUserId());
        result.setAuthor(updatedBook.getAuthor());
        result.setTitle(updatedBook.getTitle());
        result.setPageCount(updatedBook.getPageCount());


        //when
        when(bookRepository.findById(bookDto.getId())).thenReturn(Optional.of(gotBook));
        when(bookRepository.save(gotBook)).thenReturn(updatedBook);
        when(bookMapper.bookToBookDto(updatedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.updateBook(bookDto);
        assertEquals(1000L, bookDtoResult.getPageCount());
    }

    @Test
    @DisplayName("Получение книги по id. Должно пройти успешно.")
    void getBookById_Test() {
        //given
        long bookId = 1L;

        Person person = new Person();
        person.setId(1L);

        Book gotBook = new Book();
        gotBook.setId(bookId);
        gotBook.setUserId(person.getId());
        gotBook.setAuthor("test author");
        gotBook.setTitle("test title");
        gotBook.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(gotBook.getId());
        result.setUserId(gotBook.getUserId());
        result.setAuthor(gotBook.getAuthor());
        result.setTitle(gotBook.getTitle());
        result.setPageCount(gotBook.getPageCount());

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(gotBook));
        when(bookMapper.bookToBookDto(gotBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.getBookById(bookId);
        assertEquals(1000L, bookDtoResult.getPageCount());
        assertEquals(1L, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Получение всех книг. Должно пройти успешно.")
    void getAllBooks_Test() {

        //given
        Book book1 = new Book();
        book1.setId(1L);
        book1.setUserId(1L);
        book1.setAuthor("test author 1");
        book1.setTitle("test title 1");
        book1.setPageCount(1000);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setUserId(1L);
        book2.setAuthor("test author 2");
        book2.setTitle("test title 2");
        book2.setPageCount(1000);

        BookDto bookDto1 = new BookDto();
        bookDto1.setId(book1.getId());
        bookDto1.setUserId(book1.getUserId());
        bookDto1.setAuthor(book1.getAuthor());
        bookDto1.setTitle(book1.getTitle());
        bookDto1.setPageCount(book1.getPageCount());

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(book2.getId());
        bookDto2.setUserId(book2.getUserId());
        bookDto2.setAuthor(book2.getAuthor());
        bookDto2.setTitle(book2.getTitle());
        bookDto2.setPageCount(book2.getPageCount());

        List<Book> bookList = List.of(book1, book2);

        //when
        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.bookToBookDto(book1)).thenReturn(bookDto1);
        when(bookMapper.bookToBookDto(book2)).thenReturn(bookDto2);

        //then
        List<BookDto> allBooks = bookService.getAll();
        assertEquals(2, allBooks.size());
        assertEquals(bookDto1, allBooks.get(0));
        assertEquals(bookDto2, allBooks.get(1));

    }

    @Test
    @DisplayName("Удаление книги по id. Должно пройти успешно.")
    void deleteBook_Test() {
        //given

        long bookId = 1L;

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //then
        bookService.deleteBookById(bookId);
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    @DisplayName("Удаление книг по id пользователя. Должно пройти успешно.")
    void deleteBookByUserId_Test() {
        //given

        long personId = 1L;
        List<Book> listBook = new ArrayList<>();

        //when
        when(bookRepository.findBooksByUserId(personId)).thenReturn(listBook);

        //then
        bookService.deleteBookByUserId(personId);
        List<Long> list = bookService.getBookIdByUserId(personId);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Получение id книг по id пользователя. Должно пройти успешно.")
    void getBookIdByUserId_Test() {
        //given

        long personId = 1L;

        Book book1 = new Book();
        book1.setId(1L);
        book1.setUserId(1L);
        book1.setAuthor("test author 1");
        book1.setTitle("test title 1");
        book1.setPageCount(1000);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setUserId(1L);
        book2.setAuthor("test author 2");
        book2.setTitle("test title 2");
        book2.setPageCount(1000);
        List<Book> bookList = List.of(book1, book2);

        //when
        when(bookRepository.findBooksByUserId(personId)).thenReturn(bookList);

        //then
        List<Long> bookIdByUserId = bookService.getBookIdByUserId(personId);
        assertEquals(2, bookIdByUserId.size());
        assertEquals(1L, bookIdByUserId.get(0));
    }

    // update
    // get
    // get all
    // delete

    // * failed
}
