package com.medhead.POC;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medhead.POC.dataInterfaces.Hospital;
import com.medhead.POC.dataInterfaces.HospitalRepository;

@SpringBootApplication
@RestController
public class PocApplication {

  @Autowired
  private HospitalRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(PocApplication.class, args);
  }

  @GetMapping("/hello")
  public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
    return String.format("Hello %s!", name);
  }

  @GetMapping(value = "/hospital/bySpeciality", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> hospitalBySpeciality(@RequestParam(value = "speciality", defaultValue = "")String speciality) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Custom-Header", "foo");
      if (speciality.isEmpty()) {
        return new ResponseEntity<>("Bad Request", headers, HttpStatus.BAD_REQUEST);
      }
      List<String> specialities = new ArrayList<String>();
      specialities.add(speciality);
      System.out.println("Request at /hospital/bySpeciality with param speciality=" + speciality);
      List<Hospital> hospitals = repository.findBySpecialitiesIsContaining(specialities);

      return new ResponseEntity<String>(hospitals.toString(), headers, HttpStatus.OK);
    }
}
