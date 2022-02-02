package pl.grabowski.studentmanager.controller.course;

public class CourseStudentResponse {
    public Long courseId;
    public Long studentId;
    private String firstName;
    private String lastName;
    private Integer indexNumber;

    public CourseStudentResponse(Long courseId, Long studentId, String firstName, String lastName, Integer indexNumber) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }
}
