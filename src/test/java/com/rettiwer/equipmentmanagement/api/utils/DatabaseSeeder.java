package com.rettiwer.equipmentmanagement.api.utils;

import com.rettiwer.equipmentmanagement.authentication.RegisterRequest;
import com.rettiwer.equipmentmanagement.invoice.InvoiceItemsDTO;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.user.BasicUserDTO;
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

    public static InvoiceItemsDTO insertInvoice(int itemsAmount, BasicUserDTO owner, String accessToken, String apiRoute) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .body(createNewInvoice(itemsAmount, owner))
                .when()
                .post(apiRoute).as(InvoiceItemsDTO.class);
    }

    public static InvoiceItemsDTO insertInvoice(InvoiceItemsDTO invoiceItemsDTO, String accessToken, String apiRoute) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .body(invoiceItemsDTO)
                .when()
                .post(apiRoute).as(InvoiceItemsDTO.class);
    }

    public static InvoiceItemsDTO createNewInvoice(int itemsAmount, BasicUserDTO owner) {
        List<ItemDTO> items = new ArrayList<>();
        for (int i = 0; i < itemsAmount; i++) {
            items.add(createNewItem(owner));
        }

        return new InvoiceItemsDTO(
                "15/10/2022",
                LocalDate.now(),
                items);
    }

    public static InvoiceItemsDTO createNewInvoice(List<ItemDTO> items) {
        return new InvoiceItemsDTO(
                "15/10/2022",
                LocalDate.now(),
                items);
    }

    public static ItemDTO createNewItem(BasicUserDTO owner) {
        return new ItemDTO(
                    RandomStringUtils.randomAlphabetic(10),
                    new BigDecimal(RandomStringUtils.randomNumeric(3)),
                    RandomStringUtils.randomAlphabetic(10, 20),
                    owner);
    }

    public static BasicUserDTO getUserById(Integer userId, String accessToken, String apiRoute) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .get(apiRoute + "/" + userId).as(BasicUserDTO.class);
    }

    public static BasicUserDTO insertNewUser(List<RoleDTO> roles, Integer supervisorId, String accessToken, String apiRoute) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .body(createNewUser(roles, supervisorId))
                .post(apiRoute).as(BasicUserDTO.class);
    }

    public static BasicUserDTO insertNewUser(RegisterRequest request, String accessToken, String apiRoute) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers("Authorization", "Bearer " + accessToken)
                .body(request)
                .post(apiRoute).as(BasicUserDTO.class);
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
