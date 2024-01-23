package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import com.rettiwer.equipmentmanagement.item.ItemInvoiceDTO;
import com.rettiwer.equipmentmanagement.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.jwt.MockAccessTokenExtension;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockAccessTokenExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvoiceEndpointTest {
    @Value("${application.api.route}/invoices")
    private String API_ROUTE;

    @Value("${application.api.route}/items")
    private String API_ROUTE_ITEMS;

    @MockAccessToken
    private String ACCESS_TOKEN;

    @Test
    @Order(1)
    public void creatInvoice_thenUnauthorized() {
        InvoiceItemsDTO invoice = generateNewInvoice();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invoice)
                .post(API_ROUTE);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }
    @Test
    @Order(2)
    public void whenInvoiceCreated_returnCreated() {
        InvoiceItemsDTO invoice = generateNewInvoice();
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(invoice)
                .post(API_ROUTE);

        //assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    @Order(3)
    public void getAllInvoices_thenReturnList() {
        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE);

        response.prettyPrint();

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    @Order(4)
    public void getAllItems_thenReturnList() {
        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .get(API_ROUTE_ITEMS);

        response.prettyPrint();

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }


    private InvoiceItemsDTO generateNewInvoice() {
        List<ItemInvoiceDTO> items = new ArrayList<>();

        items.add(new ItemInvoiceDTO(
                null,
                "Sample Item",
                new BigDecimal("32.3"),
                "Last repaired by Don",//First user from mocked token
                null));

        items.add(new ItemInvoiceDTO(
                null,
                "Sample Second Item",
                new BigDecimal("55.5"),
                null,
                //First user from mocked token
                null));

        return new InvoiceItemsDTO(
                null,
                "15/10/2022",
                LocalDate.now(),
                items
        );
    }
}
