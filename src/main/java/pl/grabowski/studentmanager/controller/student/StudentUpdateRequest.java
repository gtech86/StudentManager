package pl.grabowski.studentmanager.controller.student;

import java.util.Date;
import java.util.Optional;

public class StudentUpdateRequest {
    public final String firstName;
    public final String lastName;
    public final String mail;
    public Integer indexNumber;
    public Date birthDay;

    public StudentUpdateRequest(String firstName, String lastName, String mail, Integer indexNumber, Date birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.indexNumber = indexNumber;
        this.birthDay = birthDay;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }

    public Date getBirthDay() {
        return birthDay;
    }
}
