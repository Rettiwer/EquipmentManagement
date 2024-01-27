package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
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
        var request = DatabaseSeeder.createNewUser();
        request.setRoles(List.of(new RoleDTO("NOT_EXISTING_ROLE")));

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post(API_ROUTE + "/register");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }
    @Test
    @Order(2)
    public void whenUserCreated_thenTokenReturned() {
        var request = DatabaseSeeder.createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post(API_ROUTE + "/register");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.jsonPath().get("access_token"));
    }

    @Test
    @Order(3)
    public void whenUserCreated_isExisting_thenError() {
        var request = DatabaseSeeder.createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post(API_ROUTE + "/register");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    @Order(4)
    public void whenUserAuthenticated_thenTokenReturned() {
        var request = DatabaseSeeder.createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .post(API_ROUTE + "/authenticate");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNotNull(response.jsonPath().get("access_token"));
    }
}
