package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationRequest;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.api.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.api.jwt.MockAccessTokenExtension;
import com.rettiwer.equipmentmanagement.user.UserDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import io.restassured.RestAssured;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@ExtendWith(MockAccessTokenExtension.class)
public class UserEndpointTest {
    @Value("${application.api.route}/users")
    private String API_ROUTE_USERS;

    @Value("${application.api.route}/invoices")
    private String API_ROUTE_INVOICES;

    @MockAccessToken
    private String ACCESS_TOKEN;

    @Autowired
    private AuthenticationService authenticationService;

    /*

        UserController index tests

     */

    @Test
    void whenGetAll_userIsAdmin_thenReturnAllUsersWithEmployees() {
        var user = DatabaseSeeder.insertNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE_USERS);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(Arrays.stream(response.as(UserDTO[].class)).anyMatch(userDTO -> !Objects.equals(userDTO.getId(), user.getId())));
    }

    @Test
    void whenGetAll_userIsSupervisor_thenItselfAndEmployees() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        for (int i = 0; i < 5; i++) {
            DatabaseSeeder.insertNewUser(
                    List.of(new RoleDTO("ROLE_EMPLOYEE")),
                    supervisor.getId(),
                    supervisorAccessToken,
                    API_ROUTE_USERS);
        }

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .get(API_ROUTE_USERS);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1, response.as(UserDTO[].class).length);
        assertEquals(5, response.as(UserDTO[].class)[0].getEmployees().size());
    }

    @Test
    void whenGetAll_userIsEmployee_thenReturnItself() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .get(API_ROUTE_USERS);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(1, response.as(UserDTO[].class).length);
        assertEquals(employeeRequest.getFirstname(), response.as(UserDTO[].class)[0].getFirstname());
    }

    /*

        UserController single tests

     */

    @Test
    void whenGetSingle_userNotExists_thenEntityNotFound() {
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE_USERS + "/9999");

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    void whenGetSingle_andItsNotOwnEmployee_thenInsufficientPermissionsException() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        //User 1 is ADMIN from mocked token
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .get(API_ROUTE_USERS + "/1");

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void whenGetSingle_andUseOwnId_thenReturnItself() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        var employee = DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .get(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(employeeRequest.getFirstname(), response.jsonPath().get("firstname"));
    }

    @Test
    void whenGetSingle_userIsAdmin_thenReturnUser() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        var employee = DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(employeeRequest.getFirstname(), response.jsonPath().get("firstname"));
    }

    /*

        UserController create tests

     */

    @Test
    void createEmployee_userIsEmployee_thenInsufficientPermissionsException() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .body(DatabaseSeeder.createNewUser(
                        List.of(new RoleDTO("ROLE_EMPLOYEE")),
                        null))
                .post(API_ROUTE_USERS);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void createEmployee_userIsSupervisor_whenTriesAssignToOtherSupervisor_thenInsufficientPermissionsException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        //Tries to assign mocked admin
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .body(DatabaseSeeder.createNewUser(
                        List.of(new RoleDTO("ROLE_EMPLOYEE")),
                        1))
                .post(API_ROUTE_USERS);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void createEmployee_userIsSupervisor_whenTriesAssignRoleAdmin_thenInsufficientPermissionsException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .body(DatabaseSeeder.createNewUser(
                        List.of(new RoleDTO("ROLE_ADMIN")),
                        supervisor.getId()))
                .post(API_ROUTE_USERS);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void createEmployee_emptyBody_thenBadRequest() {
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .post(API_ROUTE_USERS);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void createEmployee_withNotExistingRole_thenValidationException() {
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(DatabaseSeeder.createNewUser(
                        List.of(new RoleDTO("NOT_EXISTING_ROLE")),
                        null))
                .post(API_ROUTE_USERS);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void createEmployee_userIsSupervisor_thenReturnSuccess() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .body(DatabaseSeeder.createNewUser(
                        List.of(new RoleDTO("ROLE_EMPLOYEE")),
                        supervisor.getId()))
                .post(API_ROUTE_USERS);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    void createEmployee_userIsAdmin_thenReturnSuccess() {
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(DatabaseSeeder.createNewUser(
                        List.of(new RoleDTO("ROLE_EMPLOYEE")),
                        1))
                .post(API_ROUTE_USERS);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    /*

        UserController update tests

     */

    @Test
    void updateEmployee_emptyBody_thenBadRequestException() {
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .put(API_ROUTE_USERS + "/1");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_whichIsNotExisting_thenEntityNotFoundException() {
        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(DatabaseSeeder.createNewUser(
                        List.of(new RoleDTO("ROLE_EMPLOYEE")),
                        null))
                .put(API_ROUTE_USERS + "/99999");

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_userIsEmployee_thenInsufficientPermissionsException() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        var employee = DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .body(employeeRequest)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_userIsSupervisor_whenTriesAssignToOtherSupervisor_thenInsufficientPermissionsException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        //Tries to change supervisor to mocked admin
        employee.setSupervisorId(1);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .body(employee)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_userIsSupervisor_whenTriesAssignSupervisorAsOneself_thenConflictException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        supervisor.setSupervisorId(supervisor.getId());

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .body(supervisor)
                .put(API_ROUTE_USERS + "/" + supervisor.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_userIsSupervisor_whenTriesAssignSupervisorUserWithoutRole_thenConflictException() {
        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        employee.setRoles(null);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(employee)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_userIsSupervisor_whenTriesAssignRoleAdmin_thenInsufficientPermissionsException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        employee.setRoles(List.of(new RoleDTO("ROLE_ADMIN")));

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .body(employee)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_emptyBody_thenValidationException() {
        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_withNotExistingRole_thenValidationException() {
        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        employee.setRoles(List.of(new RoleDTO("NOT_EXISTING_ROLE")));

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(employee)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_invalidUserId_thenNotFoundException() {
        var employee = DatabaseSeeder.createNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(employee)
                .put(API_ROUTE_USERS + "/9999");

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    void updateEmployee_userIsSupervisor_thenReturnSuccess() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        employee.setEmail("new.email@example.com");
        employee.setFirstname("Newfirstname");
        employee.setLastname("Newlastname");

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .body(employee)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        var employeeResponse = response.as(UserDTO.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(employee.getEmail(), employeeResponse.getEmail());
        assertEquals(employee.getFirstname(), employeeResponse.getFirstname());
        assertEquals(employee.getLastname(), employeeResponse.getLastname());
    }

    @Test
    void updateEmployee_userIsAdmin_thenReturnSuccess() {
        var supervisor = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        employee.setEmail("new.email.admin@example.com");
        employee.setFirstname("Newfirstname");
        employee.setLastname("Newlastname");
        employee.setSupervisorId(1);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(employee)
                .put(API_ROUTE_USERS + "/" + employee.getId());

        var employeeResponse = response.as(UserDTO.class);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(employee.getEmail(), employeeResponse.getEmail());
        assertEquals(employee.getFirstname(), employeeResponse.getFirstname());
        assertEquals(employee.getLastname(), employeeResponse.getLastname());
        assertEquals(employee.getSupervisorId(), employeeResponse.getSupervisorId());
    }

    /*

        UserController delete tests

     */

    @Test
    void deleteEmployee_userIsEmployee_thenInsufficientPermissionsException() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null);

        var employee = DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .delete(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void deleteEmployee_userIsSupervisor_whenTriesDeleteNotOwnEmployee_thenInsufficientPermissionsException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null);

        DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                1, ACCESS_TOKEN, API_ROUTE_USERS);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .delete(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void deleteEmployee_employeeHasItems_thenRelationConstraintViolationException() {
        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        DatabaseSeeder.insertInvoice(2, employee, ACCESS_TOKEN, API_ROUTE_INVOICES);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .delete(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
    }

    @Test
    void deleteEmployee_supervisorHasEmployees_thenRelationConstraintViolationException() {
        var supervisor = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_SUPERVISOR")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .delete(API_ROUTE_USERS + "/" + supervisor.getId());

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatusCode());
    }

    @Test
    void deleteEmployee_thenReturnSuccess() {
        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                null, ACCESS_TOKEN, API_ROUTE_USERS);

        var response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .delete(API_ROUTE_USERS + "/" + employee.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
}
