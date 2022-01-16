package pl.grabowski.studentmanager.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.grabowski.studentmanager.model.Student;
import pl.grabowski.studentmanager.repository.StudentRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;


class StudentServiceTest {
    private StudentRepository studentRepository = Mockito.mock(StudentRepository.class);

    private StudentService studentService = new StudentService(studentRepository);

    @Test
    void ShouldCreateStudent(){
        //given
        Student student = new Student(1L,"Paweł","Grabowski",1234, Date.valueOf(LocalDate.now()),null);

        //when
        studentService.addNewStudent(student);

        //then
        Mockito.verify(studentRepository).save(student);
    }

    @Test
    void ShouldReturnEmptyListWhenNoStudents(){
        //given
        given(studentRepository.findAll()).willReturn(List.of());
        //when
        List<Student> result = studentService.getAllStudents();
        //then
        assertThat(result).isEmpty();
    }

    @Test
    void ShouldReturnStudentListWhenStudentsExist(){
        //given
        Student student = new Student(1L,"Paweł","Grabowski",1234, Date.valueOf(LocalDate.now()),null);
        Student expectedStudent = new Student(1L,"Paweł","Grabowski",1234, Date.valueOf(LocalDate.now()),null);
        given(studentRepository.findAll()).willReturn(List.of(student));

        //when
        List<Student> result = studentService.getAllStudents();

        //then
        assertThat(result).isEqualTo(List.of(expectedStudent));
    }

    @Test
    void ShouldReturnStudentById(){
        //given
        Student student = new Student(1L,"Paweł","Grabowski",1234, Date.valueOf(LocalDate.now()),null);
        given(studentRepository.findById(1L)).willReturn(Optional.of(student));

        //when
        Optional<Student> result = studentService.getStudentById(1L);

        //then
        assertThat(result.map(Student::getId)).hasValue(1L);
    }

    @Test
    void ShouldDeleteStudentById(){
        //given

        //when
        studentService.deleteStudentById(1L);

        //then
        Mockito.verify(studentRepository).deleteById(1L);
    }

}