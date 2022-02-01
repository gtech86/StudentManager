package pl.grabowski.studentmanager.repository.course;

import org.springframework.data.repository.CrudRepository;
import pl.grabowski.studentmanager.model.course.Course;

public interface CourseRepository extends CrudRepository<Course, Long>{
}
