package pl.grabowski.studentmanager.service;

import org.springframework.stereotype.Service;
import pl.grabowski.studentmanager.model.Student;
import pl.grabowski.studentmanager.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void addNewStudent(Student student){
        studentRepository.save(student);
    }


    public List<Student> getAllStudents() {
        return StreamSupport
                .stream(studentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }


}
