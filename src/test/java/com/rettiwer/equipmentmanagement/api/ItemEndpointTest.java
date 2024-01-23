package com.rettiwer.equipmentmanagement.api;

import ch.qos.logback.core.model.INamedModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockAccessTokenExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemEndpointTest {

    @Value("${application.api.route}/invoices")
    private String API_ROUTE_INVOICE;

    @Value("${application.api.route}/items")
    private String API_ROUTE_ITEM;

    @MockAccessToken
    private String ACCESS_TOKEN;

    @Autowired
    private InvoiceMapper invoiceMapper;

    private Invoice invoice;

    @BeforeEach
    public void setup() {
        if (invoice == null) invoice = generateNewInvoice();
    }

    @Test
    @Order(1)
    public void whenItemCreated_withoutInvoice_thenBadRequest() {
        ItemDTO itemDTO = new ItemDTO(null, "Without Invoice", new BigDecimal("28.5"),
                "Simple comment", 1, null);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(itemDTO)
                .post(API_ROUTE_ITEM);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    @Order(2)
    public void whenItemCreated_withNotExistingInvoice_thenNotFound() {
        ItemDTO itemDTO = new ItemDTO(null, "Not Existing Invoice", new BigDecimal("28.5"),
                "Simple comment", 1, 100L);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(itemDTO)
                .post(API_ROUTE_ITEM);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    @Order(3)
    public void whenItemCreated_thenReturnItem() {
        ItemDTO itemDTO = new ItemDTO(null, "New Item", new BigDecimal("28.5"),
                "Simple comment", 1, invoice.getId());

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(itemDTO)
                .post(API_ROUTE_ITEM);

        ItemDTO newItemDto = response.as(ItemDTO.class);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertNotNull(newItemDto.getId());
    }

    @Test
    @Order(4)
    public void whenGetSingleItem_thenReturnItem() {
        ItemDTO itemDTO = new ItemDTO(null, "Single Item", new BigDecimal("28.5"),
                null, 1, invoice.getId());

        itemDTO = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(itemDTO)
                .post(API_ROUTE_ITEM).as(ItemDTO.class);

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get(API_ROUTE_ITEM + "/" + itemDTO.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    @Order(5)
    public void whenNotExistingItemUpdate_thenCreated() {
        ItemDTO toUpdateItemDTO = new ItemDTO(100L, "Not Existing Item",
                new BigDecimal("28.5"), null, 1, invoice.getId());

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(toUpdateItemDTO)
                .put(API_ROUTE_ITEM + "/" + toUpdateItemDTO.getId());

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    @Order(6)
    public void whenItemUpdate_thenSuccess() {
        ItemDTO itemDTO = new ItemDTO(null, "Item Update",
                new BigDecimal("28.5"), "Simple comment",
                1, invoice.getId());

        itemDTO = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(itemDTO)
                .post(API_ROUTE_ITEM).as(ItemDTO.class);

        itemDTO.setName("New Super Item");

        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(itemDTO)
                .put(API_ROUTE_ITEM + "/" + itemDTO.getId());

        ItemDTO updatedItem = response.as(ItemDTO.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(updatedItem.getName(), itemDTO.getName());
    }

    @Test
    @Order(7)
    public void whenItemDeleted_thenSuccess() {
        ItemDTO itemDTO = new ItemDTO(null, "Item Delete",
                new BigDecimal("28.5"), "Simple comment",
                1, invoice.getId());

        itemDTO = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(itemDTO)
                .post(API_ROUTE_ITEM).as(ItemDTO.class);


        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete(API_ROUTE_ITEM + "/" + itemDTO.getId());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    @Order(8)
    public void whenItemDeleted_thenNotFound() {
        Response response = RestAssured.given()
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete(API_ROUTE_ITEM + "/" + 100);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }


    private Invoice generateNewInvoice() {
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

        return invoiceMapper.toEntity(response);
    }
}
