package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;
    private final BookMapper bookMapper;


    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate, BookMapper bookMapper) {
        this.jdbcTemplate = jdbcTemplate;

        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        final String UPDATE_BOOK = "UPDATE Book SET user_id = ?, title = ?, author = ?, page_count=? WHERE id =?";
        jdbcTemplate.update(UPDATE_BOOK, bookDto.getUserId(), bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPageCount(), bookDto.getId());
        log.info("Update book: {}", bookDto);
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        final String GET_BOOK_BY_ID = "SELECT * FROM Book WHERE id=?";
        Book book = jdbcTemplate.query(GET_BOOK_BY_ID, new BeanPropertyRowMapper<>(Book.class), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new BookNotFoundException("user with id " + id + " not found"));
        log.info("Get book: {}", book);
        // реализовать недстающие методы
        return bookMapper.bookToBookDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        final String DELETE_BOOK_BY_ID = "DELETE FROM Book WHERE id=?";
        log.info("Deleted book with id: {}", id);
        jdbcTemplate.update(DELETE_BOOK_BY_ID, id);
        // реализовать недстающие методы
    }

    @Override
    public List<Long> getBookIdByUserId(Long userId) {
        final String GET_BOOK_BY_USERID = "SELECT id, user_id, title, author, page_count FROM Book WHERE user_id =?";
        return jdbcTemplate.query(GET_BOOK_BY_USERID, new BeanPropertyRowMapper<>(Book.class), userId)
                .stream()
                .map(Book::getId)
                .toList();
    }

    @Override
    public void deleteBookByUserId(Long userId) {
        final String DELETE_BOOKS_BY_USERID = "DELETE FROM Book WHERE user_id = ?";
        jdbcTemplate.update(DELETE_BOOKS_BY_USERID, userId);
        log.info("Delete books by user id: {}", userId);
    }
}
