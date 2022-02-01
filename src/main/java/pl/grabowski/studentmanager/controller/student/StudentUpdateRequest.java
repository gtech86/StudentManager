package pl.grabowski.studentmanager.controller.student;

import java.util.Date;
import java.util.Optional;

public class StudentUpdateRequest {
    public final Optional<String> firstName;
    public final Optional<String> lastName;
    public final Optional<String> mail;
    public Optional<Integer> indexNumber;
    public Optional<Date> birthDay;

    public StudentUpdateRequest(Optional<String> firstName, Optional<String> lastName, Optional<String> mail, Optional<Integer> indexNumber, Optional<Date> birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.indexNumber = indexNumber;
        this.birthDay = birthDay;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getMail() {
        return mail;
    }

    public Optional<Integer> getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(Optional<Integer> indexNumber) {
        this.indexNumber = indexNumber;
    }

    public Optional<Date> getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Optional<Date> birthDay) {
        this.birthDay = birthDay;
    }
}
