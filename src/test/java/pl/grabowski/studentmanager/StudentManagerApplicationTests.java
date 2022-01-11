package pl.grabowski.studentmanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import pl.grabowski.studentmanager.model.Student;
import pl.grabowski.studentmanager.repository.StudentRepository;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void ShouldReturnAllStudent(){
        // when
        var result = restTemplate.getForEntity("http://localhost:" + port + "/students", Student[].class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();

        assertThat(result.getBody()).containsExactly(new Student(1L,"Paweł","Grabowski",1234, Date.valueOf(LocalDate.now()),null));
    }

}
