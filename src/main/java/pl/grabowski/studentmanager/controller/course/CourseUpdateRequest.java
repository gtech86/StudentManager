package pl.grabowski.studentmanager.controller.course;

import java.util.Optional;

public class CourseUpdateRequest {
    private final Optional<String> name;
    private final Optional<String> description;

    public CourseUpdateRequest(Optional<String> name, Optional<String> description) {
        this.name = name;
        this.description = description;
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getDescription() {
        return description;
    }
}