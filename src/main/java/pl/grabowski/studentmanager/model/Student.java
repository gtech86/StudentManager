package pl.grabowski.studentmanager.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String mail;
    private Integer indexNumber;
    private Date birthDay;

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

    public StudentAddress getStudentAddress() {
        return studentAddress;
    }

    public void setStudentAddress(StudentAddress studentAddress) {
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
