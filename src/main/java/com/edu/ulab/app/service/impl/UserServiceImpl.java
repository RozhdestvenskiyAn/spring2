package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        Person person = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new PersonNotFoundException("user with id " + userDto.getId() + " not found"));
        log.info("Get user: {}", person);
        Person updateUser = userRepository.save(person);
        log.info("Updated user: {}", updateUser);
        return userMapper.personToUserDto(updateUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        Person person = userRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("user with id " + id + " not found"));
        log.info("Get user: {}", person);
        // реализовать недстающие методы
        return userMapper.personToUserDto(person);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
        // реализовать недстающие методы
    }


    public List<UserDto> getAll() {
        List<UserDto> listUserDto = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            UserDto userDto = userMapper.personToUserDto(user);
            listUserDto.add(userDto);
        });
        log.info("Get all user: {}", listUserDto);
        // реализовать недстающие методы
        return listUserDto;
    }
}
