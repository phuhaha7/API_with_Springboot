package com.example.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetStudents() {
        // when
        underTest.getStudents();
        // then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // given
        Student student = new Student(
                "Jamila",
                "Jamila@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 30)
        );
        // when
        underTest.addNewStudent(student);
        // then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // given
        Student student = new Student(
                "Jamila",
                "Jamila@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 30)
        );
        given(studentRepository.findStudentByEmail(anyString())).willReturn(Optional.of(student));
        // when
        // then
        assertThatThrownBy(() -> underTest.addNewStudent(student)).isInstanceOf(IllegalStateException.class).hasMessageContaining("email taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void willThrowIdDoesNotExist() {
        Long studentId = 1L;
        assertThatThrownBy(() -> underTest.deleteStudent(studentId)).isInstanceOf(IllegalStateException.class).hasMessageContaining("student with id" + studentId + "does not exist");
        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    void itShouldDeleteStudent() {
        Long studentId = 1L;

        given(studentRepository.existsById(studentId)).willReturn(true);

        underTest.deleteStudent(studentId);
    }

    @Test
    void itShouldUpdateStudent() {
        Long studentId = 1L;
        String name = "Mariam";
        String email = "mariam@gmail.com";
        Student student = new Student();
        student.setId(1L);
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        underTest.updateStudent(studentId, name, email);
    }

}
