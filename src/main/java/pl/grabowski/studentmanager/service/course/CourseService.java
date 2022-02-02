package pl.grabowski.studentmanager.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.grabowski.studentmanager.model.course.Course;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.repository.course.CourseRepository;
import pl.grabowski.studentmanager.service.student.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final StudentService studentService;

    public CourseService(CourseRepository courseRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    public Optional<Course> addNewCourse(Course course){
        return Optional.of(courseRepository.save(course));
    }

    public List<Course> getAllCourses() {
        return StreamSupport
                .stream(courseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Course> updateCourse(Long id, Course course){
        var courseToUpdate = getCourseById(id);
        if(courseToUpdate.isEmpty()) return Optional.empty();
        return Optional.of(courseRepository.save(course));

    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }



    public List<Student> getStudentByCourseId(Long courseId){
        var course = getCourseById(courseId);
        if(course.isPresent()){
            return new ArrayList<>(course.get().getStudents());
        }
        return new ArrayList<>();
    }

    public Optional<Course> addStudent(Long courseId, Long studentId){
        var course = getCourseById(courseId);
        var student = studentService.getStudentById(studentId);
        course.get().addStudent(student.get());
        courseRepository.save(course.get());
        return course;
    }
}
