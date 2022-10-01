package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new BookNotFoundException(("book with id " + bookDto.getId() + " not found")));
        log.info("Get book: {}", book);
        Book updateBook = bookRepository.save(book);
        log.info("Updated user: {}", updateBook);
        return bookMapper.bookToBookDto(updateBook);
        // реализовать недстающие методы
    }

    @Override
    public BookDto getBookById(Long id) {
        log.info("Get book by id: {}", id);
        // реализовать недстающие методы
        return bookRepository.findById(id)
                .map(bookMapper::bookToBookDto)
                .orElseThrow(() -> new BookNotFoundException(("book with id " + id + " not found")));
    }

    @Override
    public void deleteBookById(Long id) {
        log.info("Delete book by id: {}", id);
        bookRepository.deleteById(id);
        // реализовать недстающие методы
    }

    @Override
    public List<Long> getBookIdByUserId(Long userId) {
        log.info("Get book by user id: {}", userId);
        return bookRepository.findBooksByUserId(userId)
                .stream()
                .map(Book::getId)
                .toList();
    }

    @Override
    public void deleteBookByUserId(Long userId) {
        log.info("Deleted books by user id: {}", userId);
        bookRepository.deleteBooksByUserId(userId);
    }
}
