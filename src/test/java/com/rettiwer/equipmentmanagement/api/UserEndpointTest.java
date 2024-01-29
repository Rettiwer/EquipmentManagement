package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationResponse;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessTokenExtension;
import com.rettiwer.equipmentmanagement.user.UserDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@ExtendWith(MockAccessTokenExtension.class)
public class UserEndpointTest {
    @Value("${application.api.route}")
    private String API_ROUTE;
    @Value("${application.api.route}/users")
    private String API_ROUTE_USERS;

    @MockAccessToken
    private String ACCESS_TOKEN;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    @Order(1)
    void getAllUsers_WithItems_thenReturnList() {
        DatabaseSeeder.generateInvoice(2, ACCESS_TOKEN, API_ROUTE);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE_USERS);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    @Order(2)
    void createSupervisorWithEmployee_thenReturnSupervisorWithEmployees() {
        var supervisorRequest = DatabaseSeeder.createNewUser();
        supervisorRequest.setRoles(List.of(new RoleDTO("ROLE_SUPERVISOR")));

        //Create supervisor
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(supervisorRequest)
                .post(API_ROUTE + "/users").as(UserDTO.class);

        var employeeRequest = DatabaseSeeder.createNewUser();
        employeeRequest.setRoles(List.of(new RoleDTO("ROLE_EMPLOYEE")));
        employeeRequest.setSupervisorId(response.getId());

        //Create new employee with supervisor
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(employeeRequest)
                .post(API_ROUTE + "/users");

        //Get supervisor with employees
        var getSupervisor = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE + "/users/" + response.getId()).as(UserDTO.class);

        assertEquals(HttpStatus.OK.value(), HttpStatus.OK.value());
        assertNull(response.getEmployees());
        assertNotNull(getSupervisor.getEmployees());
    }

    @Test
    @Order(2)
    void createEmployee_WithoutPermissions_thenReturnInsufficientPermissionException() {
        var employeeRequest = DatabaseSeeder.createNewUser();
        employeeRequest.setRoles(List.of(new RoleDTO("ROLE_EMPLOYEE")));

        String newAccessToken = authenticationService.register(employeeRequest).getAccessToken();

        var secondEmployeeRequest = DatabaseSeeder.createNewUser();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + newAccessToken)
                .body(secondEmployeeRequest)
                .post(API_ROUTE + "/users");

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }
}
