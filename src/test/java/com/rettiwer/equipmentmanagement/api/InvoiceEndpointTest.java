package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.api.utils.DatabaseSeeder;
import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.mocks.jwt.MockAccessTokenExtension;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockAccessTokenExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvoiceEndpointTest {
    @Value("${application.api.route}")
    private String API_ROUTE;

    @Value("${application.api.route}/items")
    private String API_ROUTE_ITEMS;

    @MockAccessToken
    private String ACCESS_TOKEN;

    @Test
    @Order(1)
    void creatInvoice_thenUnauthorized() {
        InvoiceItemsDTO invoice = DatabaseSeeder.generateInvoice(2, 1, ACCESS_TOKEN, API_ROUTE);
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invoice)
                .post(API_ROUTE);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }
    @Test
    @Order(2)
    void whenInvoiceCreated_returnCreated() {
        InvoiceItemsDTO invoice = DatabaseSeeder.generateInvoice(2, 1, ACCESS_TOKEN, API_ROUTE);

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(invoice)
                .post(API_ROUTE);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    @Order(3)
    void getAllInvoices_thenReturnList() {
        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    @Order(4)
    void getAllItems_thenReturnList() {
        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE_ITEMS);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
}
