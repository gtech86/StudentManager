package pl.grabowski.studentmanager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.grabowski.studentmanager.security.LoginCredential;

@RestController
public class LoginController {

    @PostMapping(name = "/login")
    public void Login(@RequestBody LoginCredential loginCredential){
    }
}
