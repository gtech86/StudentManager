package pl.grabowski.studentmanager.controller;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.studentmanager.model.Student;
import pl.grabowski.studentmanager.service.StudentService;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path= "/students")
public class StudentController {

    private final StudentService studentService;
    private final Validator validator;
    private final ModelMapper modelMapper = new ModelMapper();

    public StudentController(StudentService studentService, Validator validator) {
        this.studentService = studentService;
        this.validator = validator;
    }

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudent(){
        List<StudentResponse> studentsResponse = studentService.getAllStudents()
                .stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getMail(),
                        student.getIndexNumber(),
                        student.getBirthDay()
                        )
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(studentsResponse, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable(required = true) Long id){

        var student = studentService.getStudentById(id);
                if(student.isPresent()){
                    StudentResponse studentResponse = new StudentResponse(
                            student.get().getId(),
                            student.get().getFirstName(),
                            student.get().getLastName(),
                            student.get().getMail(),
                            student.get().getIndexNumber(),
                            student.get().getBirthDay()
                    );
                    return new ResponseEntity<>(studentResponse, HttpStatus.OK);
                }
                else return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> addNewStudent(@Valid @RequestBody StudentCreateRequest studentCreateRequest){
        var student = new Student(
                studentCreateRequest.getFirstName(),
                studentCreateRequest.getLastName(),
                studentCreateRequest.getMail(),
                studentCreateRequest.getIndexNumber(),
                studentCreateRequest.getBirthDay()
                );
        studentService.addNewStudent(student);
        try{
            studentService.addNewStudent(student);
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping(path= "/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable(required = true) Long id, @RequestBody StudentUpdateRequest studentUpdateRequest){
        /*Student student = new Student(
                id,
                studentUpdateRequest.getFirstName().orElse(null),
                studentUpdateRequest.getLastName().orElse(null),
                studentUpdateRequest.getMail().orElse(null),
                studentUpdateRequest.getIndexNumber().orElse(null),
                studentUpdateRequest.birthDay.orElse(null)
        );*/

        if(studentService.getStudentById(id).isPresent()){
            Student studentToUpdate = modelMapper.map(studentUpdateRequest, Student.class);
            var student = studentService.updateStudent(id, studentToUpdate);
            return new ResponseEntity<>(student.get(), HttpStatus.CREATED);

        }
        return ResponseEntity.notFound().build();
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
