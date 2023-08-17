package com.medhead.POC;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PocApplicationTests {

    @Autowired
    private PocApplication controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

}
