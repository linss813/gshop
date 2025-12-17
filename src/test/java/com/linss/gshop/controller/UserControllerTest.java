package com.linss.gshop.controller;

import com.linss.gshop.entity.dto.LoginRequest;
import com.linss.gshop.entity.dto.RegisterRequest;
import com.linss.gshop.util.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/user";
        headers = new HttpHeaders();
    }

    @Test
    void testCheckUsername() {
        String url = baseUrl + "/checkUsername/testuser";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCheckEmail() {
        String url = baseUrl + "/checkEmail/test@example.com";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser" + System.currentTimeMillis());
        registerRequest.setPassword("password123");
        registerRequest.setEmail("test" + System.currentTimeMillis() + "@example.com");
        registerRequest.setPhone("13800138000");

        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<Result> response = restTemplate.exchange(
                baseUrl + "/register",
                HttpMethod.POST,
                requestEntity,
                Result.class
        );

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("123456");

        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<Result> response = restTemplate.exchange(
                baseUrl + "/login",
                HttpMethod.POST,
                requestEntity,
                Result.class
        );

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllUser() {
        String url = baseUrl + "/getAllUser";
        ResponseEntity<Result> response = restTemplate.getForEntity(url, Result.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}