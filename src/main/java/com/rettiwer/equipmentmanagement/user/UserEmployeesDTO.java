package com.rettiwer.equipmentmanagement.user;

import com.rettiwer.equipmentmanagement.item.ItemDTO;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserEmployeesDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Integer supervisorId;
    private List<UserEmployeesDTO> employees;
}
