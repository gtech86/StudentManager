package pl.grabowski.studentmanager.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.studentmanager.model.student.StudentAddress;
import pl.grabowski.studentmanager.service.student.StudentAddressService;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/students")
public class StudentAddressController {

    private final StudentAddressService studentAddressService;

    @PostMapping(path = "/{id}/address")
    public void addNewAddress(@PathVariable Long studentId, @RequestBody StudentAddress studentAddress){

    }
}
