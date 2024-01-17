package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.invoice.Invoice;
import com.rettiwer.equipmentmanagement.invoice.InvoiceDTO;
import com.rettiwer.equipmentmanagement.user.Role;
import com.rettiwer.equipmentmanagement.user.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Date;

import static com.rettiwer.equipmentmanagement.EquipmentManagementApplicationTest.ACCESS_TOKEN;
import static com.rettiwer.equipmentmanagement.EquipmentManagementApplicationTest.API_ROOT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvoiceEndpointTest {

    private static final String API_ROUTE = API_ROOT + "/invoice";

    @Before
    public void setup(){
    }

    @Test
    public void createInvoice_thenCreated() {
        InvoiceDTO invoice = generateNewInvoice();

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
