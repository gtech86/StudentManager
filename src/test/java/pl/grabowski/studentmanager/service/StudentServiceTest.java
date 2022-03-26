package pl.grabowski.studentmanager.service;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.repository.student.StudentRepository;
import pl.grabowski.studentmanager.service.course.CourseService;
import pl.grabowski.studentmanager.service.student.StudentService;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class StudentServiceTest {
    private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
    private final CourseService courseService = Mockito.mock(CourseService.class);

    private final StudentService studentService = new StudentService(studentRepository, courseService);

    @Test
    void should_create_student(){
        //given
        Student student = new Student(1L,"Paweł","Grabowski", "pawel@gmail.com", 1234, Date.valueOf(LocalDate.now()));

        //when
        studentService.addNewStudent(student);

        //then
        Mockito.verify(studentRepository).save(student);
    }

    @Test
    void should_return_empty_list_when_no_students(){
        //given
        given(studentRepository.findAll()).willReturn(List.of());

        //when
        List<Student> result = studentService.getAllStudents();

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void should_return_student_list_when_students_exist(){
        //given
        Student student = new Student(1L,"Paweł","Grabowski", "pawel@gmail.com", 1234, Date.valueOf(LocalDate.now()));
        Student expectedStudent = new Student(1L,"Paweł","Grabowski", "pawel@gmail.com", 1234, Date.valueOf(LocalDate.now()));
        given(studentRepository.findAll()).willReturn(List.of(student));

        //when
        List<Student> result = studentService.getAllStudents();

        //then
        assertThat(result).isEqualTo(List.of(expectedStudent));
    }

    @Test
    void should_return_student_by_id(){
        //given
        Student student = new Student(1L,"Paweł","Grabowski", "pawel@gmail.com", 1234, Date.valueOf(LocalDate.now()));
        given(studentRepository.findById(1L)).willReturn(Optional.of(student));

        //when
        Optional<Student> result = studentService.getStudentById(1L);

        //then
        assertThat(result.map(Student::getId)).hasValue(1L);
    }

    @Test
    void should_delete_student_by_id(){
        //when
        studentService.deleteStudentById(1L);

        //then
        Mockito.verify(studentRepository).deleteById(1L);
    }

}