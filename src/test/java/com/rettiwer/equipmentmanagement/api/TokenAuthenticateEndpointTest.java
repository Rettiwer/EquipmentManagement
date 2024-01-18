package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.invoice.InvoiceDTO;
import com.rettiwer.equipmentmanagement.user.Role;
import com.rettiwer.equipmentmanagement.user.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Date;

import static com.rettiwer.equipmentmanagement.EquipmentManagementApplicationTest.ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TokenAuthenticateEndpointTest {
    @Value("${application.api.route}/auth")
    private String API_ROUTE;

    @Test
    @Order(1)
    public void whenUserCreated_thenTokenReturned() {
        User user = createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROUTE + "/register");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.jsonPath().get("access_token"));
    }

    @Test
    @Order(2)
    public void whenUserAuthenticated_thenTokenReturned() {
        User user = createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROUTE + "/authenticate");

        ACCESS_TOKEN = response.jsonPath().get("access_token");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.jsonPath().get("access_token"));
    }

    private User createNewUser() {
        User user = new User();
        user.setEmail("someone@example.com");
        user.setFirstname("John");
        user.setLastname("Smith");
        user.setPassword("StrongPass!");
        user.setRole(Role.ADMIN);
        return user;
    }
}
