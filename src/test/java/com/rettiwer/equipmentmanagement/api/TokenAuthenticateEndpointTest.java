package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.user.role.Role;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TokenAuthenticateEndpointTest {
    @Value("${application.api.route}/auth")
    private String API_ROUTE;

    @Test
    @Order(1)
    public void whenUserCreated_withNotExistingRole_thenError() {
        RegisterRequest registerRequest = createNewUser();
        registerRequest.setRoles(List.of(new RoleDTO("NOT_EXISTING_ROLE")));

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerRequest)
                .post(API_ROUTE + "/register");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }
    @Test
    @Order(2)
    public void whenUserCreated_thenTokenReturned() {
        RegisterRequest registerRequest = createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerRequest)
                .post(API_ROUTE + "/register");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.jsonPath().get("access_token"));
    }

    @Test
    @Order(3)
    public void whenUserCreated_isExisting_thenError() {
        RegisterRequest registerRequest = createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerRequest)
                .post(API_ROUTE + "/register");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    @Order(4)
    public void whenUserAuthenticated_thenTokenReturned() {
        RegisterRequest registerRequest = createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerRequest)
                .post(API_ROUTE + "/authenticate");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.jsonPath().get("access_token"));
    }

    private RegisterRequest createNewUser() {
        RegisterRequest user = new RegisterRequest();
        user.setEmail("someone@example.com");
        user.setFirstname("John");
        user.setLastname("Smith");
        user.setPassword("StrongPass!");
        user.setRoles(List.of(new RoleDTO("ROLE_ADMIN")));
        return user;
    }
}
