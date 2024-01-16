package com.rettiwer.equipmentmanagement;

import com.rettiwer.equipmentmanagement.user.Role;
import com.rettiwer.equipmentmanagement.user.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTest {

    private static final String API_ROOT = "http://127.0.0.1/api/auth";

    @Test
    public void whenCreatedNewUser_thenJWTTokenCreated() {
        User user = createNewUser();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(user)
                .post(API_ROOT + "/register");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
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
