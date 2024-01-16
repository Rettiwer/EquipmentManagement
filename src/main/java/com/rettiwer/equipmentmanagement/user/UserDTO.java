package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.item.ItemDTO;

import java.util.List;

public record UserDTO(
        String firstname,
        String lastname,
        String email,
        String role,
        List<ItemDTO> items)
{ }
