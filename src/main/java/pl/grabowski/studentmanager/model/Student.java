package pl.grabowski.studentmanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "student")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private Date birthDay;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, Integer indexNumber, Date birthDay, StudentAddress studentAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.birthDay = birthDay;
        this.studentAddress = studentAddress;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", indexNumber=" + indexNumber +
                ", birthDay=" + birthDay +
                ", studentAddress=" + studentAddress +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(indexNumber, student.indexNumber) && Objects.equals(birthDay, student.birthDay) && Objects.equals(studentAddress, student.studentAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, indexNumber, birthDay, studentAddress);
    }

    @OneToOne
    @JoinColumn(name = "addressId")
    private StudentAddress studentAddress;

}
