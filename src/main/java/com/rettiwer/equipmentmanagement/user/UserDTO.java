package com.rettiwer.equipmentmanagement.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Integer supervisorId;
    private List<RoleDTO> roles;
    private List<ItemDTO> items;
    private List<UserDTO> employees;
}
