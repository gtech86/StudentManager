package pl.grabowski.studentmanager.security;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ApplicationUserRoleTest {

    @Test
    void shouldReturnAllPermissions(){
        assertThat(ApplicationUserPermissions.COURSE_READ.name()).isEqualTo("course:read");
    }
}