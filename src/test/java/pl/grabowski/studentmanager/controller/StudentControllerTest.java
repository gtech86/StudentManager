package pl.grabowski.studentmanager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.grabowski.studentmanager.model.Student;
import pl.grabowski.studentmanager.repository.StudentRepository;
import pl.grabowski.studentmanager.service.StudentService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc*/
@ExtendWith(MockitoExtension.class)
//@ActiveProfiles("test")
class StudentControllerTest {

    MockMvc mvc;
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }


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
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].firstName", is("Paweu") ))
                .andExpect(jsonPath("$[0].lastName", is("Grabowski") ))
                .andExpect(jsonPath("$[1].firstName", is("Jan") ))
                .andExpect(jsonPath("$[1].lastName", is("Bond") ))
                .andReturn();
    }

    @Test
    void shouldCreateStudent() throws Exception{
        mvc.perform(post("/students")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{"
                            +"\"firstName\": \"firstName\","
                            +"\"lastName\": \"lastName\","
                            + "\"mail\": null,"
                            + "\"indexNumber:\" 23\","
                            + "\"birthDay\": \"2018-10-10T23:00:00.000+00:00\""
                            + "}")
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("Paweu") ))
                .andExpect(jsonPath("$.lastName", is("Grabowski") )).andReturn();

    }
}