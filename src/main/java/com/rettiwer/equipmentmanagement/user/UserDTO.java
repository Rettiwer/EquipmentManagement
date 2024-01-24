package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.item.ItemInvoiceDTO;
import com.rettiwer.equipmentmanagement.user.role.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private List<ItemInvoiceDTO> items;
    private List<User> employees;
}
