package pl.grabowski.studentmanager.controller.student;

import java.util.Date;

public class StudentResponse {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String mail;
    private final Integer indexNumber;
    private final Date birthDay;


    public StudentResponse(Long id, String firstName, String lastName, String mail, Integer indexNumber, Date birthDay) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.indexNumber = indexNumber;
        this.birthDay = birthDay;
    }

    public Long getId() {
        return id;
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
