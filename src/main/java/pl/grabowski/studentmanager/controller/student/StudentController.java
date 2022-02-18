package pl.grabowski.studentmanager.controller.student;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.service.course.CourseService;
import pl.grabowski.studentmanager.service.student.StudentService;
import pl.grabowski.studentmanager.utils.StudentMapper;

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
    private final CourseService courseService;

    @Autowired
    StudentMapper mapper;

    public StudentController(StudentService studentService, Validator validator, CourseService courseService) {
        this.studentService = studentService;
        this.validator = validator;
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('student:read')")
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
    @PreAuthorize("hasAuthority('student:read')")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable(required = true) Long id){
        if(id<1) return ResponseEntity.badRequest().build();
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

    @GetMapping(path="/{studentId}/course")
    @PreAuthorize("hasAuthority('course:read')")
    public ResponseEntity<List<StudentCourseResponse>> getCourseByStudentId(@PathVariable(required = true) Long studentId){
        List<StudentCourseResponse> studentsCourseResponse = studentService.getCourseByStudentId(studentId)
                .stream()
                .map(course -> new StudentCourseResponse(
                        studentId,
                        course.getId(),
                        course.getName(),
                        course.getDescription()
                        )
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(studentsCourseResponse, HttpStatus.OK);
    }

    @PostMapping(path="/{studentId}/course/{courseId}")
    @PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<String> assignStudentToCourse(@PathVariable(required = true) Long studentId, @PathVariable(required = true) Long courseId ){
        var course = courseService.getCourseById(courseId);
        var student = studentService.getStudentById(studentId);
        if(student.isPresent() && course.isPresent()) {
            studentService.assignCourse(studentId, courseId);
            courseService.addStudent(courseId,studentId);
            return new ResponseEntity<>("Student added to course", HttpStatus.OK);
        }
        else return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<Student> addNewStudent(@Valid @RequestBody StudentCreateRequest studentCreateRequest){
        Student student = new Student(
                studentCreateRequest.getFirstName(),
                studentCreateRequest.getLastName(),
                studentCreateRequest.getMail(),
                studentCreateRequest.getIndexNumber(),
                studentCreateRequest.getBirthDay()
        );
        try{
            studentService.addNewStudent(student);
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path= "/{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public ResponseEntity<Student> updateStudent(@PathVariable(required = true) Long id, @RequestBody StudentUpdateRequest studentUpdateRequest){
       //modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
       if(studentService.getStudentById(id).isPresent()){
            var studentToUpdate = studentService.getStudentById(id);
            mapper.updateStudentFromDto(studentUpdateRequest, studentToUpdate.get());
            var updatedStudent = studentService.updateStudent(id, studentToUpdate.get());
            return new ResponseEntity<>(updatedStudent.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path="/{id}")
    @PreAuthorize("hasAuthority('student:write')")
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
