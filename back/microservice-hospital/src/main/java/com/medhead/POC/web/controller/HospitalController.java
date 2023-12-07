package com.medhead.POC.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

        Hospital result = hospitalLogic.GetNearestHospital(this, speciality, latitude, longitude);
        data.put("data", "not_found");

        if (result == null) {
            return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
        }

        data.put("data", result.getOrganisationName());
        return new ResponseEntity<Map<String, String>>(data, null, HttpStatus.OK);
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
