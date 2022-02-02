package pl.grabowski.studentmanager.controller.student;

public class StudentCourseResponse {
    private final Long studentId;
    private final Long courseId;
    private final String courseName;
    private final String courseDescription;

    public StudentCourseResponse(Long studentId, Long courseId, String courseName, String courseDescription) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }
}
