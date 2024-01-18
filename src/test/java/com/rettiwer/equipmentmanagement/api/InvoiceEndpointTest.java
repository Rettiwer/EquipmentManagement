package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.invoice.InvoiceDTO;
import com.rettiwer.equipmentmanagement.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.jwt.MockAccessTokenExtension;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockAccessTokenExtension.class)
public class InvoiceEndpointTest {
    @Value("${application.api.route}/invoice")
    private String API_ROUTE;

    @MockAccessToken
    private String ACCESS_TOKEN;

    @Test
    public void createInvoice_thenCreated() {
        InvoiceDTO invoice = generateNewInvoice();

        log.info("Bearer " + ACCESS_TOKEN);
        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(invoice)
                .post(API_ROUTE);


        response.prettyPrint();

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    private InvoiceDTO generateNewInvoice() {
        return new InvoiceDTO(
                null,
                "15/10/2022",
                new Date(),
                null
        );
    }
}
