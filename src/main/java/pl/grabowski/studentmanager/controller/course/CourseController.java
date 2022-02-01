package pl.grabowski.studentmanager.controller.course;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.studentmanager.model.course.Course;
import pl.grabowski.studentmanager.service.course.CourseService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/course")
public class CourseController {
    private final ModelMapper modelMapper = new ModelMapper();
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses(){
        List<CourseResponse> courseResponse = courseService.getAllCourses()
                .stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getName(),
                        course.getDescription()
                        )
                )
                .collect(Collectors.toList());

        return new ResponseEntity<>(courseResponse, HttpStatus.OK);
    }

    @GetMapping(path="/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable(required = true) Long id){

        var course = courseService.getCourseById(id);
        if(course.isPresent()){
            CourseResponse courseResponse = new CourseResponse(
                    course.get().getId(),
                    course.get().getName(),
                    course.get().getName());
            return new ResponseEntity<>(courseResponse, HttpStatus.OK);
        }
        else return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Course> addNewCourse(@Valid @RequestBody CourseCreateRequest courseCreateRequest){
        Course course = new Course(
                courseCreateRequest.getName(),
                courseCreateRequest.getDescription()
        );
       
        try{
            courseService.addNewCourse(course);
            return new ResponseEntity<>(course, HttpStatus.CREATED);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping(path= "/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable(required = true) Long id, @RequestBody CourseUpdateRequest courseUpdateRequest){
        /*course course = new course(
                id,
                courseUpdateRequest.getFirstName().orElse(null),
                courseUpdateRequest.getLastName().orElse(null),
                courseUpdateRequest.getMail().orElse(null),
                courseUpdateRequest.getIndexNumber().orElse(null),
                courseUpdateRequest.birthDay.orElse(null)
        );*/

        if(courseService.getCourseById(id).isPresent()){
            Course courseToUpdate = modelMapper.map(courseUpdateRequest, Course.class);
            var course = courseService.updateCourse(id, courseToUpdate);
            return new ResponseEntity<>(course.get(), HttpStatus.CREATED);

        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteCourseById(@PathVariable(required = true) Long id){
        try{
            courseService.deleteCourseById(id);
            return new ResponseEntity<>("course deleted", HttpStatus.OK);
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
