package pl.grabowski.studentmanager;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.grabowski.studentmanager.model.course.Course;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.repository.course.CourseRepository;
import pl.grabowski.studentmanager.repository.student.StudentRepository;
import java.io.IOException;
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

    private String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJjb3Vyc2U6cmVhZCIsImNvdXJzZTp3cml0ZSIsInN0dWRlbnQ6cmVhZCIsInN0dWRlbnQ6d3JpdGUiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo2MjA1OC9sb2dpbiIsImV4cCI6MTY0NjA4NDgxNX0.90GEjqpLVEOfu9DSlVY5YgBeZWT_tR_ZOl6nWKgajWw";

    @Autowired
    MockMvc mvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private void initStudentData(){
        testStudents.add(new Student(1L,"John","Bond", "john@bond.pl", 4567, Date.valueOf(LocalDate.now())));
        testStudents.add(new Student(2L, "Paweł","Grabowski","pawel@grabowski.pl", 1234, Date.valueOf(LocalDate.now())));
        studentRepository.saveAll(testStudents);
    }
    /*@Before
    public void generateToken(){
     restTemplate.postForEntity("http://localhost:" + port + "/login?username=admin&password=adminPass", this.token, String.class);
    }*/

    @Test
    void contextLoads() {
    }

    @Test
    void Should_return_access_token_when_login() {
        String accessToken = "";
        var result = restTemplate.postForEntity("http://localhost:" + port + "/login?username=admin&password=adminPass", accessToken, String.class);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$.access_token", String.class)).isNotEmpty();
    }

    @Test
    void Should_return_all_student() throws IOException {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initStudentData();
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.GET, requestHeaders, String.class);

        // then
        String jsonBody = result.getBody();
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$[0].firstName", String.class)).isEqualTo("John");
        assertThat(json.read("$[0].lastName", String.class)).isEqualTo("Bond");
        assertThat(json.read("$[0].indexNumber", Integer.class)).isEqualTo(4567);
        assertThat(json.read("$[1].firstName", String.class)).isEqualTo("Paweł");
        assertThat(json.read("$[1].lastName", String.class)).isEqualTo("Grabowski");
        assertThat(json.read("$[1].indexNumber", Integer.class)).isEqualTo(1234);

    }

    @Test
    void Should_return_response_as_json() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initStudentData();
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.GET, requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void Should_add_new_student() throws JSONException {
        //given
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);
        header.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("firstName", "James");
        body.put("lastName", "Bond");
        body.put("indexNumber", 1234);
        body.put("birthDay", Date.valueOf(LocalDate.now()).toString());

        HttpEntity<?> requestHeaders = new HttpEntity<>(body.toString(), header);

        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.POST,requestHeaders,
                String.class);

        //then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$.firstName", String.class)).isEqualTo("James");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("Bond");
        assertThat(json.read("$.indexNumber", Integer.class)).isEqualTo(1234);
    }

    @Test
    void Should_find_student_by_id() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initStudentData();
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/2", HttpMethod.GET, requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$.firstName", String.class)).isEqualTo("Paweł");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("Grabowski");
        assertThat(json.read("$.indexNumber", Integer.class)).isEqualTo(1234);

    }

    @Test
    void Should_assign_student_to_course() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initStudentData();
        courseRepository.save(new Course("Matematyka", "Matematyka dla studentów"));
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/2/course/1", HttpMethod.POST, requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        assertThat(result.getBody()).isEqualTo("Student added to course");
    }

    @Test
    void Should_return_not_found_if_student_id_dont_exist() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initStudentData();

        //when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/3", HttpMethod.GET, requestHeaders, String.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void Should_return_bad_request_when_id_less_than_1() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initStudentData();
        //when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/-3", HttpMethod.GET, requestHeaders, String.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void Should_able_to_remove_student(){
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initStudentData();
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/2", HttpMethod.DELETE,requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo("Student deleted");
    }
////////////////NIE DZIALA Request method 'PATCH' not supported/////////////
    @Test
    void Should_able_to_update_student() throws JSONException {
        //given
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);
        header.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("firstName", "CorrectedFirstName");
        body.put("lastName", "CorrectedLastName");

        HttpEntity<?> requestHeaders = new HttpEntity<>(body.toString(), header);
        initStudentData();
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/2", HttpMethod.PATCH, requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        var json = JsonPath.parse(result);
        assertThat(json.read("$.firstName", String.class)).isEqualTo("CorrectedFirstName");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("CorrectedLastName");
        assertThat(json.read("$.indexNumber", Integer.class)).isEqualTo(1234);
    }
////////////////////////////////////////////////////


    @Test
    void Should_return_information_fields_are_empty() throws JSONException {
        //given
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);
        header.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("firstName", "");
        body.put("lastName", "");
        body.put("indexNumber", null);\\
        body.put("mail", "qwerty.pl");
        body.put("birthDay", null);
        HttpEntity<?> requestHeaders = new HttpEntity<>(body.toString(), header);
        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.POST,requestHeaders,
                String.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$.firstName", String.class)).isEqualTo("wielkość musi należeć do zakresu od 2 do 15");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("wielkość musi należeć do zakresu od 2 do 30");
        assertThat(json.read("$.indexNumber", String.class)).isEqualTo("nie może mieć wartości null");
        assertThat(json.read("$.mail", String.class)).isEqualTo("musi być poprawnie sformatowanym adresem e-mail");
    }

    @Test
    void Should_return_403_when_token_is_inValid() {
        //given
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization","ere");
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestHeaders = new HttpEntity<>(header);

        //when
        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.POST,requestHeaders, String.class);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(403);
    }

}
