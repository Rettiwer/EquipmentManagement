package com.rettiwer.equipmentmanagement.api.utils;

import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.user.UserDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder {

    public static InvoiceItemsDTO generateInvoice(int itemsAmount, String ACCESS_TOKEN, String API_ROUTE) {
        List<ItemDTO> items = new ArrayList<>();
        for (int i = 0; i < itemsAmount; i++) {
            items.add(new ItemDTO(
                    RandomStringUtils.randomAlphabetic(10),
                    new BigDecimal(RandomStringUtils.randomNumeric(3)),
                    RandomStringUtils.randomAlphabetic(10, 20),
                    1));
        }

        InvoiceItemsDTO invoiceDTO = new InvoiceItemsDTO(
                "15/10/2022",
                LocalDate.now(),
                items);

        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + ACCESS_TOKEN)
                .body(invoiceDTO)
                .when()
                .post(API_ROUTE + "/invoices").as(InvoiceItemsDTO.class);
    }

    public static RegisterRequest createNewUser() {
        return new RegisterRequest(
                RandomStringUtils.randomAlphabetic(5, 10),
                RandomStringUtils.randomAlphabetic(5, 10),
                RandomStringUtils.randomAlphabetic(10, 15) + "@example.com",
                "StrongPass!",
                List.of(new RoleDTO("ROLE_ADMIN")),
                null);
    }
}
