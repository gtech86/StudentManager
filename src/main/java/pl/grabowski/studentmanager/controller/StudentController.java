package pl.grabowski.studentmanager.controller;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.studentmanager.model.Student;
import pl.grabowski.studentmanager.service.StudentService;

import java.util.List;


@RestController

@RequestMapping(path= "/students")

public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudent(){
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    @GetMapping(path="/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable(required = true) Long id){
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> addNewStudent(@RequestBody Student student){
        studentService.addNewStudent(student);

        try{
            studentService.addNewStudent(student);
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable(required = true) Long id){
        try{
            studentService.deleteStudentById(id);
            return new ResponseEntity<>("Student deleted", HttpStatus.OK);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }


}
