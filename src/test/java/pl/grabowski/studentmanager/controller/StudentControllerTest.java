package pl.grabowski.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.grabowski.studentmanager.controller.student.StudentController;
import pl.grabowski.studentmanager.controller.student.StudentCreateRequest;
import pl.grabowski.studentmanager.controller.student.StudentUpdateRequest;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.security.ApplicationUserPermissions;
import pl.grabowski.studentmanager.security.ApplicationUserRole;
import pl.grabowski.studentmanager.service.course.CourseService;
import pl.grabowski.studentmanager.service.student.StudentService;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();
    Student student = new Student(1L,"Paweł","Grabowski", "pawel@gmail.com", 1234, Date.valueOf(LocalDate.now()));

    @Autowired
    MockMvc mvc;

    @MockBean
    private StudentService studentService;
    @MockBean
    private CourseService courseService;


    @Test

    void shouldReturnListOfStudents() throws Exception {
        //given
        List<Student> students = new ArrayList<>();
                students.add(new Student(1L,"Paweł","Grabowski", "pawel@gmail.com", 1234, Date.valueOf(LocalDate.now())));
                students.add(new Student(2L,"Jan","Bond", "bond@gmail.com", 5678, Date.valueOf(LocalDate.now())));
        given(studentService.getAllStudents()).willReturn(students);

        //then
        mvc
                .perform(get("/students")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("admin").password("adminPass").roles("ADMIN")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].firstName", is("Paweł") ))
                .andExpect(jsonPath("$[0].lastName", is("Grabowski") ))
                .andExpect(jsonPath("$[1].firstName", is("Jan") ))
                .andExpect(jsonPath("$[1].lastName", is("Bond") ))
                .andReturn();
    }

    @Test
    void shouldCreateStudent() throws Exception{
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
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(student))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Paweł") ))
                .andExpect(jsonPath("$.lastName", is("Grabowski") )).andReturn();

    }

    @Test
    void shouldReturnStudentById() throws Exception{
        //given
        given(studentService.getStudentById(1L)).willReturn(java.util.Optional.of(student));

        //then
        mvc
                .perform(get("/students/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(1) ))
                .andExpect(jsonPath("$.firstName", is("Paweł") ))
                .andExpect(jsonPath("$.lastName", is("Grabowski") ))
                .andExpect(jsonPath("$.mail", is("pawel@gmail.com") ))
                .andReturn()
                .getResponse();
    }

    @Test/////NIE DZIALA
    void shouldUpdateStudent() throws Exception{
        //given
        given(studentService.getStudentById(1L)).willReturn(java.util.Optional.of(student));
        //then
        mvc.perform(patch("/students/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"firstName\": \"Marek\"}")
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", is("Paweł") ))
                .andExpect(jsonPath("$.lastName", is("Grabowski") )).andReturn();

    }
}