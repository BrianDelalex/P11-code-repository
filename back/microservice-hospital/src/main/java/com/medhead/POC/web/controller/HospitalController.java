package com.medhead.POC.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.medhead.POC.web.logic.HospitalLogic;
import com.medhead.POC.dao.HospitalRepository;
import com.medhead.POC.dao.SpecialityRepository;
import com.medhead.POC.models.Hospital;
import com.medhead.POC.models.Speciality;
import com.medhead.POC.models.MatrixApiTypes.Response;

@RestController
@CrossOrigin(maxAge = 3600)
public class HospitalController {
    @Autowired
    private SpecialityRepository specialityRepository;
    public RestTemplate restTemplate;

    private HospitalLogic hospitalLogic = new HospitalLogic();

    @Autowired
    public HospitalRepository repository;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return this.restTemplate = builder.build();
    }

    /** GET Route at /hospital.
     * Retrieves the nearest hospital with a corresponding speciality.
     * @param speciality The speciality you are looking for.
     * @param latitude Latitude of the start position
     * @param longitude Longitude of the start position
     * @return The nearest hospital from the given coords with the given speciality.
    */
    @GetMapping(value = "/hospital", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> nearestHospital(
        @RequestParam(value = "speciality", required = true)String speciality,
        @RequestParam(value = "latitude", required = true)String latitude,
        @RequestParam(value = "longitude", required = true)String longitude)
    {
        Map<String, String> data = new HashMap<>();
        if (speciality.isEmpty()) {
            data.put("reason", "INVALID_PARAMETER");
            data.put("message", "'speciality' parameter is empty.");
            return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.BAD_REQUEST);
        }

        long start = System.currentTimeMillis();
        Hospital result = hospitalLogic.GetNearestHospital(this, speciality, latitude, longitude);
        long time = System.currentTimeMillis() - start;
        System.out.println("duration " + time + " ms");
        data.put("data", "not_found");

        if (result == null) {
            return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
        }

        data.put("data", result.getOrganisationName());
        return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
        // List<String> specialities = new ArrayList<String>();
        // specialities.add(speciality);
        // List<Hospital> hospitals = repository.findBySpecialitiesIsContaining(specialities);
        // List<Response> responses = new ArrayList<Response>();

        // hospitals = hospitals
        //     .stream()
        //     .filter(h -> h.getFreeBeds() > 0)
        //     .collect(Collectors.toList());

        
        // long start = System.currentTimeMillis();
        // for (Hospital h : hospitals) {
        //     if (!hospitals.isEmpty()) {
        //         HttpHeaders headers = new HttpHeaders();
        //         List<MediaType> mediaTypes = new ArrayList<MediaType>();;
        //         mediaTypes.add(MediaType.ALL);
        //         headers.setAccept(mediaTypes);
        //         String url = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations={destinations}&origins={origins}&key={key}";

        //         Map<String, String> params = new HashMap<>(Collections.singletonMap("destinations", h.getLatitude() + "," + h.getLongitude()));
        //         params.put("origins", latitude + "," + longitude);
        //         params.put("key", "AIzaSyCs6HGKrAjFRAzM_B3dVS50LrKcxboru54");

        //         HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
        //         HttpEntity<Response> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Response.class, params);

        //         responses.add(response.getBody());
        //     }
        // }
        // long time = System.currentTimeMillis() - start;
        // System.out.println("duration " + time + " ms");

        // Response nearest = responses
        //     .stream()
        //     .min(Comparator.comparing(Response::GetDuration))
        //     .orElse(null);
        // Hospital result = null;
        // if (nearest != null) {
        //     int i = 0;
        //     for (Response r : responses) {
        //         if (r == nearest) {
        //             result = hospitals.get(i);
        //             break;
        //         }
        //         i++;
        //     }
        // }

        // Map<String, String> data = new HashMap<>();
        // data.put("data", "not_found");

        // if (result == null) {
        //     return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
        // }

        // data.put("data", result.getOrganisationName());
        // return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
    }

     /** GET Route at /hospital/specialities.
     * Retrieves all the medical specialities and group of medical specialities in the database.
     * @return A list of all medical specialities with the group they belong with.
    */
    @GetMapping(value = "/hospital/specialities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllSpecialities() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<Speciality> specialities = specialityRepository.findAll();

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
