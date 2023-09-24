package com.medhead.POC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.medhead.POC.dao.SpecialityRepository;
import com.medhead.POC.models.Hospital;
import com.medhead.POC.models.HospitalRepository;
import com.medhead.POC.models.Speciality;
import com.medhead.POC.models.MatrixApiTypes.Response;

@SpringBootApplication
@CrossOrigin(maxAge = 3600)
@RestController
public class MhospitalApplication {
    @Autowired
    private SpecialityRepository specialityRepository;
    private HospitalRepository repository;
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MhospitalApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return this.restTemplate = builder.build();
    }

    @GetMapping(value = "/hello")
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

    @GetMapping(value = "/hospital", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> nearestHospital(
        @RequestParam(value = "speciality", required = true)String speciality,
        @RequestParam(value = "latitude", required = true)String latitude,
        @RequestParam(value = "longitude", required = true)String longitude)
    {
        if (speciality.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("reason", "INVALID_PARAMETER");
            data.put("message", "'speciality' parameter is empty.");
            return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.BAD_REQUEST);
        }
        List<String> specialities = new ArrayList<String>();
        specialities.add(speciality);
        List<Hospital> hospitals = repository.findBySpecialitiesIsContaining(specialities);
        List<Response> responses = new ArrayList<Response>();

        hospitals = hospitals
            .stream()
            .filter(h -> h.getFreeBeds() > 0)
            .collect(Collectors.toList());

        for (Hospital h : hospitals) {
            if (!hospitals.isEmpty()) {
                HttpHeaders headers = new HttpHeaders();
                List<MediaType> mediaTypes = new ArrayList<MediaType>();;
                mediaTypes.add(MediaType.ALL);
                headers.setAccept(mediaTypes);
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations={destinations}&origins={origins}&key={key}";

                Map<String, String> params = new HashMap<>(Collections.singletonMap("destinations", h.latitude + "," + h.longitude));
                params.put("origins", latitude + "," + longitude);
                params.put("key", "AIzaSyCs6HGKrAjFRAzM_B3dVS50LrKcxboru54");

                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
                HttpEntity<Response> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Response.class, params);

                System.out.println(h.getOrganisationName() + ": duration " + response.getBody().GetDuration());
                responses.add(response.getBody());
            }
        }

        Response nearest = responses
            .stream()
            .min(Comparator.comparing(Response::GetDuration))
            .orElse(null);
        Hospital result = null;
        if (nearest != null) {
            int i = 0;
            for (Response r : responses) {
                if (r == nearest) {
                    result = hospitals.get(i);
                    System.out.println("The nearest hospital is " + hospitals.get(i).getOrganisationName());
                    break;
                }
                i++;
            }
        }

        Map<String, String> data = new HashMap<>();
        data.put("data", "not_found");

        if (result == null) {
            return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
        }

        data.put("data", result.getOrganisationName());
        return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
    }

    @GetMapping(value = "/hospital/specialities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllSpecialities() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<Speciality> specialities = specialityRepository.findAll();

        System.out.println("List size " + specialities.size() );
        String json = "{ \"specialities\": [";
        for (Speciality s : specialities) {
            json += s.ToJson();
            json += ",";
        }
        json = json.substring(0, json.length() - 1);
        json += "]}";
        return new ResponseEntity<String>(json, headers, HttpStatus.OK);
    }
}
