package pl.grabowski.studentmanager.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.grabowski.studentmanager.model.StudentAddress;
import pl.grabowski.studentmanager.repository.StudentAddressRepository;


import java.util.Optional;


@RequiredArgsConstructor
@Getter
@Setter

@Service
public class StudentAddressService {

    private final StudentAddressRepository studentAddressRepository;

    public void addAddress(StudentAddress studentAddress){
        studentAddressRepository.save(studentAddress);
    }

    public Optional<StudentAddress> getAddressById(Long id){
        return studentAddressRepository.findById(id);
    }

    public void deleteStudentAddressById(Long id) {
        studentAddressRepository.deleteById(id);
    }


}
