package pl.grabowski.studentmanager.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

public class StudentCreateRequest {
    @NotNull
    @Size(min=2, max=15)
    private final String firstName;
    @NotNull
    @Size(min=2, max=30)
    private final String lastName;
    @Email
    private final String mail;
    @NotNull
    private final Integer indexNumber;
    @NotNull
    @Past
    private final Date birthDay;

    public StudentCreateRequest(String firstName, String lastName, String mail, Integer indexNumber, Date birthDay) {
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
