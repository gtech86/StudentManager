package pl.grabowski.studentmanager.repository.course;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.grabowski.studentmanager.model.course.Course;
import pl.grabowski.studentmanager.model.student.Student;

import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long>{

    @Override
    @Query("SELECT course FROM Course course")
    Iterable<Course> findAll();

    @Override
    @Query("SELECT course FROM Course course where course.id = :aLong")
    Optional<Course> findById(Long aLong);

    @Override
    @Modifying
    @Query("DELETE FROM Course course WHERE course.id = :aLong")
    void deleteById(@Param("aLong") Long aLong);
}
