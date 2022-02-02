package pl.grabowski.studentmanager.controller.course;

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
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private StudentService studentService;

    @Test
    void shouldAddNewCourse() throws Exception {
        //given
        CourseCreateRequest courseCreateRequest = new CourseCreateRequest("Physics", "Desc");

        //then
        mvc
                .perform(post("/course")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\": \"Physics\",\"description\": \"Desc\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Physics") ))
                .andExpect(jsonPath("$.description", is("Desc") ))
                .andReturn();
    }

    @Test
    void shouldReturnAllCourses() throws Exception{
        //given
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1L,"Physic", "Desc"));
        courses.add(new Course(2L,"Math", "MathDesc"));
        given(courseService.getAllCourses()).willReturn(courses);

        //then
        mvc.perform(get("/course")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Physic") ))
                .andExpect(jsonPath("$[0].description", is("Desc") ))
                .andExpect(jsonPath("$[1].name", is("Math") ))
                .andExpect(jsonPath("$[1].description", is("MathDesc") ))
                .andReturn();
    }

}