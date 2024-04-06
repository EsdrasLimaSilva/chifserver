package me.edu.chifserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("")
    public ResponseEntity<String> getHello(){
        return new ResponseEntity<>("Olá Mundo", HttpStatus.OK);
    }
}
