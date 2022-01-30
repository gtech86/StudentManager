package pl.grabowski.studentmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.grabowski.studentmanager.model.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

}
