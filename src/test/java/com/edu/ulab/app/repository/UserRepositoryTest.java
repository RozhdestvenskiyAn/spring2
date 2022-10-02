package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.PersonNotFoundException;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
@Rollback
@Sql({"classpath:sql/1_clear_schema.sql",
        "classpath:sql/2_insert_person_data.sql",
        "classpath:sql/3_insert_book_data.sql"
})
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    @Test
    @DisplayName("Получить юзера по Id. Число select должно равняться 1")
    void getPersonById_thenAssertDmlCount() {
        //Given
        long id = 1001l;

        //When
        Person result = userRepository.findById(id).get();

        //Then
        assertThat(result.getTitle()).isEqualTo("reader");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Получить  всех юзеров. Число select должно равняться 1")
    void getAllPerson_thenAssertDmlCount() {
        //Given
        List<Person> personList = new ArrayList<>();
        //When
        for (Person person : userRepository.findAll()) {
                personList.add(person);
        }
        //Then
        assertThat(personList.size()).isEqualTo(1);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    @Test
    @DisplayName("Обновить юзера. Число select должно равняться 1")
    void updatePerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setId(1001L);
        person.setAge(10);
        person.setTitle("reader");
        person.setFullName("Test Test");
        //When
        Person savedPerson = userRepository.save(person);

        //Then
        assertThat(savedPerson.getAge()).isEqualTo(10);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Удалить юзера по Id. Число select должно равняться 1")
    void deletePersonById_thenAssertDmlCount() {
        //Given
        long id = 1001l;

        //When
        userRepository.deleteById(id);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
        assertThatThrownBy(() -> {
            userRepository.findById(id).orElseThrow(() -> new PersonNotFoundException("user with id " + id + " not found"));
        }).isInstanceOf(PersonNotFoundException.class);
    }
    // update
    // get
    // get all
    // delete

    // * failed
}
