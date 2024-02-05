package com.rettiwer.equipmentmanagement.user;

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
