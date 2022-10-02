package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFullName("test name");
        userDto.setTitle("test title");
        userDto.setAge(35);

        Person gotUser = new Person();
        gotUser.setId(userDto.getId());
        gotUser.setFullName("name");
        gotUser.setTitle("title");
        gotUser.setAge(30);

        Person updateUser = new Person();
        updateUser.setId(gotUser.getId());
        updateUser.setFullName(userDto.getFullName());
        updateUser.setTitle(userDto.getTitle());
        updateUser.setAge(userDto.getAge());

        UserDto result = new UserDto();
        result.setId(updateUser.getId());
        result.setFullName(updateUser.getFullName());
        result.setTitle(updateUser.getTitle());
        result.setAge(updateUser.getAge());


        //when
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(gotUser));
        when(userRepository.save(gotUser)).thenReturn(updateUser);
        when(userMapper.personToUserDto(updateUser)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.updateUser(userDto);
        assertEquals(35, userDtoResult.getAge());
    }

    @Test
    @DisplayName("Получение пользователя по id. Должно пройти успешно.")
    void getUserById_Test() {
        //given
        long userId = 1L;

        Person gotUser = new Person();
        gotUser.setId(userId);
        gotUser.setFullName("name");
        gotUser.setTitle("title");
        gotUser.setAge(30);

        UserDto result = new UserDto();
        result.setId(gotUser.getId());
        result.setFullName(gotUser.getFullName());
        result.setTitle(gotUser.getTitle());
        result.setAge(gotUser.getAge());

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(gotUser));
        when(userMapper.personToUserDto(gotUser)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.getUserById(userId);
        assertEquals(30, userDtoResult.getAge());
        assertEquals(1L, userDtoResult.getId());
    }

    @Test
    @DisplayName("Получение всех пользователей. Должно пройти успешно.")
    void getAllUsers_Test() {

        //given
        Person person1  = new Person();
        person1.setId(1L);
        person1.setFullName("test name1");
        person1.setAge(11);
        person1.setTitle("test title1");

        Person person2  = new Person();
        person2.setId(2L);
        person2.setFullName("test name2");
        person2.setAge(22);
        person2.setTitle("test title2");

        UserDto userDto1 = new UserDto();
        userDto1.setId(person1.getId());
        userDto1.setFullName(person1.getFullName());
        userDto1.setAge(person1.getAge());
        userDto1.setTitle(person1.getTitle());

        UserDto userDto2 = new UserDto();
        userDto2.setId(person2.getId());
        userDto2.setFullName(person2.getFullName());
        userDto2.setAge(person2.getAge());
        userDto2.setTitle(person2.getTitle());

        List<Person> personList = List.of(person1, person2);

        //when
        when(userRepository.findAll()).thenReturn(personList);
        when(userMapper.personToUserDto(person1)).thenReturn(userDto1);
        when(userMapper.personToUserDto(person2)).thenReturn(userDto2);

        //then
        List<UserDto> allUsers = userService.getAll();
        assertEquals(2, allUsers.size());
        assertEquals(userDto1, allUsers.get(0));
        assertEquals(userDto2, allUsers.get(1));
    }

    @Test
    @DisplayName("Удаление пользователя по id. Должно пройти успешно.")
    void deleteBook_Test() {
        //given

        long userId = 1L;

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //then
        userService.deleteUserById(userId);
        assertThrows(PersonNotFoundException.class, () -> userService.getUserById(userId));
    }

    // update
    // get
    // get all
    // delete

    // * failed
    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
