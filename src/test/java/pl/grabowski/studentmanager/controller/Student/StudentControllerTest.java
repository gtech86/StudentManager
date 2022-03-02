package pl.grabowski.studentmanager.controller.Student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.grabowski.studentmanager.controller.student.StudentController;
import pl.grabowski.studentmanager.controller.student.StudentCreateRequest;
import pl.grabowski.studentmanager.model.course.Course;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.service.course.CourseService;
import pl.grabowski.studentmanager.service.student.StudentService;
import pl.grabowski.studentmanager.utils.StudentMapper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

    private final String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJjb3Vyc2U6cmVhZCIsImNvdXJzZTp3cml0ZSIsInN0dWRlbnQ6cmVhZCIsInN0dWRlbnQ6d3JpdGUiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNjQ2ODEzNDYzfQ.cEXU4HAZKF7Q1_hhJ056gY9UveFrvVk7xTxdl6pkhEI";
    private List<Student> students = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;
    @MockBean
    StudentMapper mapper;
    @MockBean
    private StudentService studentService;
    @MockBean
    private CourseService courseService;

   // @BeforeTestMethod
    void initStudents(){
        students.add(new Student(1L,"Paweł","Grabowski", "pawel@gmail.com", 1234, Date.valueOf("1986-05-31")));
        students.add(new Student(2L,"Jan","Bond", "bond@gmail.com", 5678, Date.valueOf("1986-05-31")));
    }


    @Test
    void should_return_all_student() throws Exception {
        //given
        initStudents();
        given(studentService.getAllStudents()).willReturn(students);

        //then
        mvc
                .perform(get("/students")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].firstName", is("Paweł") ))
                .andExpect(jsonPath("$[0].lastName", is("Grabowski") ))
                .andExpect(jsonPath("$[0].mail", is("pawel@gmail.com") ))
                .andExpect(jsonPath("$[1].firstName", is("Jan") ))
                .andExpect(jsonPath("$[1].lastName", is("Bond") ))
                .andExpect(jsonPath("$[1].mail", is("bond@gmail.com") ));
    }

    @Test
    void should_add_new_student() throws Exception{
        //given
        StudentCreateRequest student = new StudentCreateRequest(
                "Paweł",
                "Grabowski",
                "pawel@gmail.com",
                123456,
                Date.valueOf(LocalDate.now())
        );
        //then
        mvc.perform(post("/students")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(student))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Paweł") ))
                .andExpect(jsonPath("$.lastName", is("Grabowski") ))
                .andExpect(jsonPath("$.mail", is("pawel@gmail.com") ));
    }

    @Test
    void should_return_information_about_incorrect_fields() throws Exception{
        //given
        StudentCreateRequest student = new StudentCreateRequest(
                "",
                "",
                "dsdsd.pl",
                null,
                Date.valueOf("2025-05-31")
        );
        //then
        mvc.perform(post("/students")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(student))
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.firstName", is("size must be between 2 and 15") ))
                .andExpect(jsonPath("$.lastName", is("size must be between 2 and 30") ))
                .andExpect(jsonPath("$.indexNumber", is("must not be null") ))
                .andExpect(jsonPath("$.mail", is("must be a well-formed email address") ))
                .andExpect(jsonPath("$.birthDay", is("must be a past date") ));
    }

    @Test
    void should_find_student_by_id() throws Exception{
        //given
        initStudents();
        given(studentService.getStudentById(1L)).willReturn(Optional.of(students.get(0)));

        //then
        mvc
                .perform(get("/students/1")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(1) ))
                .andExpect(jsonPath("$.firstName", is("Paweł") ))
                .andExpect(jsonPath("$.lastName", is("Grabowski") ))
                .andExpect(jsonPath("$.mail", is("pawel@gmail.com") ));
    }

    @Test
    void shouldUpdateStudent() throws Exception{
        //given
        initStudents();
        given(studentService.getStudentById(1L)).willReturn(java.util.Optional.of(students.get(1)));

        //then
        mvc.perform(patch("/students/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lastName\": \"Updated\"}")
                );

    }

    @Test
    void should_assign_student_to_course() throws Exception{
        //given
        initStudents();
        given(studentService.getStudentById(1L)).willReturn(java.util.Optional.of(students.get(1)));
        given(courseService.getCourseById(2L)).willReturn(java.util.Optional.of(new Course("Matematyka", "Dyskretna")));
        //then
        mvc.perform(post("/students/1/course/2")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Student added to course"));
    }

    @Test
    void should_return_response_as_json() throws Exception{
        //given
        initStudents();
        given(studentService.getAllStudents()).willReturn(students);

        //then
        mvc.perform(get("/students")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void should_return_not_found_if_student_id_dont_exist() throws Exception{
        //then
        mvc
                .perform(get("/students/-1")
                        .header("Authorization", token)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_able_to_remove_student() throws Exception{
        //then
        var result = mvc.perform(delete("/students/1")
                        .header("Authorization", token)
                )
                .andExpect(content().string("Student deleted"));
        verify(studentService).deleteStudentById(1L);
    }

    @Test
    void should_return_403_when_token_is_inValid() throws Exception{
        //then
        var result = mvc.perform(delete("/students/1")
                        .header("Authorization", "Bearer ")
                )
                .andExpect(status().is(403));
    }


}

