package com.medhead.POC;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.ExecutionException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PocApplicationTests {

/**
 * Sanity check test.
 */
    @Autowired
    private PocApplication controller;

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
        ResponseEntity<String> response = this.restTemplate.exchange("http://localhost:" + port + "/hospital?latitude=50.39028759089191&longitude=-3.9204667072600907", HttpMethod.GET, null, String.class); 
        assertEquals(400, response.getStatusCode().value());
        assertThat(response.getBody()).contains("Bad Request");
        response = this.restTemplate.exchange("http://localhost:" + port + "/hospital?speciality=Cardiologie&longitude=-3.9204667072600907", HttpMethod.GET, null, String.class); 
        assertEquals(400, response.getStatusCode().value());
        assertThat(response.getBody()).contains("Bad Request");
        response = this.restTemplate.exchange("http://localhost:" + port + "/hospital?speciality=Cardiologie&latitude=50.39028759089191", HttpMethod.GET, null, String.class); 
        assertEquals(400, response.getStatusCode().value());
        assertThat(response.getBody()).contains("Bad Request");
        response = this.restTemplate.exchange("http://localhost:" + port + "/hospital?speciality=&latitude=50.39028759089191&longitude=-3.9204667072600907", HttpMethod.GET, null, String.class); 
        assertThat(response.getBody()).contains("Bad Request");
        assertEquals(400, response.getStatusCode().value());
    }
}
