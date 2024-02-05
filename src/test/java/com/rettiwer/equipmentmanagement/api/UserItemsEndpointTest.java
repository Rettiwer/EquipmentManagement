package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationRequest;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.item.UserItemsDTO;
import com.rettiwer.equipmentmanagement.api.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.api.jwt.MockAccessTokenExtension;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        DatabaseSeeder.insertInvoice(5, supervisor.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

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

        DatabaseSeeder.insertInvoice(5, supervisor.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        DatabaseSeeder.insertInvoice(2, employee.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

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

        DatabaseSeeder.insertInvoice(5, employee.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        //MockedAdmin Items
        DatabaseSeeder.insertInvoice(6, 1, ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/items");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1, response.as(UserItemsDTO[].class).length);
    }

    @Test
    void getSingleUserItems_asAdmin_thenReturnAllUserItems() {
        var supervisor = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        DatabaseSeeder.insertInvoice(5, supervisor.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/" + supervisor.getId() + "/items");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(5, response.as(UserItemsDTO.class).getItems().size());
    }

    @Test
    void getSingleUserItemsFromOtherSupervisor_asSupervisor_thenInsufficientPermissionsException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")), null);

        DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                1, ACCESS_TOKEN, API_ROUTE_USERS);

        DatabaseSeeder.insertInvoice(2, employee.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/" + employee.getId() + "/items");

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }
    @Test
    void getSingleUserItems_asSupervisor_thenReturnOwnAndEmployeesUserItems() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")), null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), supervisorAccessToken, API_ROUTE_USERS);

        DatabaseSeeder.insertInvoice(2, employee.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/" + employee.getId() + "/items");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(2, response.as(UserItemsDTO.class).getItems().size());
    }

    @Test
    void getSingleUserItemsFromOtherUser_asEmployee_thenInsufficientPermissionsException() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")), null);

        DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/" + 1 + "/items");

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void getSingleUserItems_asEmployee_thenReturnOwnUserItems() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")), null);

        var employee = DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        DatabaseSeeder.insertInvoice(5, employee.getId(), ACCESS_TOKEN, API_ROUTE_INVOICE);

        var response = RestAssured.given()
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_USERS + "/" + employee.getId() + "/items");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(5, response.as(UserItemsDTO.class).getItems().size());
    }
}
