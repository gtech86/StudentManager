package pl.grabowski.studentmanager.model.course;

import pl.grabowski.studentmanager.model.student.Student;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courses_id")
    private Long id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "courses")
    private Set<Student> students;

    public Course() {
    }

    public Course(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(description, course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void addStudent(Student student){
        students.add(student);
    }

    public Set<Student> getStudents() {
        return students;
    }
}
