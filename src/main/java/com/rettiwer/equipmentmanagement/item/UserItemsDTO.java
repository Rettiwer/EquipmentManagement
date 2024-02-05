package com.rettiwer.equipmentmanagement.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserItemsDTO {
    private Integer id;
    private String firstname;
    private String lastname;

    private Integer supervisorId;

    private List<ItemDTO> items;
}
