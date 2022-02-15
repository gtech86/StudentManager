package pl.grabowski.studentmanager.controller.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void ShouldReturnAccessTokenWhenLogin() throws Exception {
        mvc
                .perform(post("/login?username=admin&password=adminPass")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
                .andExpect(jsonPath("$.access_token",notNullValue()))
                .andReturn();
    }

}