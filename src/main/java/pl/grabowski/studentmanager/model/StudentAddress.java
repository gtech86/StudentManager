package pl.grabowski.studentmanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "StudentAddress")
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class StudentAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String street;
    private int number;
    private int postalCode;


}
