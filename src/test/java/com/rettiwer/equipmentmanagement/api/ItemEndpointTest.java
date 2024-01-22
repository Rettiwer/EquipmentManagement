package com.rettiwer.equipmentmanagement.api;

import com.rettiwer.equipmentmanagement.invoice.Invoice;
import com.rettiwer.equipmentmanagement.invoice.InvoiceDTO;
import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import com.rettiwer.equipmentmanagement.invoice.InvoiceMapper;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.item.ItemInvoiceDTO;
import com.rettiwer.equipmentmanagement.jwt.MockAccessToken;
import com.rettiwer.equipmentmanagement.jwt.MockAccessTokenExtension;
import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockAccessTokenExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemEndpointTest {

    @Value("${application.api.route}/invoice")
    private String API_ROUTE_INVOICE;

    @Value("${application.api.route}/item")
    private String API_ROUTE_ITEMS;

    @MockAccessToken
    private String ACCESS_TOKEN;

    @Autowired
    private InvoiceMapper invoiceMapper;


    private Invoice invoice;

    @BeforeEach
    public void setup() {
        if (invoice == null) {
            generateNewInvoice();
        }
    }

    @Test
    @Order(1)
    public void createItem_thenUnauthorized() {
//        InvoiceItemsDTO invoice = generateNewInvoice();
//        Response response = RestAssured.given()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(invoice)
//                .post(API_ROUTE);
//
//        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode());
    }

    private void generateNewInvoice() {
        InvoiceDTO invoiceDTO = new InvoiceDTO(
                null,
                "15/10/2022",
                LocalDate.now());

        InvoiceDTO response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(invoiceDTO)
                .post(API_ROUTE_INVOICE)
                .as(InvoiceDTO.class);

        invoice = invoiceMapper.toEntity(response);
    }
}
