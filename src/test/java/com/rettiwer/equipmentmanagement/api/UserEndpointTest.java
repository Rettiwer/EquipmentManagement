package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessTokenExtension;
import com.rettiwer.equipmentmanagement.user.role.Role;
import com.rettiwer.equipmentmanagement.user.role.RoleRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
//@ExtendWith(MockAccessTokenExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
public class UserEndpointTest {

    @Value("${application.api.route}/users")
    private String API_ROUTE_USERS;

   // @MockAccessToken
    private String ACCESS_TOKEN;

    @Autowired
    private RoleRepository roleRepository;


    @Test
    @Order(1)
    public void getAllUserItems_thenReturnList() {
        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS);
    }
}
