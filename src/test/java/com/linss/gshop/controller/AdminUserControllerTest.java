package com.linss.gshop.controller;

import com.linss.gshop.entity.dto.AdminUserDTO;
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
class AdminUserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/admin";
        headers = new HttpHeaders();
    }

    @Test
    void testAdminLogin() {
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setUsername("admin");
        adminUserDTO.setPassword("123456");

        HttpEntity<AdminUserDTO> requestEntity = new HttpEntity<>(adminUserDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/login",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testRegisterAdmin() {
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setUsername("testadmin" + System.currentTimeMillis());
        adminUserDTO.setPassword("password123");

        HttpEntity<AdminUserDTO> requestEntity = new HttpEntity<>(adminUserDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/register",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        assertEquals(200, response.getStatusCodeValue());
    }
}