package com.rettiwer.equipmentmanagement.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rettiwer.equipmentmanagement.user.role.RoleDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicUserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private BasicUserDTO supervisor;
    private List<RoleDTO> roles;
}
