package pl.grabowski.studentmanager.controller.course;

import javax.validation.constraints.Size;

public class CourseCreateRequest {
    @Size(min = 2, max = 25)
    private final String name;
    @Size(min = 3, max = 100)
    private final String description;

    public CourseCreateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
