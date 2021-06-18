package com.oracle.codekata.service.filecontroller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FileControllerApplication controller;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertTrue(controller != null);
    }

    @Test
    public void FileController_Positive() {
        try{


            RestTemplate restTemplate = new RestTemplate();

            final String baseUrl = "http://localhost:" + port + "/files/weather";
            URI uri = new URI(baseUrl);

            ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

            //Verify request succeed for weather
            Assertions.assertEquals(200, result.getStatusCodeValue());
            Assertions.assertTrue(result.getBody().contains("temperature"));
        }
        catch (Exception e){
            Assertions.fail();
        }

    }

    @Test
    public void FileController_Fail() {
        try{


            RestTemplate restTemplate = new RestTemplate();

            final String baseUrl = "http://localhost:" + port + "/files/bleh";
            URI uri = new URI(baseUrl);

            ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

            //should not get here
            Assertions.fail();

        }
        catch (Exception e){
            Assertions.assertTrue(e.getMessage().contains("parameter"));
        }

    }
}