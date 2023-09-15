package com.medhead.POC;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MhospitalApplicationTests {

/**
 * Sanity check test.
 */
    @Autowired
    private MhospitalApplication controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

/**
 * Testing http route.
 */
    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void helloShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/hello",
                String.class)).contains("Hello World!");
    }

    @Test
    public void hospitalShouldReturnErrorMissingParameters() throws Exception {
        String url = "http://localhost:" + port + "/hospital";
        String uri;
        Map<String, String> params = new HashMap<>();
        params.put("speciality", "Cardiologie");
        params.put("latitude", "50.39028759089191");
        params.put("longitude", "-3.9204667072600907");

        // Test without speciality parameter
        uri = "?latitude={latitude}&longitude={longitude}";
        ResponseEntity<String> response = this.restTemplate.exchange(url + uri , HttpMethod.GET, null, String.class, params);
        assertEquals(400, response.getStatusCode().value());
        assertThat(response.getBody()).contains("Bad Request");

        // Test without latitude parameter
        uri = "?speciality={speciality}&longitude={longitude}";
        response = this.restTemplate.exchange(url + uri, HttpMethod.GET, null, String.class, params); 
        assertEquals(400, response.getStatusCode().value());
        assertThat(response.getBody()).contains("Bad Request");

        // Test without longitude parameter
        uri = "?speciality={speciality}&latitude={latitude}";
        response = this.restTemplate.exchange(url + uri, HttpMethod.GET, null, String.class, params); 
        assertEquals(400, response.getStatusCode().value());
        assertThat(response.getBody()).contains("Bad Request");

        // Test with empty speciality parameter
        params.put("speciality", "");
        uri = "?speciality={speciality}&latitude={latitude}&longitude={longitude}";
        response = this.restTemplate.exchange(url + uri, HttpMethod.GET, null, String.class, params); 
        assertThat(response.getBody()).contains("INVALID_PARAMETER");
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void hospitalShouldReturnOkWithoutData() throws Exception {
        String url = "http://localhost:" + port + "/hospital";
        String uri = "?speciality={speciality}&latitude={latitude}&longitude={longitude}";
        Map<String, String> params = new HashMap<>();

        params.put("speciality", "azertd");
        params.put("latitude", "50.39028759089191");
        params.put("longitude", "-3.9204667072600907");

        // Test with invalid speciality
        ResponseEntity<String> response = this.restTemplate.exchange(url + uri, HttpMethod.GET, null, String.class, params); 
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody()).contains("\"data\":\"not_found\"");
    }

    @Test
    public void hospitalShouldReturnOkAndHospitalName() throws Exception {
        String url = "http://localhost:" + port + "/hospital";
        String uri = "?speciality={speciality}&latitude={latitude}&longitude={longitude}";
        Map<String, String> params = new HashMap<>();

        params.put("speciality", "Cardiologie");
        params.put("latitude", "50.39028759089191");
        params.put("longitude", "-3.9204667072600907");

        ResponseEntity<String> response = this.restTemplate.exchange(url + uri, HttpMethod.GET, null, String.class, params);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody()).contains("Lee Mill Hospital");
    }
}
