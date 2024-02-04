package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationRequest;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.item.UserItemsDTO;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessTokenExtension;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockAccessTokenExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserItemsEndpointTest {
    @Value("${application.api.route}/users")
    private String API_ROUTE_USERS;

    @Value("${application.api.route}/invoices")
    private String API_ROUTE_INVOICE;


    @MockAccessToken
    private String ACCESS_TOKEN;


    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void getAllUserItems_asAdmin_thenReturnAllUserItems() {
        var supervisor = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_SUPERVISOR")),
                1, ACCESS_TOKEN, API_ROUTE_USERS);

        DatabaseSeeder.generateInvoice(5, supervisor.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/items");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(5, Arrays.stream(response.as(UserItemsDTO[].class))
                .filter(userItemsDTO -> Objects.equals(userItemsDTO.getId(), supervisor.getId()))
                .findFirst()
                .orElseThrow()
                .getItems().size());
    }

    @Test
    void getAllUserItems_asSupervisor_thenReturnOwnAndEmployeesUserItems() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                1);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        DatabaseSeeder.generateInvoice(5, supervisor.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        DatabaseSeeder.generateInvoice(2, employee.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/items");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(2, response.as(UserItemsDTO[].class).length);
    }

    @Test
    void getAllUserItems_asEmployee_thenReturnOwnUserItems() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                1);

        var employee = DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        DatabaseSeeder.generateInvoice(5, employee.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        //MockedAdmin Items
        DatabaseSeeder.generateInvoice(6, 1, ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/items");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1, response.as(UserItemsDTO[].class).length);
    }
}
