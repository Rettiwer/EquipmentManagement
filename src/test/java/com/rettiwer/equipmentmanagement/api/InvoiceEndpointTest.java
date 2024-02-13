package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationRequest;
import com.rettiwer.equipmentmanagement.authentication.AuthenticationService;
import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.api.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.api.jwt.MockAccessTokenExtension;
import com.rettiwer.equipmentmanagement.user.BasicUserDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockAccessTokenExtension.class)
public class InvoiceEndpointTest {
    @Value("${application.api.route}/invoices")
    private String API_ROUTE_INVOICES;

    @Value("${application.api.route}/users")
    private String API_ROUTE_USERS;

    @MockAccessToken
    private String ACCESS_TOKEN;

    private BasicUserDTO userAdmin;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        userAdmin = DatabaseSeeder.getUserById(1, ACCESS_TOKEN, API_ROUTE_USERS);
    }

    @Test
    void getSingle_asEmployee_thenInsufficientPermissionsException() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")), null);

        DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_INVOICES + "/" + userAdmin.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void getSingle_fromOtherOwner_asSupervisor_thenInsufficientPermissionsException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")), null);

        DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        InvoiceItemsDTO invoice = DatabaseSeeder.insertInvoice(2, userAdmin, ACCESS_TOKEN, API_ROUTE_INVOICES);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_INVOICES + "/" + invoice.getId());

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void getSingle_asSupervisor_thenReturnInvoice() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")), null);

        var supervisor = DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        var employee = DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                supervisor.getId(), ACCESS_TOKEN, API_ROUTE_USERS);

        var items = new ArrayList<ItemDTO>();
        items.add(DatabaseSeeder.createNewItem(userAdmin));
        items.add(DatabaseSeeder.createNewItem(employee));
        items.add(DatabaseSeeder.createNewItem(supervisor));

        InvoiceItemsDTO invoice = DatabaseSeeder.insertInvoice(DatabaseSeeder.createNewInvoice(items),
                ACCESS_TOKEN, API_ROUTE_INVOICES);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_INVOICES + "/" + invoice.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(2, response.as(InvoiceItemsDTO.class).getItems().size());
    }

    @Test
    void getSingle_asAdmin_thenReturnInvoice() {
        InvoiceItemsDTO invoice = DatabaseSeeder.insertInvoice(2, userAdmin, ACCESS_TOKEN, API_ROUTE_INVOICES);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_INVOICES + "/" + invoice.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(invoice.getId(), response.as(InvoiceItemsDTO.class).getId());
    }

    @Test
    void create_asEmployee_thenInsufficientPermissionsException() {
        var employeeRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_EMPLOYEE")), null);

        DatabaseSeeder.insertNewUser(employeeRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String employeeAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(employeeRequest.getEmail(), employeeRequest.getPassword()))
                .getAccessToken();

        InvoiceItemsDTO invoice = DatabaseSeeder.createNewInvoice(2, userAdmin);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + employeeAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invoice)
                .post(API_ROUTE_INVOICES);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
    }

    @Test
    void create_asSupervisor_itemToOtherSupervisorEmployee_thenValidationException() {
        var supervisorRequest = DatabaseSeeder.createNewUser(
                List.of(new RoleDTO("ROLE_SUPERVISOR")), null);

        DatabaseSeeder.insertNewUser(supervisorRequest, ACCESS_TOKEN, API_ROUTE_USERS);

        String supervisorAccessToken = authenticationService
                .authenticate(new AuthenticationRequest(supervisorRequest.getEmail(), supervisorRequest.getPassword()))
                .getAccessToken();

        InvoiceItemsDTO invoice = DatabaseSeeder.createNewInvoice(2, userAdmin);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + supervisorAccessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invoice)
                .post(API_ROUTE_INVOICES);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void create_asAdmin_thenReturnSuccess() {
        InvoiceItemsDTO invoice = DatabaseSeeder.createNewInvoice(2, userAdmin);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invoice)
                .post(API_ROUTE_INVOICES);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    void update_asAdmin_thenReturnSuccess() {
        InvoiceItemsDTO invoice = DatabaseSeeder.insertInvoice(2, userAdmin, ACCESS_TOKEN, API_ROUTE_INVOICES);

        var employee =DatabaseSeeder.insertNewUser(List.of(new RoleDTO("ROLE_EMPLOYEE")),
                1, ACCESS_TOKEN, API_ROUTE_USERS);

        invoice.getItems().remove(invoice.getItems().size() - 1);
        invoice.getItems().get(0).setOwner(employee);

        invoice.getItems().add(DatabaseSeeder.createNewItem(employee));

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invoice)
                .put(API_ROUTE_INVOICES + "/" + invoice.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void delete_asAdmin_thenReturnSuccess() {
        InvoiceItemsDTO invoice = DatabaseSeeder.insertInvoice(2, userAdmin, ACCESS_TOKEN, API_ROUTE_INVOICES);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invoice)
                .delete(API_ROUTE_INVOICES + "/" + invoice.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
}
