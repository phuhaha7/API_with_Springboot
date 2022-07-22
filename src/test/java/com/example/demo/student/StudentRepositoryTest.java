package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindStudentByEmail() {
        //given
        String email = "Jamila@gmail.com";
        Student student = new Student(
                "Jamila",
                email,
                LocalDate.of(2000, Month.JANUARY, 30)
        );
        underTest.save(student);
        //when
        Optional<Student> studentExist = underTest.findStudentByEmail(email);
        boolean expected = false;
        if (studentExist.isPresent()) expected = true;
        //then
        assertThat(expected).isTrue();
    }
    @Test
    void itShouldCHeckIfStudentEmailDoesNotExist() {
        //given
        String email = "Jamila@gmail.com";
        //when
        Optional<Student> studentExist = underTest.findStudentByEmail(email);
        boolean expected = false;
        if (studentExist.isPresent()) expected = true;
        //then
        assertThat(expected).isFalse();
    }
}
