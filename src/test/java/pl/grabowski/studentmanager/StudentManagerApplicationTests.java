package pl.grabowski.studentmanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.repository.student.StudentRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class StudentIntegrationTest {

    @LocalServerPort
    private int port;

    private final List<Student> testStudents = new ArrayList<>();

    @Autowired
    MockMvc mvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String StudentResourceUrl() {
        return "http://localhost:"+port+"/students";
    }


    private void initData(){
        testStudents.add(new Student(1L,"John","Bond", "john@bond.pl", 4567, Date.valueOf(LocalDate.now())));
        testStudents.add(new Student(2L, "Paweł","Grabowski","pawel@grabiski.pl", 1234, Date.valueOf(LocalDate.now())));
        studentRepository.saveAll(testStudents);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void ShouldReturnAllStudent(){
        //given
        initData();
        // when
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students", Student[].class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();

        assertThat(result.getBody()).containsExactly(
                new Student(1L,"John","Bond", "john@bond.pl", 4567, Date.valueOf(LocalDate.now())),
                new Student(2L, "Paweł","Grabowski","pawel@grabiski.pl", 1234, Date.valueOf(LocalDate.now()))
        );
    }

    @Test
    void ShouldAbleToAddNewStudent(){
        //when
        var result = restTemplate.postForEntity(StudentResourceUrl(),
                new Student(1L,"John","Bond", "john@bond.pl", 4567, Date.valueOf(LocalDate.now())),
                Student.class);

        //then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        assertThat(result.getBody().getId()).isEqualTo(1L);
    }

    @Test
    void ShouldAbleToFindStudentById(){
        //given
        initData();
        // when
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students/2", Student.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        assertThat(result.getBody()).isEqualTo(
                new Student(2L, "Paweł","Grabowski","pawel@grabiski.pl", 1234, Date.valueOf(LocalDate.now()))
        );
    }

    @Test
    void ShouldAbleToRemoveStudent(){
        //given
        initData();
        // when
        restTemplate.delete("http://localhost:"+port+"/students/2", Student.class);
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students/2", Student.class);

        // then
        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void ShouldAbleToUpdateStudent(){
        //given
        initData();
        var updateResult = restTemplate.postForEntity("http://localhost:"+port+"/students",
                new Student(2L, "Poprawiony","Grabowski","pawel@grabowski.pl", 1234, Date.valueOf(LocalDate.now())),
                Student.class
                );
        // when
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students/2", Student.class);

        // then
        assertThat(updateResult.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        assertThat(result.getBody()).isEqualTo(
                new Student(2L, "Poprawiony","Grabowski","pawel@grabiski.pl", 1234, Date.valueOf(LocalDate.now()))
        );

    }

}
