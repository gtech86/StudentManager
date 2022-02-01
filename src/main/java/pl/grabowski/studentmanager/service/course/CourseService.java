package pl.grabowski.studentmanager.service.course;

import org.springframework.stereotype.Service;
import pl.grabowski.studentmanager.model.course.Course;
import pl.grabowski.studentmanager.repository.course.CourseRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void addNewCourse(Course course){
        courseRepository.save(course);
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

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }


}
