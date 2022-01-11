package pl.grabowski.studentmanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.grabowski.studentmanager.model.StudentAddress;
import pl.grabowski.studentmanager.service.StudentAddressService;


@RequiredArgsConstructor
@Controller

@RequestMapping(path = "/students")
public class StudentAddressController {

    private final StudentAddressService studentAddressService;

    @PostMapping(path = "/{id}/address", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addNewAddress(@PathVariable Long studentId, @RequestBody StudentAddress studentAddress){

    }
}
