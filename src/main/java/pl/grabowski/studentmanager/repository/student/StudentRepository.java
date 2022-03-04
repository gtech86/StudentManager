package pl.grabowski.studentmanager.repository.student;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.grabowski.studentmanager.model.student.Student;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    @Override
    @Query("SELECT student FROM Student student")
    Iterable<Student> findAll();

    @Override
    @Query("SELECT student FROM Student student where student.id = :aLong")
    Optional<Student> findById(Long aLong);

    @Override
    @Modifying
    @Query("DELETE FROM Student s WHERE s.id = :aLong")
    void deleteById(@Param("aLong") Long aLong);

}
