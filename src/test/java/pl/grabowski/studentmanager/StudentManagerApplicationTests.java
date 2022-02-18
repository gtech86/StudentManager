package pl.grabowski.studentmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.grabowski.studentmanager.model.student.Student;
import pl.grabowski.studentmanager.repository.student.StudentRepository;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.apache.tomcat.util.net.SocketEvent.TIMEOUT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class StudentIntegrationTest {

    @LocalServerPort
    private int port;

    private final List<Student> testStudents = new ArrayList<>();

    private String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iLCJjb3Vyc2U6cmVhZCIsImNvdXJzZTp3cml0ZSIsInN0dWRlbnQ6cmVhZCIsInN0dWRlbnQ6d3JpdGUiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNjQ1MjY0NDU2fQ.S4V1vU3u8MN9wlYBa7w_ZSh7oUFxeXVF8PMoKBSU0l8";
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    @Autowired
    private StudentRepository studentRepository;

    private void initData(){
        testStudents.add(new Student(1L,"John","Bond", "john@bond.pl", 4567, Date.valueOf(LocalDate.now())));
        testStudents.add(new Student(2L, "Paweł","Grabowski","pawel@grabowski.pl", 1234, Date.valueOf(LocalDate.now())));
        studentRepository.saveAll(testStudents);
    }

    private void generateToken(){

    }

    @Test
    void contextLoads() {
    }

    @Test
    void ShouldReturnAccessTokenWhenLogin() {
        String accessToken = "";
        var result = restTemplate.postForEntity("http://localhost:" + port + "/login?username=admin&password=adminPass", accessToken, String.class);
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$.access_token", String.class)).isNotEmpty();
    }

    @Test
    void ShouldReturnAllStudent() throws IOException {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initData();
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
    void ShouldReturnResponseAsJson() throws IOException {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initData();
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.GET, requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void ShouldAbleToAddNewStudent() throws JSONException {
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
        //String jsonBody = result.getBody();
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$.firstName", String.class)).isEqualTo("James");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("Bond");
        assertThat(json.read("$.indexNumber", Integer.class)).isEqualTo(1234);
    }

    @Test
    void ShouldAbleToFindStudentById() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initData();
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
    void ShouldAbleToRemoveStudent(){
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity requestHeaders = new HttpEntity(headers);
        initData();
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students/2", HttpMethod.DELETE,requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isEqualTo("Student deleted");
    }

    @Test
    void ShouldAbleToUpdateStudent() throws JSONException {
        //given

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);
        JSONObject body = new JSONObject();
        body.put("firstName", "Poprawiony");
        body.put("lastName", "LastCorrect");

        HttpEntity<?> requestHeaders = new HttpEntity<>(header);
        // when
        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.PATCH, requestHeaders, String.class);

        // then
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(result);
        assertThat(json.read("$.firstName", String.class)).isEqualTo("Paweł");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("Grabowski");
        assertThat(json.read("$.indexNumber", Integer.class)).isEqualTo(1234);
        assertThat(json.read("$.birthDay", Integer.class)).isEqualTo(1234);

    }

    @Test
    void ShouldReturnInformationFieldsAreEmpty() throws JSONException {
        //given
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", token);
        header.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("firstName", "");
        body.put("lastName", "");
        body.put("indexNumber", "");
        body.put("birthDay", "");

        HttpEntity<?> requestHeaders = new HttpEntity<>(body.toString(), header);

        var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.POST,requestHeaders,
                String.class);

        //then
        //String jsonBody = result.getBody();
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.hasBody()).isTrue();
        String jsonBody = result.getBody();
        var json = JsonPath.parse(jsonBody);
        assertThat(json.read("$.firstName", String.class)).isEqualTo("James");
        assertThat(json.read("$.lastName", String.class)).isEqualTo("Bond");
        assertThat(json.read("$.indexNumber", Integer.class)).isEqualTo(1234);
    }

    @Test
    void ShouldThrowExceptionWhenTokenIsInValid() {
        //given
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization","ere");
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestHeaders = new HttpEntity<>(header);
        assertThatThrownBy(() -> {
            var result = restTemplate.exchange("http://localhost:"+port+"/students", HttpMethod.POST,requestHeaders,
                    String.class);
        }).isInstanceOf(HttpClientErrorException.class);

        //then

    }

}
