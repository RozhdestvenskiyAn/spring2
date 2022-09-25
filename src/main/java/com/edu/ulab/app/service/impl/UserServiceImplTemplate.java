package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public UserServiceImplTemplate(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        final String UPDATE_PERSON = "UPDATE PERSON SET full_name =?, title = ?, age=? WHERE id = ?";
        if (jdbcTemplate.update(UPDATE_PERSON, userDto.getFullName(), userDto.getTitle(), userDto.getAge(), userDto.getId()) !=0){
            log.info("Success update user: {}", userDto);
        } else{
            throw new PersonNotFoundException("user with id " + userDto.getId() + " not found");
        }
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        final String GET_PERSON_BY_ID = "SELECT id, full_name, title, age FROM Person WHERE id=?";
        Person person = jdbcTemplate.query(GET_PERSON_BY_ID, new BeanPropertyRowMapper<>(Person.class), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new PersonNotFoundException("user with id " + id + " not found"));
        log.info("Get user: {}", person);
        // реализовать недстающие методы
        return userMapper.personToUserDto(person);
    }

    @Override
    public void deleteUserById(Long id) {
        final String DELETE_PERSON_BY_ID = "DELETE FROM Person WHERE id=?";
        log.info("Deleted user with id: {}", id);
        jdbcTemplate.update(DELETE_PERSON_BY_ID, id);
    }
}
