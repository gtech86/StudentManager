package pl.grabowski.studentmanager.controller.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping(name = "/login")
    public void Login(){
    }
}
