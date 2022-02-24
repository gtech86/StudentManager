package pl.grabowski.studentmanager.service.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.grabowski.studentmanager.model.course.Course;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.repository.student.StudentRepository;
import pl.grabowski.studentmanager.service.course.CourseService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseService courseService;

    @Autowired
    public StudentService(StudentRepository studentRepository,@Lazy CourseService courseService) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
    }


    public void addNewStudent(Student student){
        studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return StreamSupport
                .stream(studentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Student> updateStudent(Long id, Student student){
        var studentToUpdate = getStudentById(id);
        if(studentToUpdate.isEmpty()) return Optional.empty();
        studentRepository.save(student);
        return Optional.of(student);

    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    public Optional<Student> assignCourse(Long studentId, Long courseId){
        var course = courseService.getCourseById(courseId);
        var student = getStudentById(studentId);
        student.get().addCourse(course.get());
        studentRepository.save(student.get());
        return student;
    }

    public List<Course> getCourseByStudentId(Long studentId){
        var student = getStudentById(studentId);
        if(student.isPresent()){
            return new ArrayList<>(student.get().getCourses());
        }
        return new ArrayList<>();
    }



}
