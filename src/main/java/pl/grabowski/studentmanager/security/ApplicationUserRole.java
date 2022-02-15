package pl.grabowski.studentmanager.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.grabowski.studentmanager.security.ApplicationUserPermissions.*;

public enum ApplicationUserRole {
    USER(new HashSet<>(Arrays.asList(STUDENT_READ, COURSE_READ))),
    ADMIN(new HashSet<>(Arrays.asList(STUDENT_READ, STUDENT_WRITE, COURSE_READ, COURSE_WRITE)));

    private final Set<ApplicationUserPermissions> permissions;

    ApplicationUserRole(Set<ApplicationUserPermissions> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermissions> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthority(){
        var permissions =  getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissions()))
                .collect(Collectors.toList());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
