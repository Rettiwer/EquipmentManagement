package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationRequest;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessTokenExtension;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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

    /*

        UserController index tests

     */

    @Test
    void whenGetAll_userIsAdmin_returnsAllUsersWithEmployees() {
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
    void whenGetAll_userIsSupervisor_returnsItselfAndEmployees() {
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
    void whenGetAll_userIsEmployee_returnsItself() {
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
    void whenGetSingle_userNotExists_returnsEntityNotFound() {

    }

    @Test
    void whenGetSingle_userIsAdmin_returnsUser() {

    }

    @Test
    void whenGetSingle_andItsNotOwnEmployee_returnsInsufficientPermissionsException() {

    }

    @Test
    void whenGetSingle_andUseOwnId_returnItself() {

    }

    /*

        UserController create tests

     */

    @Test
    void createEmployee_userIsEmployee_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void createEmployee_userIsSupervisor_whenTriesAssignToOtherSupervisor_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void createEmployee_userIsSupervisor_whenTriesAssignRoleAdmin_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void createEmployee_emptyBody_thenValidationError() {

    }

    @Test
    void createEmployee_withNotExistingRole_thenValidationError() {

    }

    @Test
    void createEmployee_userIsSupervisor_thenSuccess() {

    }

    @Test
    void createEmployee_userIsAdmin_thenSuccess() {

    }

    /*

        UserController update tests

     */

    @Test
    void updateEmployee_userIsEmployee_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void updateEmployee_userIsSupervisor_whenTriesAssignToOtherSupervisor_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void updateEmployee_userIsSupervisor_whenTriesAssignRoleAdmin_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void updateEmployee_emptyBody_thenValidationError() {

    }

    @Test
    void updateEmployee_withNotExistingRole_thenValidationError() {

    }

    @Test
    void updateEmployee_invalidUserId_thenNotFound() {

    }

    @Test
    void updateEmployee_userIsSupervisor_thenSuccess() {

    }

    /*

        UserController delete tests

     */

    @Test
    void deleteEmployee_userIsEmployee_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void deleteEmployee_userIsSupervisor_whenTriesDeleteNotOwnEmployee_thenThrowInsufficientPermissionsException() {

    }

    @Test
    void deleteEmployee_employeeHasItems_thenRelationConstraintViolationException() {

    }

    @Test
    void deleteEmployee_employeeHasEmployees_thenRelationConstraintViolationException() {

    }

    @Test
    void deleteEmployee_thenSuccess() {

    }

    @Test
    @Order(2)
    void createSupervisorWithEmployee_thenReturnSupervisorWithEmployees() {
//        var supervisorRequest = DatabaseSeeder.createNewUser();
//        supervisorRequest.setRoles(List.of(new RoleDTO("ROLE_SUPERVISOR")));
//
//        //Create supervisor
//        var response = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .body(supervisorRequest)
//                .post(API_ROUTE + "/users").as(UserDTO.class);
//
//        var employeeRequest = DatabaseSeeder.createNewUser();
//        employeeRequest.setRoles(List.of(new RoleDTO("ROLE_EMPLOYEE")));
//        employeeRequest.setSupervisorId(response.getId());
//
//        //Create new employee with supervisor
//        RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .body(employeeRequest)
//                .post(API_ROUTE + "/users");
//
//        //Get supervisor with employees
//        var getSupervisor = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .get(API_ROUTE + "/users/" + response.getId()).as(UserDTO.class);
//
//        assertEquals(HttpStatus.OK.value(), HttpStatus.OK.value());
//        assertNull(response.getEmployees());
//        assertNotNull(getSupervisor.getEmployees());
    }

    @Test
    @Order(3)
    void createEmployee_WithoutPermissions_thenReturnInsufficientPermissionException() {
//        var employeeRequest = DatabaseSeeder.createNewUser();
//        employeeRequest.setRoles(List.of(new RoleDTO("ROLE_EMPLOYEE")));
//
//        String newAccessToken = authenticationService.register(employeeRequest).getAccessToken();
//
//        var secondEmployeeRequest = DatabaseSeeder.createNewUser();
//
//        var response = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + newAccessToken)
//                .body(secondEmployeeRequest)
//                .post(API_ROUTE + "/users");
//
//        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    @Order(4)
    void createEmployee_thenUpdate() {
//        var employeeRequest = DatabaseSeeder.createNewUser();
//        employeeRequest.setRoles(List.of(new RoleDTO("ROLE_EMPLOYEE")));
//        employeeRequest.setSupervisorId(1);
//
//        var response = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .body(employeeRequest)
//                .post(API_ROUTE_USERS).as(UserDTO.class);
//
//        response.setFirstname("TESTNAME");
//        response.getRoles().add(new RoleDTO("ROLE_ADMIN"));
//
//        var employee2 = DatabaseSeeder.createNewUser();
//        employee2.setRoles(List.of(new RoleDTO("ROLE_SUPERVISOR")));
//        var secondSupervisor = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .body(employee2)
//                .post(API_ROUTE_USERS).as(UserDTO.class);
//
//
//        var updateResponse = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .body(response)
//                .put(API_ROUTE_USERS + "/" + response.getId());
//
//        assertEquals(HttpStatus.OK.value(), updateResponse.getStatusCode());
//        assertEquals(response.getFirstname(), updateResponse.as(UserDTO.class).getFirstname());
    }

    @Test
    @Order(5)
    void deleteEmployee_thatNotExists_thenNotFound() {
//        var deleteRequest = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .delete(API_ROUTE_USERS + "/" + 9999);
//
//        assertEquals(HttpStatus.NOT_FOUND.value(), deleteRequest.getStatusCode());
    }

    @Test
    @Order(5)
    void createEmployee_thenDelete() {
//        var employeeRequest = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .body(DatabaseSeeder.createNewUser())
//                .post(API_ROUTE_USERS).as(UserDTO.class);
//
//        DatabaseSeeder.generateInvoice(1, employeeRequest.getId(), ACCESS_TOKEN, API_ROUTE);
//
//        var deleteRequest = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .delete(API_ROUTE_USERS + "/" + employeeRequest.getId());
//
//        var getResponse = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
//                .get(API_ROUTE_USERS + "/" + employeeRequest.getId());
//
//        assertEquals(HttpStatus.OK.value(), deleteRequest.getStatusCode());
//        assertEquals(HttpStatus.NOT_FOUND.value(), getResponse.getStatusCode());
    }
}
