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

    public static InvoiceItemsDTO generateInvoice(int itemsAmount, int ownerId, String accessToken, String apiRoute) {
        List<ItemDTO> items = new ArrayList<>();
        for (int i = 0; i < itemsAmount; i++) {
            items.add(new ItemDTO(
                    RandomStringUtils.randomAlphabetic(10),
                    new BigDecimal(RandomStringUtils.randomNumeric(3)),
                    RandomStringUtils.randomAlphabetic(10, 20),
                    ownerId));
        }

        InvoiceItemsDTO invoiceDTO = new InvoiceItemsDTO(
                "15/10/2022",
                LocalDate.now(),
                items);

        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .body(invoiceDTO)
                .when()
                .post(apiRoute + "/invoices").as(InvoiceItemsDTO.class);
    }

    public static UserDTO insertNewUser(List<RoleDTO> roles, Integer supervisorId, String accessToken, String apiRoute) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .body(createNewUser(roles, supervisorId))
                .post(apiRoute).as(UserDTO.class);
    }

    public static UserDTO insertNewUser(RegisterRequest request, String accessToken, String apiRoute) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(apiRoute).as(UserDTO.class);
    }

    public static RegisterRequest createNewUser(List<RoleDTO> roles, Integer supervisorId) {
            return new RegisterRequest(
                    RandomStringUtils.randomAlphabetic(5, 10),
                    RandomStringUtils.randomAlphabetic(5, 10),
                    RandomStringUtils.randomAlphabetic(10, 15) + "@example.com",
                    "StrongPass!",
                    roles,
                    supervisorId);
    }
}
