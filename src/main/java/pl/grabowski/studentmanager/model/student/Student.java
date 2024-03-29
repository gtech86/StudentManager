package pl.grabowski.studentmanager.model.student;

import org.apache.maven.repository.internal.SnapshotMetadataGeneratorFactory;
import pl.grabowski.studentmanager.model.course.Course;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "mail")
    private String mail;
    @Column(name = "index_number")
    private Integer indexNumber;
    @Column(name = "birth_day")
    private Date birthDay;

    @ManyToMany
    @JoinTable(name = "student_course",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private Set<Course> courses;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, String mail, Integer indexNumber, Date birthDay) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.indexNumber = indexNumber;
        this.birthDay = birthDay;
    }

    public Student(String firstName, String lastName, String mail, Integer indexNumber, Date birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.indexNumber = indexNumber;
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", indexNumber=" + indexNumber +
                ", birthDay=" + birthDay +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(mail, student.mail) && Objects.equals(indexNumber, student.indexNumber) && Objects.equals(birthDay, student.birthDay) && Objects.equals(courses, student.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, mail, indexNumber, birthDay, courses);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber = indexNumber;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course){
        courses.add(course);
    }
}
