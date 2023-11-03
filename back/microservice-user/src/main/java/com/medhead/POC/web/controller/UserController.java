package com.medhead.POC.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.medhead.POC.dao.UserRepository;
import com.medhead.POC.models.User;

@RestController
public class UserController {
    @Autowired
    UserRepository repository;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
    
    @PostMapping(value = "/user/register")
    public ResponseEntity<String> addUser(
        @RequestParam(value = "username", required = true)String username,
        @RequestParam(value = "password", required = true)String pwd_b64) {

        User user = repository.findByUsername(username);

        if (user != null) {
            return new ResponseEntity<String>("Username already used", null, HttpStatus.CONFLICT);
        }
        String encrypted = encoder.encode(pwd_b64);
        repository.save(new User(username, encrypted));

        return new ResponseEntity<String>("User successfully registered", null, HttpStatus.OK);
    }

    @PostMapping(value = "/user/login")
    public ResponseEntity<String> logUser(
        @RequestParam(value = "username", required = true)String username,
        @RequestParam(value = "password", required = true)String pwd_b64) {

        User user = repository.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<String>("The pair username/password is uncorrect", null, HttpStatus.UNAUTHORIZED);
        }

        if (!encoder.matches(pwd_b64, user.password)) {
            return new ResponseEntity<String>("The pair username/password is uncorrect", null, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<String>("User successfully logged in", null, HttpStatus.OK);
    }
}