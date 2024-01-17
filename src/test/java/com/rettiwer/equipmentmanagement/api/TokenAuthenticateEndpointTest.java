package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.user.Role;
import com.rettiwer.equipmentmanagement.user.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.rettiwer.equipmentmanagement.EquipmentManagementApplicationTest.ACCESS_TOKEN;
import static com.rettiwer.equipmentmanagement.EquipmentManagementApplicationTest.API_ROOT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TokenAuthenticateEndpointTest {
    private static final String API_ROUTE = API_ROOT + "/auth";

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
