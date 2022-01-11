package pl.grabowski.studentmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.grabowski.studentmanager.model.StudentAddress;

@Repository
public interface StudentAddressRepository extends CrudRepository<StudentAddress, Long> {
}
