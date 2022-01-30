package pl.grabowski.studentmanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.grabowski.studentmanager.model.Student;
import pl.grabowski.studentmanager.repository.StudentRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentIntegrationTest {

    @LocalServerPort
    private int port;

    private final List<Student> testStudents = new ArrayList<>();

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String StudentResourceUrl() {
        return "http://localhost:"+port+"/students";
    }

    /*private void initData(){
        testStudents.add(new Student(1L,"John","Bond", mail, 4567, Date.valueOf(LocalDate.now()),null));
        testStudents.add(new Student(2L, "Paweł","Grabowski", mail, 1234, Date.valueOf(LocalDate.now()),null));
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
                new Student(1L,"John","Bond", mail, 4567, Date.valueOf(LocalDate.now()),null),
                new Student(2L, "Paweł","Grabowski", mail, 1234, Date.valueOf(LocalDate.now()),null));
    }

    @Test
    void ShouldAbleToAddNewStudent(){
        //when
        var result = restTemplate.postForEntity(StudentResourceUrl(),
                new Student(1L,"John","Bond", mail, 4567, Date.valueOf(LocalDate.now()),null),
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
                new Student(2L, "Paweł","Grabowski", mail, 1234, Date.valueOf(LocalDate.now()),null)
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
                new Student(2L, "Poprawiony","Grabowski", mail, 1234, Date.valueOf(LocalDate.now()),null),
                Student.class
                );
        // when
        var result = restTemplate.getForEntity("http://localhost:"+port+"/students/2", Student.class);

        // then
        assertThat(updateResult.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        assertThat(result.getBody()).isEqualTo(
                new Student(2L, "Poprawiony","Grabowski", mail, 1234, Date.valueOf(LocalDate.now()),null)
        );

    }
*/

}
