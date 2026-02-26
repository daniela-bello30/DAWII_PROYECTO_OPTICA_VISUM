package pe.edu.cibertec.msseguridad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHashController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test/hash/{pass}")
    public String hash(@PathVariable String pass) {
        return passwordEncoder.encode(pass);
    }
}