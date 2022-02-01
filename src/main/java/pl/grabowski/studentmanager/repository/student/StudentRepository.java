package pl.grabowski.studentmanager.repository.student;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.grabowski.studentmanager.model.student.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

}
